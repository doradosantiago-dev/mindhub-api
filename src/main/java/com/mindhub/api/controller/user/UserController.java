package com.mindhub.api.controller.user;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mindhub.api.dto.auth.UserRegisterRequest;
import com.mindhub.api.dto.auth.UserResponse;
import com.mindhub.api.dto.follow.FollowStatsResponse;
import com.mindhub.api.dto.user.AdminUserUpdateRequest;
import com.mindhub.api.dto.user.UserProfileRequest;
import com.mindhub.api.dto.user.UserProfileResponse;
import com.mindhub.api.dto.user.UserUpdateRequest;
import com.mindhub.api.mapper.follow.FollowMapper;
import com.mindhub.api.model.enums.PrivacyType;
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
 * Controlador REST para operaciones de usuarios en la plataforma MindHub.
 *
 * Este controlador proporciona endpoints para la gestión de usuarios,
 * incluyendo la consulta de información,
 * búsqueda y listado, actualización de perfiles, gestión de privacidad,
 * activación y desactivación (solo administradores),
 * eliminación de usuarios, creación de usuarios administradores y gestión de
 * perfiles extendidos.
 *
 * Todos los endpoints requieren autenticación de usuario y están diseñados para
 * permitir la gestión completa
 * de usuarios y perfiles en la plataforma. Los usuarios solo pueden modificar
 * sus propios datos,
 * mientras que los administradores tienen acceso a la gestión de todos los
 * usuarios.
 *
 */

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Usuarios", description = "Endpoints para gestión de usuarios y perfiles")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

        private final UserService userService;
        private final FollowService followService;
        private final FollowMapper followMapper;

        /**
         * Obtiene la información del usuario autenticado actual.
         * 
         * Este endpoint permite al usuario autenticado obtener su información
         * completa incluyendo perfil y datos personales.
         * 
         * @return ResponseEntity con la información del usuario actual
         */
        @GetMapping("/me")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Obtener usuario actual", description = "Recupera la información completa del usuario autenticado")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Usuario actual obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación")
        })
        public ResponseEntity<UserResponse> getCurrentUser() {
                log.debug("Obteniendo información del usuario actual");

                UserResponse response = userService.getCurrentUserWithProfile();

                log.debug("Usuario actual obtenido exitosamente");

                return ResponseEntity.ok(response);
        }

        /**
         * Obtiene la información de un usuario específico por su ID.
         * 
         * Este endpoint permite obtener la información pública de cualquier
         * usuario de la plataforma usando su ID único.
         * 
         * @param id ID del usuario a consultar
         * @return ResponseEntity con la información del usuario
         */
        @GetMapping("/{id}")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Obtener usuario por ID", description = "Recupera la información de un usuario específico usando su ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Usuario obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        })
        public ResponseEntity<UserResponse> getUserById(
                        @Parameter(description = "ID del usuario", example = "1") @PathVariable Long id) {
                log.debug("Obteniendo usuario con ID: {}", id);

                UserResponse response = userService.findByIdAsResponse(id);

                log.debug("Usuario obtenido exitosamente: {}", id);

                return ResponseEntity.ok(response);
        }

        /**
         * Obtiene las estadísticas de seguimiento de un usuario específico.
         * 
         * Este endpoint permite obtener estadísticas completas de seguimiento
         * de cualquier usuario, incluyendo número de seguidores, seguidos
         * y relaciones de seguimiento.
         * 
         * @param id ID del usuario cuyas estadísticas se consultan
         * @return ResponseEntity con las estadísticas de seguimiento
         */
        @GetMapping("/{id}/follow-stats")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Obtener estadísticas de seguimiento", description = "Recupera las estadísticas de seguimiento de un usuario específico")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FollowStatsResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        })
        public ResponseEntity<FollowStatsResponse> getUserFollowStats(
                        @Parameter(description = "ID del usuario", example = "1") @PathVariable Long id) {
                log.debug("Obteniendo estadísticas de seguimiento para usuario ID: {}", id);

                User currentUser = userService.getCurrentUser();

                User targetUser = userService.findByIdOrThrow(id);

                var stats = followService.getFollowStats(currentUser, targetUser);

                var response = followMapper.toStatsResponse(targetUser, stats.getFollowers(),
                                stats.getFollowed(), stats.getFollows(), stats.getFollowsYou());

                log.debug("Estadísticas de seguimiento obtenidas exitosamente para usuario ID: {}", id);

                return ResponseEntity.ok(response);
        }

        /**
         * Busca usuarios públicos en la plataforma.
         * 
         * Este endpoint permite buscar usuarios que tengan perfiles públicos
         * usando un término de búsqueda. Los resultados se ordenan por
         * relevancia y fecha de registro.
         * 
         * @param query    Término de búsqueda para filtrar usuarios
         * @param pageable Información de paginación (número de página, tamaño,
         *                 ordenamiento)
         * @return ResponseEntity con la página de usuarios encontrados
         */
        @GetMapping("/search")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Buscar usuarios", description = "Busca usuarios públicos en la plataforma usando un término de búsqueda")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Usuarios encontrados exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación")
        })
        public ResponseEntity<Page<UserResponse>> searchUsers(
                        @Parameter(description = "Término de búsqueda", example = "john") @RequestParam String query,
                        @Parameter(description = "Parámetros de paginación", example = "page=0&size=10&sort=firstName,asc") @PageableDefault(size = 10) Pageable pageable) {
                log.debug("Buscando usuarios con query: '{}', página: {}", query, pageable.getPageNumber());

                Page<UserResponse> users = userService.searchPublicUsers(query, pageable);

                log.debug("Usuarios encontrados: {} con query: '{}'", users.getContent().size(), query);

                return ResponseEntity.ok(users);
        }

        /**
         * Obtiene todos los usuarios de la plataforma (solo administradores).
         * 
         * Este endpoint permite a los administradores obtener una lista
         * completa de todos los usuarios registrados en la plataforma
         * con soporte para paginación.
         * 
         * @param pageable Información de paginación (número de página, tamaño,
         *                 ordenamiento)
         * @return ResponseEntity con la página de usuarios
         */
        @GetMapping
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(summary = "Obtener todos los usuarios", description = "Recupera una lista de todos los usuarios registrados (solo administradores)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Usuarios obtenidos exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol ADMIN")
        })
        public ResponseEntity<Page<UserResponse>> getAllUsers(
                        @Parameter(description = "Parámetros de paginación", example = "page=0&size=10&sort=firstName,asc") @PageableDefault(size = 10) Pageable pageable) {
                log.debug("Obteniendo todos los usuarios, página: {}", pageable.getPageNumber());

                Page<UserResponse> users = userService.findAllUsersAsDto(pageable);

                log.debug("Usuarios obtenidos: {} en total", users.getContent().size());

                return ResponseEntity.ok(users);
        }

        /**
         * Cuenta los administradores activos en el sistema.
         *
         * @return número de administradores activos
         */
        @GetMapping("/admins/count")
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(summary = "Contar administradores activos", description = "Devuelve el número de administradores activos en el sistema")
        public ResponseEntity<Long> countActiveAdmins() {
                log.debug("Solicitando conteo de administradores activos");

                long count = userService.countActiveAdmins();

                return ResponseEntity.ok(count);
        }

        /**
         * Actualiza la información del usuario autenticado actual.
         * 
         * Este endpoint permite al usuario autenticado actualizar su información
         * personal como nombre, apellido, email, teléfono y otros datos básicos.
         * 
         * @param request Datos actualizados del usuario
         * @return ResponseEntity con la información del usuario actualizada
         */
        @PutMapping("/me")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Actualizar usuario actual", description = "Actualiza la información del usuario autenticado")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Datos del usuario inválidos"),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación")
        })
        public ResponseEntity<UserResponse> updateCurrentUser(@Valid @RequestBody UserUpdateRequest request) {
                log.debug("Actualizando usuario actual");

                User currentUser = userService.getCurrentUser();

                UserResponse response = userService.updateUser(currentUser.getId(), request);

                log.debug("Usuario actual actualizado exitosamente");

                return ResponseEntity.ok(response);
        }

        /**
         * Actualiza la información de un usuario específico.
         * 
         * Este endpoint permite actualizar la información de un usuario específico.
         * Los usuarios solo pueden actualizar sus propios datos, mientras que
         * los administradores pueden actualizar cualquier usuario.
         * 
         * @param id      ID del usuario a actualizar
         * @param request Datos actualizados del usuario
         * @return ResponseEntity con la información del usuario actualizada
         */
        @PutMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
        @Operation(summary = "Actualizar usuario", description = "Actualiza la información de un usuario específico")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Datos del usuario inválidos"),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "403", description = "Prohibido - Solo puede actualizar sus propios datos o ser ADMIN"),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        })
        public ResponseEntity<UserResponse> updateUser(
                        @Parameter(description = "ID del usuario", example = "1") @PathVariable Long id,
                        @Valid @RequestBody UserUpdateRequest request) {
                log.debug("Actualizando usuario con ID: {}", id);

                User currentUser = userService.getCurrentUser();

                if (userService.isCurrentUserAdmin() && !currentUser.getId().equals(id)) {
                        AdminUserUpdateRequest adminRequest = new AdminUserUpdateRequest(
                                        null, // username (no se modifica desde este endpoint)
                                        request.firstName(), request.lastName(), request.email(),
                                        request.phone(), request.profilePicture(), request.address(),
                                        request.biography(), request.privacyType(), null);

                        UserResponse response = userService.updateUserAsAdmin(id, adminRequest);

                        log.debug("Usuario actualizado como administrador: {}", id);

                        return ResponseEntity.ok(response);
                } else {
                        UserResponse response = userService.updateUser(id, request);

                        log.debug("Usuario actualizado exitosamente: {}", id);

                        return ResponseEntity.ok(response);
                }
        }

        /**
         * Actualiza la información de un usuario específico como administrador.
         * 
         * Este endpoint permite a los administradores actualizar cualquier campo
         * de un usuario incluyendo su rol y tipo de privacidad.
         * 
         * @param id      ID del usuario a actualizar
         * @param request Datos actualizados del usuario
         * @return ResponseEntity con la información del usuario actualizada
         */
        @PutMapping("/{id}/admin")
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(summary = "Actualizar usuario como administrador", description = "Actualiza la información de un usuario específico con privilegios de administrador")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Datos del usuario inválidos"),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol ADMIN"),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        })
        public ResponseEntity<Map<String, Object>> updateUserAsAdminEndpoint(
                        @Parameter(description = "ID del usuario", example = "1") @PathVariable Long id,
                        @Valid @RequestBody AdminUserUpdateRequest request) {
                log.info("Actualizando usuario como administrador con ID: {}", id);

                log.info("AdminUserUpdateRequest recibido en controlador: {}", request);

                User currentUser = userService.getCurrentUser();
                boolean isUpdatingSelf = currentUser.getId().equals(id);
                boolean isUsernameChanged = request.username() != null && !request.username().isBlank();

                UserResponse response = userService.updateUserAsAdmin(id, request);

                log.debug("Usuario actualizado como administrador exitosamente: {}", id);

                // Si el admin actualizó su propio username, generar nuevo token
                if (isUpdatingSelf && isUsernameChanged) {
                        String newToken = userService.generateTokenForUser(response);
                        log.info("Generado nuevo token JWT para admin tras cambio de username");

                        Map<String, Object> responseWithToken = Map.of(
                                        "user", response,
                                        "token", newToken,
                                        "message",
                                        "Usuario actualizado. Se ha generado un nuevo token debido al cambio de username");
                        return ResponseEntity.ok(responseWithToken);
                }

                // Si no cambió el username o no es el mismo usuario, respuesta normal
                Map<String, Object> normalResponse = Map.of(
                                "user", response,
                                "message", "Usuario actualizado exitosamente");
                return ResponseEntity.ok(normalResponse);
        }

        /**
         * Cambia la configuración de privacidad del perfil del usuario actual.
         * 
         * Este endpoint permite al usuario autenticado cambiar la configuración
         * de privacidad de su perfil (PUBLIC, PRIVATE, FRIENDS_ONLY).
         * 
         * @param privacyType Nuevo tipo de privacidad para el perfil
         * @return ResponseEntity con mensaje de confirmación
         */
        @PutMapping("/me/privacy")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Cambiar privacidad del perfil", description = "Modifica la configuración de privacidad del perfil del usuario actual")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Privacidad del perfil actualizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                                        {
                                          "message": "Profile privacy updated"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación")
        })
        public ResponseEntity<Map<String, String>> changeProfilePrivacy(
                        @Parameter(description = "Nuevo tipo de privacidad", example = "PUBLIC") @RequestParam PrivacyType privacyType) {
                log.debug("Cambiando privacidad del perfil a: {}", privacyType);

                userService.changeProfileType(privacyType);

                log.debug("Privacidad del perfil actualizada exitosamente");

                return ResponseEntity.ok(Map.of("message", "Privacidad del perfil actualizada"));
        }

        /**
         * Activa un usuario desactivado (solo administradores).
         * 
         * Este endpoint permite a los administradores reactivar un usuario
         * que ha sido previamente desactivado.
         * 
         * @param id ID del usuario a activar
         * @return ResponseEntity con mensaje de confirmación
         */
        @PutMapping("/{id}/activate")
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(summary = "Activar usuario", description = "Reactiva un usuario desactivado (solo administradores)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Usuario activado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                                        {
                                          "message": "Usuario activado exitosamente"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol ADMIN"),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        })
        public ResponseEntity<Map<String, String>> activateUser(
                        @Parameter(description = "ID del usuario", example = "1") @PathVariable Long id) {
                log.debug("Activando usuario con ID: {}", id);

                userService.activateUser(id);

                log.debug("Usuario activado exitosamente: {}", id);

                return ResponseEntity.ok(Map.of("message", "Usuario activado exitosamente"));
        }

        /**
         * Desactiva un usuario activo (solo administradores).
         * 
         * Este endpoint permite a los administradores desactivar un usuario
         * activo, impidiendo su acceso a la plataforma.
         * 
         * @param id ID del usuario a desactivar
         * @return ResponseEntity con mensaje de confirmación
         */
        @PutMapping("/{id}/deactivate")
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(summary = "Desactivar usuario", description = "Desactiva un usuario activo (solo administradores)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Usuario desactivado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                                        {
                                          "message": "Usuario desactivado exitosamente"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol ADMIN"),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        })
        public ResponseEntity<Map<String, String>> deactivateUser(
                        @Parameter(description = "ID del usuario", example = "1") @PathVariable Long id) {
                log.debug("Desactivando usuario con ID: {}", id);

                userService.deactivateUser(id);

                log.debug("Usuario desactivado exitosamente: {}", id);

                return ResponseEntity.ok(Map.of("message", "Usuario desactivado exitosamente"));
        }

        /**
         * Elimina la cuenta del usuario autenticado actual.
         * 
         * Este endpoint permite al usuario autenticado eliminar su propia
         * cuenta de forma permanente. La eliminación no se puede deshacer.
         * 
         * @return ResponseEntity con mensaje de confirmación
         */
        @DeleteMapping("/me")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Eliminar cuenta actual", description = "Elimina permanentemente la cuenta del usuario autenticado")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                                        {
                                          "message": "Usuario eliminado extosamente"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación")
        })
        public ResponseEntity<Map<String, String>> deleteCurrentUser() {
                log.debug("Eliminando cuenta del usuario actual");

                User currentUser = userService.getCurrentUser();

                userService.deleteUser(currentUser.getId());

                log.debug("Usuario actual eliminado exitosamente");

                return ResponseEntity.ok(Map.of("message", "Usuario eliminado extosamente"));
        }

        /**
         * Elimina un usuario específico (solo administradores).
         * 
         * Este endpoint permite a los administradores eliminar cualquier
         * usuario de la plataforma de forma permanente.
         * 
         * @param id ID del usuario a eliminar
         * @return ResponseEntity con mensaje de confirmación
         */
        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(summary = "Eliminar usuario", description = "Elimina permanentemente un usuario específico (solo administradores)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                                        {
                                          "message": "Usuario eliminado extosamente"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol ADMIN"),
                        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        })
        public ResponseEntity<Map<String, String>> deleteUser(
                        @Parameter(description = "ID del usuario", example = "1") @PathVariable Long id) {
                log.debug("Eliminando usuario con ID: {}", id);

                userService.deleteUser(id);

                log.debug("Usuario eliminado exitosamente: {}", id);

                return ResponseEntity.ok(Map.of("message", "Usuario eliminado extosamente"));
        }

        /**
         * Crea un nuevo usuario administrador (solo administradores).
         * 
         * Este endpoint permite a los administradores crear nuevos usuarios
         * con privilegios de administrador en la plataforma.
         * 
         * @param request Datos del nuevo usuario administrador
         * @return ResponseEntity con la información del usuario creado
         */
        @PostMapping("/admin")
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(summary = "Crear usuario administrador", description = "Crea un nuevo usuario con privilegios de administrador")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Usuario administrador creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Datos del usuario inválidos"),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol ADMIN")
        })
        public ResponseEntity<UserResponse> createAdminUser(@Valid @RequestBody UserRegisterRequest request) {
                log.debug("Creando nuevo usuario administrador");

                UserResponse response = userService.createAdminUser(request);

                log.debug("Usuario administrador creado exitosamente");

                return ResponseEntity.ok(response);
        }

        /**
         * Actualiza el perfil extendido del usuario autenticado actual.
         * 
         * Este endpoint permite al usuario autenticado actualizar su perfil
         * extendido con información adicional como biografía, ubicación,
         * redes sociales y otros datos personales.
         * 
         * @param request Datos actualizados del perfil extendido
         * @return ResponseEntity con el perfil actualizado
         */
        @PutMapping("/me/profile")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Actualizar perfil extendido", description = "Actualiza el perfil extendido del usuario autenticado")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Datos del perfil inválidos"),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación")
        })
        public ResponseEntity<UserProfileResponse> updateUserProfile(@Valid @RequestBody UserProfileRequest request) {
                log.debug("Actualizando perfil extendido del usuario actual");

                User currentUser = userService.getCurrentUser();

                UserProfileResponse response = userService.updateUserProfileExtended(currentUser.getId(), request);

                log.debug("Perfil extendido actualizado exitosamente");

                return ResponseEntity.ok(response);
        }

        /**
         * Obtiene el perfil extendido del usuario autenticado actual.
         * 
         * Este endpoint permite al usuario autenticado obtener su perfil
         * extendido. Si no existe un perfil, se crea uno automáticamente
         * con valores vacíos.
         * 
         * @return ResponseEntity con el perfil extendido del usuario
         */
        @GetMapping("/me/profile")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Obtener perfil extendido", description = "Recupera el perfil extendido del usuario autenticado")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Perfil obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación")
        })
        public ResponseEntity<UserProfileResponse> getUserProfile() {
                log.debug("Obteniendo perfil extendido del usuario actual");

                User currentUser = userService.getCurrentUser();
                UserProfileResponse response = userService.getUserProfile(currentUser.getId());
                if (response == null) {
                        UserProfileRequest emptyProfile = new UserProfileRequest(
                                        null, null, null, null, null, null, null, null);

                        response = userService.updateUserProfileExtended(currentUser.getId(), emptyProfile);

                        log.debug("Perfil extendido creado automáticamente");
                }

                log.debug("Perfil extendido obtenido exitosamente");

                return ResponseEntity.ok(response);
        }

}
