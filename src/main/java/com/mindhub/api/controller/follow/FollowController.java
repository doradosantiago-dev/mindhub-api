package com.mindhub.api.controller.follow;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindhub.api.dto.follow.FollowRequest;
import com.mindhub.api.dto.follow.FollowResponse;
import com.mindhub.api.dto.follow.FollowStatsResponse;
import com.mindhub.api.mapper.follow.FollowMapper;
import com.mindhub.api.model.user.User;
import com.mindhub.api.service.follow.FollowService;
import com.mindhub.api.service.user.UserService;

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
 * Controlador REST para operaciones de seguimiento entre usuarios en la
 * plataforma MindHub.
 *
 * Proporciona endpoints para la gestión de relaciones de seguimiento,
 * incluyendo:
 * Seguir y dejar de seguir usuarios,
 * obtener estadísticas de seguimiento,
 * listar seguidores y seguidos de un usuario,
 * verificar estado de seguimiento,
 * gestionar seguidores y seguidos propios,
 *
 * Todos los endpoints requieren autenticación de usuario y están diseñados
 * para permitir la interacción social a través del sistema de seguimiento.
 *
 * Los usuarios pueden ver las estadísticas de seguimiento de cualquier usuario,
 * pero solo pueden gestionar sus propias relaciones de seguimiento.
 */

@Slf4j
@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Seguimiento", description = "Endpoints para gestión de relaciones de seguimiento entre usuarios")
@SecurityRequirement(name = "bearerAuth")
public class FollowController {

        private final FollowService followService;
        private final UserService userService;
        private final FollowMapper followMapper;

