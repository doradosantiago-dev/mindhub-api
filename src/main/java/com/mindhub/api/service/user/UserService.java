package com.mindhub.api.service.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mindhub.api.dto.auth.UserLoginRequest;
import com.mindhub.api.dto.auth.UserRegisterRequest;
import com.mindhub.api.dto.auth.UserResponse;
import com.mindhub.api.dto.user.AdminUserUpdateRequest;
import com.mindhub.api.dto.user.UserProfileRequest;
import com.mindhub.api.dto.user.UserProfileResponse;
import com.mindhub.api.dto.user.UserUpdateRequest;
import com.mindhub.api.model.enums.PrivacyType;
import com.mindhub.api.model.user.User;
import com.mindhub.api.service.base.GenericService;

/**
 * Servicio para gestión de usuarios del sistema.
 * 
 * <p>
 * Proporciona funcionalidades para registro, autenticación, gestión
 * de perfiles y administración de usuarios, incluyendo validaciones
 * de permisos y seguridad.
 * </p>
 * 
 * @author MindHub Development Team
 * @since Spring Boot 3.5.5
 */
public interface UserService extends GenericService<User, Long> {

    /**
     * Registra un nuevo usuario.
     * 
     * @param request Datos del usuario a registrar
     * @return Usuario registrado
     */
    UserResponse registerUser(UserRegisterRequest request);

    /**
     * Autentica un usuario y genera un token JWT.
     * 
     * @param request Datos de autenticación
     * @return Token JWT generado
     */
    String authenticateUser(UserLoginRequest request);

    /**
     * Obtiene un usuario por su ID como respuesta DTO.
     * 
     * @param id ID del usuario
     * @return Usuario encontrado
     */
    UserResponse findByIdAsResponse(Long id);

    /**
     * Busca un usuario por su nombre de usuario.
     * 
     * @param username Nombre de usuario
     * @return Usuario encontrado
     */
    UserResponse findByUsername(String username);

    /**
     * Obtiene todos los usuarios paginados como DTOs.
     * 
     * @param pageable Configuración de paginación
     * @return Página de usuarios
     */
    Page<UserResponse> findAllUsersAsDto(Pageable pageable);

    /**
     * Busca usuarios públicos por nombre o apellido.
     * 
     * @param search   Término de búsqueda
     * @param pageable Configuración de paginación
     * @return Página de usuarios encontrados
     */
    Page<UserResponse> searchPublicUsers(String search, Pageable pageable);

    /**
     * Busca usuarios por nombre o apellido sin restricción de privacidad (solo
     * admin).
     * 
     * @param search   Término de búsqueda
     * @param pageable Configuración de paginación
     * @return Página de usuarios encontrados
     */
    Page<UserResponse> searchAllUsers(String search, Pageable pageable);

    /**
     * Actualiza un usuario.
     * 
     * @param id      ID del usuario
     * @param request Datos de actualización
     * @return Usuario actualizado
     */
    UserResponse updateUser(Long id, UserUpdateRequest request);

    /**
     * Actualiza un usuario como administrador.
     * 
     * @param id      ID del usuario
     * @param request Datos de actualización
     * @return Usuario actualizado
     */
    UserResponse updateUserAsAdmin(Long id, AdminUserUpdateRequest request);

    /**
     * Genera un token JWT para un usuario a partir de su UserResponse.
     * 
     * @param userResponse Datos del usuario
     * @return Token JWT generado
     */
    String generateTokenForUser(UserResponse userResponse);

    /**
     * Activa un usuario.
     * 
     * @param id ID del usuario
     */
    void activateUser(Long id);

    /**
     * Desactiva un usuario.
     * 
     * @param id ID del usuario
     */
    void deactivateUser(Long id);

    /**
     * Cambia el tipo de privacidad del perfil del usuario actual.
     * 
     * @param privacyType Nuevo tipo de privacidad
     */
    void changeProfileType(PrivacyType privacyType);

    /**
     * Obtiene el usuario actual autenticado.
     * 
     * @return Usuario actual
     */
    User getCurrentUser();

    /**
     * Verifica si el usuario actual es administrador.
     * 
     * @return true si es administrador, false en caso contrario
     */
    boolean isCurrentUserAdmin();

    /**
     * Busca un administrador activo.
     * 
     * @return Administrador activo o null si no existe
     */
    User findActiveAdmin();

    /**
     * Elimina un usuario.
     * 
     * @param id ID del usuario a eliminar
     */
    void deleteUser(Long id);

    /**
     * Actualiza la fecha de última actividad de un usuario.
     * 
     * @param userId ID del usuario
     */
    void updateLastActivity(Long userId);

    /**
     * Crea un usuario administrador.
     * 
     * @param request Datos del usuario administrador
     * @return Usuario administrador creado
     */
    UserResponse createAdminUser(UserRegisterRequest request);

    /**
     * Cuenta el número total de usuarios.
     * 
     * @return Número total de usuarios
     */
    long countTotalUsers();

    /**
     * Cuenta el número de usuarios activos.
     * 
     * @return Número de usuarios activos
     */
    long countActiveUsers();

    /**
     * Cuenta el número de usuarios inactivos.
     * 
     * @return Número de usuarios inactivos
     */
    long countInactiveUsers();

    /**
     * Cuenta el número de administradores activos en el sistema.
     *
     * @return número de administradores activos
     */
    long countActiveAdmins();

    /**
     * Actualiza el perfil extendido de un usuario.
     * 
     * @param userId  ID del usuario
     * @param request Datos del perfil
     * @return Perfil actualizado
     */
    UserProfileResponse updateUserProfileExtended(Long userId, UserProfileRequest request);

    /**
     * Obtiene el perfil de un usuario.
     * 
     * @param userId ID del usuario
     * @return Perfil del usuario
     */
    UserProfileResponse getUserProfile(Long userId);

    /**
     * Obtiene el usuario actual con su perfil.
     * 
     * @return Usuario actual con perfil
     */
    UserResponse getCurrentUserWithProfile();
}
