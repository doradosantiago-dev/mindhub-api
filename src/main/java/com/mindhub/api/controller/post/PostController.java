package com.mindhub.api.controller.post;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mindhub.api.dto.post.PostCreateRequest;
import com.mindhub.api.dto.post.PostResponse;
import com.mindhub.api.model.enums.PrivacyType;
import com.mindhub.api.model.user.User;
import com.mindhub.api.service.post.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para operaciones de publicaciones en la plataforma MindHub.
 *
 * Proporciona endpoints para crear y actualizar publicaciones, obtener feeds
 * personalizados y públicos,
 * gestionar publicaciones propias y de otros usuarios, cambiar la privacidad de
 * las publicaciones,
 * eliminar publicaciones y administrar publicaciones reportadas por la
 * comunidad (solo accesible para administradores).
 *
 * Todos los endpoints requieren autenticación de usuario y están diseñados para
 * permitir la creación y gestión de contenido
 * dentro de la plataforma social. Los usuarios pueden modificar o eliminar
 * únicamente sus propias publicaciones,
 * mientras que los administradores tienen la capacidad de gestionar aquellas
 * que han sido reportadas.
 */

@Slf4j
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Posts", description = "Endpoints para gestión de publicaciones y contenido de la plataforma")
@SecurityRequirement(name = "bearerAuth")
public class PostController {

        private final PostService postService;