        /**
         * Crea una relación de seguimiento entre el usuario actual y otro usuario.
         * 
         * Este endpoint permite al usuario autenticado seguir a otro usuario de la
         * plataforma.
         * Se valida que el usuario a seguir exista y que no se esté siguiendo ya.
         * 
         * La relación de seguimiento se crea con la fecha y hora actual y permite
         * al usuario seguir recibiendo actualizaciones del usuario seguido.
         * 
         * @param currentUser Usuario autenticado que realiza la acción
         * @param request     Datos de la solicitud de seguimiento (userId)
         * @return ResponseEntity con la relación de seguimiento creada y código 201
         */
        @PostMapping
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Seguir usuario", description = "Crea una relación de seguimiento entre el usuario actual y otro usuario")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Relación de seguimiento creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FollowResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Datos de seguimiento inválidos o ya se está siguiendo"),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "404", description = "Usuario a seguir no encontrado")
        })
        public ResponseEntity<FollowResponse> followUser(
                        @AuthenticationPrincipal User currentUser,
                        @Valid @RequestBody FollowRequest request) {

                log.debug("Usuario {} intentando seguir al usuario {}", currentUser.getUsername(), request.getUserId());

                var follow = followService.follow(currentUser, request.getUserId());
                var response = followMapper.toResponse(follow);

                log.debug("Relación de seguimiento creada exitosamente");

                return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        /**
         * Elimina una relación de seguimiento entre el usuario actual y otro usuario.
         * 
         * Este endpoint permite al usuario autenticado dejar de seguir a otro usuario.
         * Se valida que la relación de seguimiento exista antes de eliminarla.
         * 
         * La eliminación es permanente y el usuario dejará de recibir actualizaciones
         * del usuario que ya no sigue.
         * 
         * @param currentUser Usuario autenticado que realiza la acción
         * @param userId      ID del usuario a dejar de seguir
         * @return ResponseEntity con código 204 (NO_CONTENT) sin cuerpo
         */
        @DeleteMapping("/{userId}")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Dejar de seguir usuario", description = "Elimina la relación de seguimiento entre el usuario actual y otro usuario")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Relación de seguimiento eliminada exitosamente"),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado o relación de seguimiento inexistente")
        })
        public ResponseEntity<Void> unfollowUser(
                        @AuthenticationPrincipal User currentUser,
                        @Parameter(description = "ID del usuario a dejar de seguir", example = "1") @PathVariable Long userId) {

                log.debug("Usuario {} dejando de seguir al usuario {}", currentUser.getUsername(), userId);

                followService.unfollow(currentUser, userId);

                log.debug("Relación de seguimiento eliminada exitosamente");

                return ResponseEntity.noContent().build();
        }

        /**
         * Obtiene las estadísticas de seguimiento de un usuario específico.
         * 
         * Este endpoint permite obtener estadísticas completas de seguimiento de
         * cualquier
         * usuario de la plataforma, incluyendo número de seguidores, seguidos y si el
         * usuario actual sigue o es seguido por el usuario consultado.
         * 
         * @param currentUser Usuario autenticado que consulta las estadísticas
         * @param userId      ID del usuario cuyas estadísticas se consultan
         * @return ResponseEntity con las estadísticas de seguimiento
         */
        @GetMapping("/stats/{userId}")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Obtener estadísticas de seguimiento", description = "Recupera las estadísticas completas de seguimiento de un usuario específico")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FollowStatsResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        })
        public ResponseEntity<FollowStatsResponse> getFollowStats(
                        @AuthenticationPrincipal User currentUser,
                        @Parameter(description = "ID del usuario para consultar estadísticas", example = "1") @PathVariable Long userId) {

                log.debug("Obteniendo estadísticas de seguimiento para usuario ID: {}", userId);

                User user = userService.findByIdOrThrow(userId);

                var stats = followService.getFollowStats(currentUser, user);
                var response = followMapper.toStatsResponse(user, stats.getFollowers(),
                                stats.getFollowed(), stats.getFollows(), stats.getFollowsYou());

                log.debug("Estadísticas de seguimiento obtenidas exitosamente");

                return ResponseEntity.ok(response);
        }

        /**
         * Obtiene una lista paginada de seguidores de un usuario específico.
         * 
         * Este endpoint permite obtener todos los usuarios que siguen a un usuario
         * específico con soporte para paginación. Los seguidores se ordenan por
         * fecha de seguimiento (más recientes primero).
         * 
         * @param currentUser Usuario autenticado que consulta los seguidores
         * @param userId      ID del usuario cuyos seguidores se consultan
         * @param pageable    Información de paginación (número de página, tamaño,
         *                    ordenamiento)
         * @return ResponseEntity con la página de seguidores
         */
        @GetMapping("/{userId}/followers")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Obtener seguidores de un usuario", description = "Recupera una lista paginada de usuarios que siguen a un usuario específico")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Seguidores obtenidos exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FollowResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        })
        public ResponseEntity<Page<FollowResponse>> getFollowers(
                        @AuthenticationPrincipal User currentUser,
                        @Parameter(description = "ID del usuario", example = "1") @PathVariable Long userId,
                        @Parameter(description = "Parámetros de paginación", example = "page=0&size=20&sort=followDate,desc") @PageableDefault(size = 20) Pageable pageable) {

                log.debug("Obteniendo seguidores para usuario ID: {}, página: {}", userId, pageable.getPageNumber());

                User user = userService.findByIdOrThrow(userId);

                Page<com.mindhub.api.model.follow.Follow> follows = followService.getFollowsByFollowed(user,
                                pageable);

                Page<FollowResponse> response = follows.map(followMapper::toResponse);

                log.debug("Seguidores obtenidos: {} para usuario ID: {}", response.getContent().size(), userId);

                return ResponseEntity.ok(response);
        }

        /**
         * Obtiene una lista paginada de usuarios que sigue un usuario específico.
         * 
         * Este endpoint permite obtener todos los usuarios que un usuario específico
         * está siguiendo con soporte para paginación. Los seguidos se ordenan por
         * fecha de seguimiento (más recientes primero).
         * 
         * @param currentUser Usuario autenticado que consulta los seguidos
         * @param userId      ID del usuario cuyos seguidos se consultan
         * @param pageable    Información de paginación (número de página, tamaño,
         *                    ordenamiento)
         * @return ResponseEntity con la página de seguidos
         */
        @GetMapping("/{userId}/followed")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Obtener seguidos de un usuario", description = "Recupera una lista paginada de usuarios que un usuario específico está siguiendo")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Seguidos obtenidos exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FollowResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        })
        public ResponseEntity<Page<FollowResponse>> getFollowing(
                        @AuthenticationPrincipal User currentUser,
                        @Parameter(description = "ID del usuario", example = "1") @PathVariable Long userId,
                        @Parameter(description = "Parámetros de paginación", example = "page=0&size=20&sort=followDate,desc") @PageableDefault(size = 20) Pageable pageable) {

                log.debug("Obteniendo seguidos para usuario ID: {}, página: {}", userId, pageable.getPageNumber());

                User user = userService.findByIdOrThrow(userId);

                Page<com.mindhub.api.model.follow.Follow> follows = followService.getFollowsByFollower(user,
                                pageable);

                Page<FollowResponse> response = follows.map(followMapper::toResponse);

                log.debug("Seguidos obtenidos: {} para usuario ID: {}", response.getContent().size(), userId);

                return ResponseEntity.ok(response);
        }

        /**
         * Verifica si el usuario actual sigue a otro usuario específico.
         * 
         * Este endpoint permite verificar el estado de seguimiento entre el usuario
         * autenticado y otro usuario específico. Retorna true si existe una relación
         * de seguimiento, false en caso contrario.
         * 
         * @param currentUser Usuario autenticado que consulta el estado
         * @param userId      ID del usuario a verificar si se está siguiendo
         * @return ResponseEntity con el estado de seguimiento (true/false)
         */
        @GetMapping("/check/{userId}")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Verificar estado de seguimiento", description = "Verifica si el usuario actual sigue a otro usuario específico")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Estado de seguimiento verificado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(example = "true"))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        })
        public ResponseEntity<Boolean> checkFollow(
                        @AuthenticationPrincipal User currentUser,
                        @Parameter(description = "ID del usuario a verificar", example = "1") @PathVariable Long userId) {

                log.debug("Verificando estado de seguimiento para usuario ID: {}", userId);

                User user = userService.findByIdOrThrow(userId);

                boolean follows = followService.follows(currentUser, user);

                log.debug("Estado de seguimiento verificado: {} para usuario ID: {}", follows, userId);

                return ResponseEntity.ok(follows);
        }

        /**
         * Obtiene una lista paginada de seguidores del usuario actual.
         * 
         * Este endpoint permite al usuario autenticado obtener todos sus seguidores
         * con soporte para paginación. Los seguidores se ordenan por fecha de
         * seguimiento (más recientes primero).
         * 
         * @param currentUser Usuario autenticado que consulta sus seguidores
         * @param pageable    Información de paginación (número de página, tamaño,
         *                    ordenamiento)
         * @return ResponseEntity con la página de seguidores del usuario actual
         */
        @GetMapping("/my-followers")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Obtener mis seguidores", description = "Recupera una lista paginada de usuarios que siguen al usuario actual")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Seguidores obtenidos exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FollowResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación")
        })
        public ResponseEntity<Page<FollowResponse>> getMyFollowers(
                        @AuthenticationPrincipal User currentUser,
                        @Parameter(description = "Parámetros de paginación", example = "page=0&size=20&sort=followDate,desc") @PageableDefault(size = 20) Pageable pageable) {

                log.debug("Obteniendo seguidores del usuario actual, página: {}", pageable.getPageNumber());

                Page<com.mindhub.api.model.follow.Follow> follows = followService.getFollowsByFollowed(currentUser,
                                pageable);

                Page<FollowResponse> response = follows.map(followMapper::toResponse);

                log.debug("Seguidores obtenidos: {} para usuario actual", response.getContent().size());

                return ResponseEntity.ok(response);
        }

        /**
         * Obtiene una lista paginada de usuarios que el usuario actual está siguiendo.
         * 
         * Este endpoint permite al usuario autenticado obtener todos los usuarios
         * que está siguiendo con soporte para paginación. Los seguidos se ordenan
         * por fecha de seguimiento (más recientes primero).
         * 
         * @param currentUser Usuario autenticado que consulta sus seguidos
         * @param pageable    Información de paginación (número de página, tamaño,
         *                    ordenamiento)
         * @return ResponseEntity con la página de seguidos del usuario actual
         */
        @GetMapping("/my-following")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Obtener mis seguidos", description = "Recupera una lista paginada de usuarios que el usuario actual está siguiendo")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Seguidos obtenidos exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FollowResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación")
        })
        public ResponseEntity<Page<FollowResponse>> getMyFollowing(
                        @AuthenticationPrincipal User currentUser,
                        @Parameter(description = "Parámetros de paginación", example = "page=0&size=20&sort=followDate,desc") @PageableDefault(size = 20) Pageable pageable) {

                log.debug("Obteniendo seguidos del usuario actual, página: {}", pageable.getPageNumber());

                Page<com.mindhub.api.model.follow.Follow> follows = followService.getFollowsByFollower(currentUser,
                                pageable);

                Page<FollowResponse> response = follows.map(followMapper::toResponse);

                log.debug("Seguidos obtenidos: {} para usuario actual", response.getContent().size());

                return ResponseEntity.ok(response);
        }

}
