package com.mindhub.api.controller.comment;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindhub.api.dto.comment.CommentRequest;
import com.mindhub.api.dto.comment.CommentResponse;
import com.mindhub.api.service.comment.CommentService;

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
 * Controlador REST para operaciones de comentarios en la plataforma MindHub.
 *
 * Proporciona endpoints para la gestión de comentarios, incluyendo:
 * Creación de nuevos comentarios en publicaciones, obtención de comentarios por
 * ID,
 * listado paginado de comentarios por publicación,
 * actualización de comentarios existentes,
 * eliminación de comentarios
 *
 * Todos los endpoints requieren autenticación de usuario y están diseñados
 * para permitir la interacción social a través de comentarios en las
 * publicaciones
 * de la plataforma.
 *
 * Los usuarios solo pueden modificar o eliminar sus propios comentarios,
 * garantizando la integridad y privacidad del contenido.
 */

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Comentarios", description = "Endpoints para gestión de comentarios en publicaciones")
@SecurityRequirement(name = "bearerAuth")
public class CommentController {

        private final CommentService commentService;

        /**
         * Crea un nuevo comentario en una publicación específica.
         * 
         * Este endpoint permite a los usuarios autenticados crear comentarios en
         * publicaciones
         * existentes. El comentario se asocia automáticamente al usuario autenticado
         * y se valida que la publicación exista antes de crear el comentario.
         * 
         * El comentario se crea con la fecha y hora actual y se almacena en la base
         * de datos para su posterior recuperación.
         * 
         * @param request Datos del comentario a crear (contenido, postId)
         * @return ResponseEntity con el comentario creado y código 201 (CREATED)
         */
        @PostMapping
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Crear comentario", description = "Crea un nuevo comentario en una publicación específica")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Comentario creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Datos del comentario inválidos"),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "404", description = "Publicación no encontrada")
        })
        public ResponseEntity<CommentResponse> createComment(@Valid @RequestBody CommentRequest request) {
                log.debug("Creando comentario para publicación ID: {}", request.postId());

                CommentResponse response = commentService.createComment(request);

                log.debug("Comentario creado exitosamente con ID: {}", response.id());

                return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        /**
         * Obtiene un comentario específico por su ID.
         * 
         * Este endpoint permite recuperar un comentario específico usando su ID único.
         * El comentario incluye información completa como autor, contenido, fecha
         * de creación y la publicación a la que pertenece.
         * 
         * @param id ID único del comentario a obtener
         * @return ResponseEntity con el comentario encontrado
         */
        @GetMapping("/{id}")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Obtener comentario por ID", description = "Recupera un comentario específico usando su ID único")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Comentario obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "404", description = "Comentario no encontrado")
        })
        public ResponseEntity<CommentResponse> getCommentById(
                        @Parameter(description = "ID del comentario", example = "1") @PathVariable Long id) {
                log.debug("Obteniendo comentario con ID: {}", id);

                CommentResponse comment = commentService.getCommentById(id);

                log.debug("Comentario obtenido exitosamente: {}", id);

                return ResponseEntity.ok(comment);
        }

        /**
         * Obtiene una lista paginada de comentarios para una publicación específica.
         * 
         * Este endpoint permite recuperar todos los comentarios de una publicación
         * específica
         * con soporte para paginación. Los comentarios se ordenan por fecha de creación
         * (más recientes primero) y se pueden personalizar los parámetros de
         * paginación.
         * 
         * El tamaño de página por defecto es de 10 comentarios, pero puede ser
         * personalizado a través del parámetro pageable.
         * 
         * @param postId   ID de la publicación para obtener sus comentarios
         * @param pageable Información de paginación (número de página, tamaño,
         *                 ordenamiento)
         * @return ResponseEntity con la página de comentarios de la publicación
         */
        @GetMapping("/post/{postId}")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Obtener comentarios de una publicación", description = "Recupera una lista paginada de comentarios para una publicación específica")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Comentarios obtenidos exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "404", description = "Publicación no encontrada")
        })
        public ResponseEntity<Page<CommentResponse>> getPostComments(
                        @Parameter(description = "ID de la publicación", example = "1") @PathVariable Long postId,
                        @Parameter(description = "Parámetros de paginación", example = "page=0&size=10&sort=creationDate,desc") @PageableDefault(size = 10) Pageable pageable) {
                log.debug("Obteniendo comentarios para publicación ID: {}, página: {}", postId,
                                pageable.getPageNumber());

                Page<CommentResponse> comments = commentService.getPostComments(postId, pageable);

                log.debug("Comentarios obtenidos: {} para publicación ID: {}", comments.getContent().size(), postId);
                return ResponseEntity.ok(comments);
        }

        /**
         * Actualiza un comentario existente.
         * 
         * Este endpoint permite a los usuarios actualizar el contenido de sus propios
         * comentarios. Solo el autor del comentario puede modificarlo, garantizando
         * la integridad del contenido.
         * 
         * La actualización mantiene la fecha de creación original pero actualiza
         * el contenido del comentario.
         * 
         * @param id      ID del comentario a actualizar
         * @param request Nuevos datos del comentario
         * @return ResponseEntity con el comentario actualizado
         */
        @PutMapping("/{id}")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Actualizar comentario", description = "Actualiza el contenido de un comentario existente")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Comentario actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Datos del comentario inválidos"),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "403", description = "Prohibido - Solo el autor puede modificar el comentario"),
                        @ApiResponse(responseCode = "404", description = "Comentario no encontrado")
        })
        public ResponseEntity<CommentResponse> updateComment(
                        @Parameter(description = "ID del comentario", example = "1") @PathVariable Long id,
                        @Valid @RequestBody CommentRequest request) {
                log.debug("Actualizando comentario con ID: {}", id);

                CommentResponse response = commentService.updateComment(id, request);

                log.debug("Comentario actualizado exitosamente: {}", id);

                return ResponseEntity.ok(response);
        }

        /**
         * Elimina un comentario existente.
         * 
         * Este endpoint permite a los usuarios eliminar sus propios comentarios.
         * Solo el autor del comentario puede eliminarlo, garantizando la integridad
         * del contenido y la privacidad del usuario.
         * 
         * La eliminación es permanente y no se puede deshacer.
         * 
         * @param id ID del comentario a eliminar
         * @return ResponseEntity con mensaje de confirmación
         */
        @DeleteMapping("/{id}")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Eliminar comentario", description = "Elimina permanentemente un comentario existente")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Comentario eliminado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                                        {
                                          "message": "Comment deleted successfully"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "403", description = "Prohibido - Solo el autor puede eliminar el comentario"),
                        @ApiResponse(responseCode = "404", description = "Comentario no encontrado")
        })
        public ResponseEntity<Map<String, String>> deleteComment(
                        @Parameter(description = "ID del comentario", example = "1") @PathVariable Long id) {
                log.debug("Eliminando comentario con ID: {}", id);

                commentService.deleteById(id);

                log.debug("Comentario eliminado exitosamente: {}", id);

                return ResponseEntity.ok(Map.of("message", "Comentario eliminado exitosamente"));
        }

}