        /**
         * Crea una nueva publicación en la plataforma.
         * 
         * Este endpoint permite a los usuarios autenticados crear nuevas publicaciones
         * con
         * contenido, tipo de privacidad y otros metadatos. La publicación se asocia
         * automáticamente al usuario autenticado.
         * 
         * La publicación se crea con la fecha y hora actual y se almacena en la base
         * de datos para su posterior recuperación y visualización.
         * 
         * @param request Datos de la publicación a crear (contenido, privacidad, etc.)
         * @return ResponseEntity con la publicación creada y código 201 (CREATED)
         */
        @PostMapping
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Crear publicación", description = "Crea una nueva publicación en la plataforma con el contenido especificado")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Publicación creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Datos de la publicación inválidos"),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación")
        })
        public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostCreateRequest request) {
                log.debug("Creando nueva publicación con contenido: {}",
                                request.content().substring(0, Math.min(50, request.content().length())));

                PostResponse response = postService.createPost(request);

                log.debug("Publicación creada exitosamente con ID: {}", response.id());

                return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        /**
         * Obtiene el feed personalizado del usuario autenticado.
         *
         * Este endpoint proporciona un feed personalizado que incluye publicaciones de
         * usuarios
         * que el usuario actual sigue, así como publicaciones públicas. Las
         * publicaciones se ordenan
         * por fecha de creación (más recientes primero).
         * 
         * @param currentUser Usuario autenticado que consulta su feed
         * @param pageable    Información de paginación (número de página, tamaño,
         *                    ordenamiento)
         * @return ResponseEntity con la página de publicaciones del feed personalizado
         */
        @GetMapping("/feed")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Obtener feed personalizado", description = "Recupera el feed personalizado del usuario con publicaciones de usuarios seguidos y públicas")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Feed personalizado obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación")
        })
        public ResponseEntity<Page<PostResponse>> getPersonalFeed(
                        @AuthenticationPrincipal User currentUser,
                        @Parameter(description = "Parámetros de paginación", example = "page=0&size=10&sort=creationDate,desc") @PageableDefault(size = 10, sort = "creationDate", direction = Sort.Direction.DESC) Pageable pageable) {
                log.debug("Obteniendo feed personalizado para usuario: {}, página: {}", currentUser.getUsername(),
                                pageable.getPageNumber());

                Page<PostResponse> posts = postService.getPersonalFeed(currentUser, pageable);

                log.debug("Feed personalizado obtenido: {} publicaciones para usuario: {}", posts.getContent().size(),
                                currentUser.getUsername());

                return ResponseEntity.ok(posts);
        }

        /**
         * Obtiene una lista de publicaciones públicas de la plataforma.
         * 
         * Este endpoint permite obtener todas las publicaciones marcadas como públicas
         * en la plataforma, independientemente del usuario que las haya creado.
         * Las publicaciones se ordenan por fecha de creación (más recientes primero).
         * 
         * @param pageable Información de paginación (número de página, tamaño,
         *                 ordenamiento)
         * @return ResponseEntity con la página de publicaciones públicas
         */
        @GetMapping("/public")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Obtener publicaciones públicas", description = "Recupera una lista de todas las publicaciones públicas de la plataforma")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Publicaciones públicas obtenidas exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación")
        })
        public ResponseEntity<Page<PostResponse>> getPublicPosts(
                        @Parameter(description = "Parámetros de paginación", example = "page=0&size=10&sort=creationDate,desc") @PageableDefault(size = 10) Pageable pageable) {
                log.debug("Obteniendo publicaciones públicas, página: {}", pageable.getPageNumber());

                Page<PostResponse> posts = postService.getPublicPosts(pageable);

                log.debug("Publicaciones públicas obtenidas: {} publicaciones", posts.getContent().size());
                return ResponseEntity.ok(posts);
        }

        /**
         * Obtiene las publicaciones del usuario autenticado.
         * 
         * Este endpoint permite al usuario autenticado obtener todas sus propias
         * publicaciones con soporte para paginación. Las publicaciones se ordenan por
         * fecha
         * de creación (más recientes primero).
         * 
         * @param pageable Información de paginación (número de página, tamaño,
         *                 ordenamiento)
         * @return ResponseEntity con la página de publicaciones del usuario actual
         */
        @GetMapping("/me")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Obtener mis publicaciones", description = "Recupera todas las publicaciones del usuario autenticado")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Publicaciones obtenidas exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación")
        })
        public ResponseEntity<Page<PostResponse>> getMyPosts(
                        @Parameter(description = "Parámetros de paginación", example = "page=0&size=10&sort=creationDate,desc") @PageableDefault(size = 10, sort = "creationDate", direction = Sort.Direction.DESC) Pageable pageable) {
                log.debug("Obteniendo publicaciones del usuario actual, página: {}", pageable.getPageNumber());

                Page<PostResponse> posts = postService.getMyPosts(pageable);

                log.debug("Publicaciones obtenidas: {} publicaciones del usuario actual", posts.getContent().size());
                return ResponseEntity.ok(posts);
        }

        /**
         * Obtiene las publicaciones de un usuario específico.
         * 
         * Este endpoint permite obtener todas las publicaciones de un usuario
         * específico
         * que sean accesibles según la configuración de privacidad. Las publicaciones
         * se ordenan por fecha de creación (más recientes primero).
         * 
         * @param userId   ID del usuario cuyos publicaciones se consultan
         * @param pageable Información de paginación (número de página, tamaño,
         *                 ordenamiento)
         * @return ResponseEntity con la página de publicaciones del usuario
         */
        @GetMapping("/user/{userId}")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Obtener publicaciones de un usuario", description = "Recupera todas las publicaciones accesibles de un usuario específico")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Publicaciones del usuario obtenidas exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        })
        public ResponseEntity<Page<PostResponse>> getUserPosts(
                        @Parameter(description = "ID del usuario", example = "1") @PathVariable Long userId,
                        @Parameter(description = "Parámetros de paginación", example = "page=0&size=10&sort=creationDate,desc") @PageableDefault(size = 10, sort = "creationDate", direction = Sort.Direction.DESC) Pageable pageable) {
                log.debug("Obteniendo publicaciones para usuario ID: {}, página: {}", userId, pageable.getPageNumber());

                Page<PostResponse> posts = postService.getUserPosts(userId, pageable);

                log.debug("Publicaciones obtenidas: {} publicaciones para usuario ID: {}", posts.getContent().size(),
                                userId);
                return ResponseEntity.ok(posts);
        }

        /**
         * Obtiene una lista de publicaciones reportadas (solo administradores).
         * 
         * Este endpoint permite a los administradores obtener todas las publicaciones
         * que han sido reportadas por otros usuarios. Es útil para la moderación
         * de contenido en la plataforma.
         * 
         * @param pageable Información de paginación (número de página, tamaño,
         *                 ordenamiento)
         * @return ResponseEntity con la página de publicaciones reportadas
         */
        @GetMapping("/reported")
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(summary = "Obtener publicaciones reportadas", description = "Recupera todas las publicaciones que han sido reportadas (solo administradores)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Publicaciones reportadas obtenidas exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol ADMIN")
        })
        public ResponseEntity<Page<PostResponse>> getReportedPosts(
                        @Parameter(description = "Parámetros de paginación", example = "page=0&size=10&sort=creationDate,desc") @PageableDefault(size = 10) Pageable pageable) {
                log.debug("Obteniendo publicaciones reportadas, página: {}", pageable.getPageNumber());

                Page<PostResponse> posts = postService.getReportedPosts(pageable);

                log.debug("Publicaciones reportadas obtenidas: {} publicaciones", posts.getContent().size());
                return ResponseEntity.ok(posts);
        }

        /**
         * Obtiene una publicación específica por su ID.
         * 
         * Este endpoint permite recuperar una publicación específica usando su ID
         * único.
         * La publicación incluye información completa como autor, contenido, fecha
         * de creación, privacidad y metadatos asociados.
         * 
         * @param id ID único de la publicación a obtener
         * @return ResponseEntity con la publicación encontrada
         */
        @GetMapping("/{id}")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Obtener publicación por ID", description = "Recupera una publicación específica usando su ID único")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Publicación obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "404", description = "Publicación no encontrada")
        })
        public ResponseEntity<PostResponse> getPostById(
                        @Parameter(description = "ID de la publicación", example = "1") @PathVariable Long id) {
                log.debug("Obteniendo publicación con ID: {}", id);
                PostResponse response = postService.getPostById(id);

                log.debug("Publicación obtenida exitosamente: {}", id);

                return ResponseEntity.ok(response);
        }

        /**
         * Actualiza una publicación existente.
         * 
         * Este endpoint permite a los usuarios actualizar el contenido de sus propias
         * publicaciones. Solo el autor de la publicación puede modificarla,
         * garantizando la integridad
         * del contenido.
         * 
         * La actualización mantiene la fecha de creación original pero actualiza
         * el contenido y metadatos de la publicación.
         * 
         * @param id      ID de la publicación a actualizar
         * @param request Nuevos datos de la publicación
         * @return ResponseEntity con la publicación actualizada
         */
        @PutMapping("/{id}")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Actualizar publicación", description = "Actualiza el contenido de una publicación existente")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Publicación actualizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Datos de la publicación inválidos"),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "403", description = "Prohibido - Solo el autor puede modificar la publicación"),
                        @ApiResponse(responseCode = "404", description = "Publicación no encontrada")
        })
        public ResponseEntity<PostResponse> updatePost(
                        @Parameter(description = "ID de la publicación", example = "1") @PathVariable Long id,
                        @Valid @RequestBody PostCreateRequest request) {
                log.debug("Actualizando publicación con ID: {}", id);

                PostResponse response = postService.updatePost(id, request);

                log.debug("Publicación actualizada exitosamente: {}", id);

                return ResponseEntity.ok(response);
        }

        /**
         * Cambia la privacidad de una publicación existente.
         * 
         * Este endpoint permite a los usuarios cambiar la configuración de privacidad
         * de sus propias publicaciones. Solo el autor de la publicación puede modificar
         * su privacidad.
         * 
         * Los tipos de privacidad disponibles son PUBLIC, PRIVATE y FRIENDS_ONLY.
         * 
         * @param id          ID de la publicación a modificar
         * @param privacyType Nuevo tipo de privacidad para la publicación
         * @return ResponseEntity con mensaje de confirmación
         */
        @PutMapping("/{id}/privacy")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Cambiar privacidad de la publicación", description = "Modifica la configuración de privacidad de una publicación existente")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Privacidad de la publicación actualizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                                        {
                                          "message": "Privacidad de la publicación actualizada"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "403", description = "Prohibido - Solo el autor puede modificar la publicación"),
                        @ApiResponse(responseCode = "404", description = "Publicación no encontrada")
        })
        public ResponseEntity<Map<String, String>> changePostPrivacy(
                        @Parameter(description = "ID de la publicación", example = "1") @PathVariable Long id,
                        @Parameter(description = "Nuevo tipo de privacidad", example = "PUBLIC") @RequestParam PrivacyType privacyType) {
                log.debug("Cambiando privacidad de la publicación ID: {} a: {}", id, privacyType);

                postService.changePostPrivacy(id, privacyType);

                log.debug("Privacidad de la publicación actualizada exitosamente: {}", id);
                return ResponseEntity.ok(Map.of("message", "Privacidad de la publicación actualizada"));
        }

        /**
         * Elimina una publicación existente.
         * 
         * Este endpoint permite a los usuarios eliminar sus propias publicaciones.
         * Solo el autor de la publicación puede eliminarla, garantizando la integridad
         * del contenido y la privacidad del usuario.
         * 
         * La eliminación es permanente y no se puede deshacer.
         * 
         * @param id ID de la publicación a eliminar
         * @return ResponseEntity con mensaje de confirmación
         */
        @DeleteMapping("/{id}")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Eliminar publicación", description = "Elimina permanentemente una publicación existente")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Publicación eliminada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                                        {
                                          "message": "Publicación eliminada exitosamente"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "403", description = "Prohibido - Solo el autor puede eliminar la publicación"),
                        @ApiResponse(responseCode = "404", description = "Publicación no encontrada")
        })
        public ResponseEntity<Map<String, String>> deletePost(
                        @Parameter(description = "ID de la publicación", example = "1") @PathVariable Long id) {
                log.debug("Eliminando publicación con ID: {}", id);
                postService.deleteById(id);

                log.debug("Publicación eliminada exitosamente: {}", id);

                return ResponseEntity.ok(Map.of("message", "Publicación eliminada exitosamente"));
        }

}
