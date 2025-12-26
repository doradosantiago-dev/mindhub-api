package com.mindhub.api.service.user;

import java.time.LocalDate;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mindhub.api.dto.auth.UserLoginRequest;
import com.mindhub.api.dto.auth.UserRegisterRequest;
import com.mindhub.api.dto.auth.UserResponse;
import com.mindhub.api.dto.user.AdminUserUpdateRequest;
import com.mindhub.api.dto.user.UserProfileRequest;
import com.mindhub.api.dto.user.UserProfileResponse;
import com.mindhub.api.dto.user.UserUpdateRequest;
import com.mindhub.api.exception.EmailAlreadyExistsException;
import com.mindhub.api.exception.UserNotFoundException;
import com.mindhub.api.exception.UsernameAlreadyExistsException;
import com.mindhub.api.mapper.user.UserMapper;
import com.mindhub.api.model.enums.ActionType;
import com.mindhub.api.model.enums.NotificationType;
import com.mindhub.api.model.enums.PrivacyType;
import com.mindhub.api.model.role.Role;
import com.mindhub.api.model.user.User;
import com.mindhub.api.model.userProfile.UserProfile;
import com.mindhub.api.repository.user.UserRepository;
import com.mindhub.api.repository.userProfile.UserProfileRepository;
import com.mindhub.api.security.JwtService;
import com.mindhub.api.service.admin.AdminActionService;
import com.mindhub.api.service.base.GenericServiceImpl;
import com.mindhub.api.service.notification.NotificationService;
import com.mindhub.api.service.role.RoleService;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio de usuarios.
 * 
 * Gestiona las operaciones CRUD de usuarios, incluyendo autenticación,
 * registro, gestión de perfiles y administración de usuarios.
 * 
 */

@Slf4j
@Service
@Transactional
public class UserServiceImpl extends GenericServiceImpl<User, Long> implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final NotificationService notificationService;
    private final AdminActionService adminActionService;
    private final RoleService roleService;
    private final UserProfileRepository userProfileRepository;

    public UserServiceImpl(UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            @Lazy NotificationService notificationService,
            @Lazy AdminActionService adminActionService,
            @Lazy RoleService roleService,
            UserProfileRepository userProfileRepository) {
        super(userRepository);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.notificationService = notificationService;
        this.adminActionService = adminActionService;
        this.roleService = roleService;
        this.userProfileRepository = userProfileRepository;
    }

    /**
     * Obtiene el ID de una entidad.
     * 
     * @param entity Entidad
     * @return ID de la entidad
     */
    @Override
    protected Long getEntityId(User entity) {
        return entity.getId();
    }

    /**
     * Crea una excepción de notificación cuando no se encuentra una entidad.
     * 
     * @param message Mensaje de la excepción
     * @return Excepción de notificación
     */
    @Override
    protected RuntimeException createNotFoundException(String message) {
        return new UserNotFoundException(message);
    }

    /**
     * Obtiene el nombre de la entidad.
     * 
     * @return Nombre de la entidad
     */
    @Override
    public String getEntityName() {
        return "User";
    }

    /**
     * Verifica si un usuario es administrador.
     * 
     * @param user Usuario a verificar
     * @return true si es administrador, false en caso contrario
     */
    private boolean isAdmin(User user) {
        return user != null && user.getRole() != null && user.getRole().isAdmin();
    }

    /**
     * Registra un nuevo usuario.
     * 
     * @param request Datos del usuario a registrar
     * @return Usuario registrado
     */
    @Override
    public UserResponse registerUser(UserRegisterRequest request) {
        log.info("Iniciando registro de usuario con nombre de usuario: {}", request.username());

        validateUniqueFields(request);

        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.password()));

        Role defaultRole = roleService.getDefaultRole();

        user.setRole(defaultRole);

        if (user.getRole() != null && user.getRole().isAdmin()) {
            log.warn("Intento de crear un administrador mediante registro público");

            throw new SecurityException("No puedes crear un administrador mediante el registro público");
        }

        user.setPrivacyType(PrivacyType.PUBLIC);

        User savedUser = save(user);

        log.info("Usuario registrado exitosamente con ID: {}", savedUser.getId());

        if (savedUser.getProfile() == null) {
            log.debug("Creando perfil vacío para el usuario con ID: {}", savedUser.getId());

            UserProfile emptyProfile = UserProfile.builder()
                    .user(savedUser)
                    .build();

            userProfileRepository.save(emptyProfile);

            savedUser.setProfile(emptyProfile);
        }

        // Notificar a todos los administradores sobre el nuevo usuario registrado
        notifyAdminsAboutNewUser(savedUser);

        return userMapper.toResponse(savedUser);
    }

    /**
     * Genera un token JWT para un usuario a partir de su UserResponse.
     * 
     * @param userResponse Datos del usuario
     * @return Token JWT generado
     */
    @Override
    public String generateTokenForUser(UserResponse userResponse) {
        log.debug("Generando token para usuario: {}", userResponse.username());

        User user = userRepository.findByUsername(userResponse.username())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String token = jwtService.generateToken(user);

        log.debug("Token generado exitosamente para: {}", userResponse.username());

        return token;
    }

    /**
     * Autentica un usuario y genera un token JWT.
     * 
     * @param request Datos de autenticación
     * @return Token JWT generado
     */
    @Override
    public String authenticateUser(UserLoginRequest request) {
        log.info("Intentando autenticar usuario: {}", request.username());

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.isEnabled()) {
            log.warn("Usuario deshabilitado: {}", request.username());

            throw new IllegalStateException("Usuario deshabilitado. Contacta con el administrador");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            log.error("Contraseña incorrecta para el usuario: {}", request.username());

            throw new IllegalArgumentException("Contraseña incorrecta");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        updateLastActivity(user.getId());

        log.info("Autenticación exitosa para el usuario: {}", request.username());

        String jwt = jwtService.generateToken(user);

        log.debug("Token JWT generado para el usuario: {}", request.username());

        return jwt;
    }

    /**
     * Actualiza la fecha de última actividad de un usuario.
     * 
     * @param userId ID del usuario
     */
    @Override
    public void updateLastActivity(Long userId) {
        log.debug("Actualizando última actividad para el usuario con ID: {}", userId);

        User user = findByIdOrThrow(userId);

        user.setLastActivityDate(LocalDate.now());

        save(user);

        log.info("Última actividad actualizada correctamente para el usuario con ID: {}", userId);
    }

    /**
     * Obtiene un usuario por su ID como respuesta DTO.
     * 
     * @param id ID del usuario
     * @return Usuario encontrado
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponse findByIdAsResponse(Long id) {
        log.debug("Buscando usuario por ID: {}", id);

        User user = findByIdOrThrow(id);

        log.info("Usuario encontrado con ID: {}", id);

        return userMapper.toResponse(user);
    }

    /**
     * Busca un usuario por su nombre de usuario.
     * 
     * @param username Nombre de usuario
     * @return Usuario encontrado
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponse findByUsername(String username) {
        log.debug("Buscando usuario por nombre de usuario: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        log.info("Usuario encontrado con nombre de usuario: {}", username);

        return userMapper.toResponse(user);
    }

    /**
     * Busca un usuario por su nombre de usuario como entidad.
     * 
     * @param username Nombre de usuario
     * @return Usuario encontrado como entidad
     */
    @Transactional(readOnly = true)
    public User findByUsernameAsEntity(String username) {
        log.debug("Buscando entidad de usuario por nombre de usuario: {}", username);

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
    }

    /**
     * Obtiene todos los usuarios paginados como DTOs.
     * 
     * @param pageable Configuración de paginación
     * @return Página de usuarios
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> findAllUsersAsDto(Pageable pageable) {
        log.debug("Obteniendo usuarios paginados. Página: {}, Tamaño: {}", pageable.getPageNumber(),
                pageable.getPageSize());

        return userRepository.findAll(pageable)
                .map(userMapper::toResponse);
    }

    /**
     * Busca usuarios públicos por nombre o apellido.
     * 
     * @param search   Término de búsqueda
     * @param pageable Configuración de paginación
     * @return Página de usuarios encontrados
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> searchPublicUsers(String search, Pageable pageable) {
        log.debug("Buscando usuarios públicos con término: {}", search);

        Page<User> users = userRepository.findPublicUsersByFirstNameOrLastName(search, pageable);

        log.info("Se encontraron {} usuarios públicos con el término: {}", users.getContent().size(), search);

        return users.map(userMapper::toResponse);
    }

    /**
     * Busca usuarios por nombre o apellido sin restricción de privacidad (solo
     * admin).
     * 
     * @param search   Término de búsqueda
     * @param pageable Configuración de paginación
     * @return Página de usuarios encontrados
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> searchAllUsers(String search, Pageable pageable) {
        log.debug("[ADMIN] Buscando usuarios con término: {}", search);

        Page<User> users = userRepository.findUsersByFirstNameOrLastName(search, pageable);

        log.info("[ADMIN] Se encontraron {} usuarios con el término: {}", users.getContent().size(), search);

        return users.map(userMapper::toResponse);
    }

    /**
     * Actualiza un usuario.
     * 
     * @param id      ID del usuario
     * @param request Datos de actualización
     * @return Usuario actualizado
     */
    @Override
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        log.debug("Iniciando actualización de usuario con ID: {}", id);

        User user = findByIdOrThrow(id);

        User currentUser = getCurrentUser();

        validateUpdatePermissions(currentUser, id);
        validateEmailFormat(request.email());
        validateEmailUniqueness(user, request.email());

        updateUserFields(user, request);

        User savedUser = save(user);

        log.info("Usuario con ID {} actualizado correctamente por el usuario actual con ID {}", id,
                currentUser.getId());

        return userMapper.toResponse(savedUser);
    }

    /**
     * Actualiza un usuario como administrador.
     * 
     * @param id      ID del usuario
     * @param request Datos de actualización
     * @return Usuario actualizado
     */
    @Override
    public UserResponse updateUserAsAdmin(Long id, AdminUserUpdateRequest request) {
        User admin = getCurrentUser();

        log.debug("El administrador con ID {} está actualizando al usuario con ID {}", admin.getId(), id);
        log.debug("AdminUserUpdateRequest recibido: {}", request);

        validateAdminPermissions(admin);

        User user = findByIdOrThrow(id);

        log.info("Usuario antes de actualizar: ID={}, Role={}, PrivacyType={}",
                user.getId(), user.getRole().getName(), user.getPrivacyType());

        validateEmailFormat(request.email());
        validateEmailUniqueness(user, request.email());

        updateUserFieldsAsAdmin(user, request);

        User savedUser = save(user);

        log.info("Usuario después de actualizar: ID={}, Role={}, PrivacyType={}",
                savedUser.getId(), savedUser.getRole().getName(), savedUser.getPrivacyType());

        log.info("Usuario con ID {} actualizado por el administrador con ID {}", id, admin.getId());

        logAdminAction(admin, user, "Usuario actualizado por el administrador");

        // Enviar notificación al usuario
        notificationService.createNotification(user,
                "Datos actualizados",
                "Tus datos han sido actualizados por el administrador",
                NotificationType.ADMIN_ACTION);

        return userMapper.toResponse(savedUser);
    }

    /**
     * Valida los permisos para actualizar un usuario.
     * 
     * @param currentUser  Usuario actual
     * @param targetUserId ID del usuario objetivo
     */
    private void validateUpdatePermissions(User currentUser, Long targetUserId) {
        if (!currentUser.getId().equals(targetUserId) && !isAdmin(currentUser)) {
            log.warn("El usuario con ID {} intentó actualizar al usuario con ID {} sin permisos", currentUser.getId(),
                    targetUserId);

            throw new SecurityException("No tienes permiso para actualizar este usuario");
        }
    }

    /**
     * Valida que un usuario tenga permisos de administrador.
     * 
     * @param user Usuario a validar
     */
    private void validateAdminPermissions(User user) {
        log.warn("El usuario con ID {} intentó realizar una acción de administrador sin permisos", user.getId());

        if (!isAdmin(user)) {
            throw new SecurityException("Solo los administradores pueden actualizar usuarios");
        }
    }

    /**
     * Valida el formato del email.
     * 
     * @param email Email a validar
     */
    private void validateEmailFormat(String email) {
        if (email != null && !email.isEmpty() && !isValidEmailFormat(email)) {
            log.error("Formato de email inválido: {}", email);

            throw new IllegalArgumentException("Formato de correo electrónico no válido");
        }
    }

    /**
     * Verifica si el formato del email es válido.
     * 
     * @param email Email a verificar
     * @return true si el formato es válido, false en caso contrario
     */
    private boolean isValidEmailFormat(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    /**
     * Valida que el email sea único.
     * 
     * @param user     Usuario actual
     * @param newEmail Nuevo email a validar
     */
    private void validateEmailUniqueness(User user, String newEmail) {
        if (newEmail != null && !user.getEmail().equals(newEmail) &&
                userRepository.existsByEmail(newEmail)) {
            log.error("El email {} ya está en uso", newEmail);

            throw new EmailAlreadyExistsException("El correo electrónico ya existe");
        }
    }

    /**
     * Actualiza los campos de un usuario.
     * 
     * @param user    Usuario a actualizar
     * @param request Datos de actualización
     */
    private void updateUserFields(User user, UserUpdateRequest request) {
        log.debug("Actualizando campos del usuario con ID: {}", user.getId());

        if (request.firstName() != null) {
            user.setFirstName(request.firstName().trim());

            log.debug("Nombre actualizado para el usuario {}", user.getId());
        }
        if (request.lastName() != null) {
            user.setLastName(request.lastName().trim());

            log.debug("Apellido actualizado para el usuario {}", user.getId());
        }
        if (request.email() != null) {
            user.setEmail(request.email().trim());

            log.debug("Email actualizado para el usuario {}", user.getId());
        }
        if (request.phone() != null) {
            user.setPhone(request.phone().trim());

            log.debug("Teléfono actualizado para el usuario {}", user.getId());
        }
        if (request.profilePicture() != null) {
            user.setProfilePicture(request.profilePicture().trim());

            log.debug("Foto de perfil actualizada para el usuario {}", user.getId());
        }
        if (request.address() != null) {
            user.setAddress(request.address().trim());

            log.debug("Dirección actualizada para el usuario {}", user.getId());
        }
        if (request.biography() != null) {
            user.setBiography(request.biography().trim());

            log.debug("Biografía actualizada para el usuario {}", user.getId());
        }
        if (request.privacyType() != null) {
            user.setPrivacyType(request.privacyType());

            log.debug("Privacidad actualizada para el usuario {}", user.getId());
        }
    }

    /**
     * Actualiza los campos de un usuario como administrador.
     * 
     * @param user    Usuario a actualizar
     * @param request Datos de actualización
     */
    private void updateUserFieldsAsAdmin(User user, AdminUserUpdateRequest request) {
        log.debug("Actualizando campos (modo administrador) del usuario con ID: {}", user.getId());
        log.info("Request roleName: '{}'", request.roleName());
        log.info("Request privacyType: '{}'", request.privacyType());

        if (request.username() != null && !request.username().isBlank()) {
            String newUsername = request.username().trim();
            if (!newUsername.equals(user.getUsername())) {
                // Verificar que el nuevo username no esté en uso por otro usuario
                if (userRepository.findByUsername(newUsername).isPresent()) {
                    throw new IllegalArgumentException("El nombre de usuario '" + newUsername + "' ya está en uso");
                }
                user.setUsername(newUsername);
                log.info("Username actualizado por admin para el usuario {}: {} -> {}", user.getId(),
                        user.getUsername(), newUsername);
            }
        }
        if (request.firstName() != null) {
            user.setFirstName(request.firstName().trim());

            log.debug("Nombre actualizado por admin para el usuario {}", user.getId());
        }
        if (request.lastName() != null) {
            user.setLastName(request.lastName().trim());

            log.debug("Apellido actualizado por admin para el usuario {}", user.getId());
        }
        if (request.email() != null) {
            user.setEmail(request.email().trim());

            log.debug("Email actualizado por admin para el usuario {}", user.getId());
        }
        if (request.phone() != null) {
            user.setPhone(request.phone().trim());

            log.debug("Teléfono actualizado por admin para el usuario {}", user.getId());
        }
        if (request.profilePicture() != null) {
            user.setProfilePicture(request.profilePicture().trim());

            log.debug("Foto de perfil actualizada por admin para el usuario {}", user.getId());
        }
        if (request.address() != null) {
            user.setAddress(request.address().trim());

            log.debug("Dirección actualizada por admin para el usuario {}", user.getId());
        }
        if (request.biography() != null) {
            user.setBiography(request.biography().trim());

            log.debug("Biografía actualizada por admin para el usuario {}", user.getId());
        }

        // Actualizar privacidad primero si se proporciona
        if (request.privacyType() != null) {
            user.setPrivacyType(request.privacyType());

            log.debug("Privacidad actualizada por admin para el usuario {}", user.getId());
        }

        // Actualizar rol si se proporciona (esto puede sobrescribir la privacidad si es
        // ADMIN)
        if (request.roleName() != null && !request.roleName().trim().isEmpty()) {
            Role newRole = roleService.findByNameOrThrow(request.roleName().trim());
            Role oldRole = user.getRole();

            user.setRole(newRole);

            log.info("Rol actualizado por admin para el usuario {} de {} a {}",
                    user.getId(), oldRole.getName(), newRole.getName());

            // Si el nuevo rol es ADMIN, forzar privacidad a PRIVATE (sobrescribe cualquier
            // valor anterior)
            if ("ADMIN".equals(newRole.getName())) {
                user.setPrivacyType(PrivacyType.PRIVATE);
                log.info("Privacidad establecida automáticamente a PRIVATE para el administrador {}", user.getId());
            }
        }
    }

    /**
     * Registra una acción de administrador.
     * 
     * @param admin  Administrador que realiza la acción
     * @param user   Usuario afectado
     * @param action Acción realizada
     */
    private void logAdminAction(User admin, User user, String action) {
        log.info("Acción de administrador: {} sobre el usuario con ID {}", action, user.getId());

        adminActionService.logAction(admin, ActionType.UPDATE_USER,
                action, "Usuario " + user.getUsername() + " actualizado por el administrador",
                user.getId(), "users", user);

    }

    /**
     * Activa un usuario.
     * 
     * @param id ID del usuario
     */
    @Override
    public void activateUser(Long id) {
        log.debug("Intentando activar usuario con ID: {}", id);

        User admin = getCurrentUser();

        if (!isAdmin(admin)) {
            log.warn("El usuario {} intentó activar al usuario {} sin permisos", admin.getId(), id);

            throw new SecurityException("Solo los administradores pueden activar usuarios");
        }

        User user = findByIdOrThrow(id);
        if (user.isEnabled()) {
            log.warn("El usuario {} ya estaba activado", id);

            throw new IllegalStateException("El usuario ya está activado");
        }

        user.setActive(true);

        save(user);

        log.info("Usuario con ID {} activado por el administrador {}", id, admin.getId());

        adminActionService.logAction(
                admin,
                ActionType.ACTIVATE_USER,
                "Usuario activado por el administrador",
                "Usuario " + user.getUsername() + " activado por el administrador",
                user.getId(),
                "users",
                user);

        notificationService.createNotification(user,
                "Cuenta activada",
                "Tu cuenta ha sido activada por el administrador",
                NotificationType.ADMIN_ACTION);
    }

    /**
     * Desactiva un usuario.
     * 
     * @param id ID del usuario
     */
    @Override
    public void deactivateUser(Long id) {
        log.debug("Intentando desactivar usuario con ID: {}", id);

        User admin = getCurrentUser();

        if (!isAdmin(admin)) {
            log.warn("El usuario {} intentó desactivar al usuario {} sin permisos", admin.getId(), id);

            throw new SecurityException("Solo los administradores pueden desactivar usuarios");
        }

        User user = findByIdOrThrow(id);

        if (!user.isEnabled()) {
            log.warn("El usuario {} ya estaba desactivado", id);

            throw new IllegalStateException("El usuario ya está desactivado");
        }

        if (isAdmin(user)) {
            log.error("Intento de desactivar al administrador con ID {}", id);

            throw new SecurityException("No puedes desactivar a un administrador");
        }

        user.setActive(false);

        save(user);

        log.info("Usuario con ID {} desactivado por el administrador {}", id, admin.getId());

        adminActionService.logAction(
                admin,
                ActionType.DEACTIVATE_USER,
                "Usuario desactivado por el administrador",
                "Usuario " + user.getUsername() + " desactivado por el administrador",
                user.getId(),
                "users",
                user);

        notificationService.createNotification(user,
                "Cuenta desactivada",
                "Tu cuenta ha sido desactivada por el administrador",
                NotificationType.ADMIN_ACTION);
    }

    /**
     * Cambia el tipo de privacidad del perfil del usuario actual.
     * 
     * @param privacyType Nuevo tipo de privacidad
     */
    @Override
    public void changeProfileType(PrivacyType privacyType) {
        User user = getCurrentUser();

        log.debug("Cambiando privacidad del usuario {} a {}", user.getId(), privacyType);

        if (isAdmin(user) && privacyType == PrivacyType.PUBLIC) {
            log.warn("El administrador {} intentó establecer perfil público", user.getId());

            throw new SecurityException("Los administradores no pueden tener un perfil público");
        }

        user.setPrivacyType(privacyType);

        save(user);

        log.info("Privacidad del usuario {} actualizada a {}", user.getId(), privacyType);
    }

    /**
     * Obtiene el usuario actual autenticado.
     * 
     * @return Usuario actual
     */
    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return findByUsernameAsEntity(authentication.getName());
    }

    /**
     * Verifica si el usuario actual es administrador.
     * 
     * @return true si es administrador, false en caso contrario
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isCurrentUserAdmin() {
        return isAdmin(getCurrentUser());
    }

    /**
     * Busca un administrador activo.
     * 
     * @return Administrador activo o null si no existe
     */
    @Override
    @Transactional(readOnly = true)
    public User findActiveAdmin() {
        return userRepository.findActiveAdmin().orElse(null);
    }

    /**
     * Elimina un usuario.
     * 
     * @param id ID del usuario a eliminar
     */
    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.debug("Intentando eliminar usuario con ID: {}", id);

        User currentUser = getCurrentUser();

        if (!currentUser.getId().equals(id) && !isAdmin(currentUser)) {
            log.warn("El usuario {} intentó eliminar al usuario {} sin permisos", currentUser.getId(), id);

            throw new SecurityException("No tienes permiso para eliminar este usuario");
        }

        User userToDelete = findByIdOrThrow(id);

        if (isAdmin(userToDelete)) {
            List<User> allAdmins = userRepository.findAll().stream()
                    .filter(user -> isAdmin(user))
                    .toList();

            if (allAdmins.size() <= 1) {
                log.error("Intento de eliminar al último administrador del sistema");

                throw new SecurityException("No puedes eliminar el último administrador del sistema");
            }
        }

        if (isAdmin(currentUser) && !currentUser.getId().equals(id)) {
            ActionType actionType = isAdmin(userToDelete) ? ActionType.DELETE_ADMIN : ActionType.DELETE_USER;

            String actionTitle = isAdmin(userToDelete)
                    ? "Administrador eliminado"
                    : "Usuario eliminado";

            String actionDescription = isAdmin(userToDelete)
                    ? "Administrador " + userToDelete.getUsername() + " eliminado por un administrador"
                    : "Usuario " + userToDelete.getUsername() + " eliminado por un administrador";

            log.info("{} con ID {} eliminado por el administrador {}",
                    isAdmin(userToDelete) ? "Administrador" : "Usuario",
                    userToDelete.getId(), currentUser.getId());

            adminActionService.logAction(currentUser, actionType,
                    actionTitle, actionDescription,
                    userToDelete.getId(), "users", userToDelete);
        }

        userRepository.delete(userToDelete);

        log.info("Usuario con ID {} eliminado correctamente", id);
    }

    /**
     * Crea un usuario administrador.
     * 
     * @param request Datos del usuario administrador
     * @return Usuario administrador creado
     */
    @Override
    public UserResponse createAdminUser(UserRegisterRequest request) {
        User currentUser = getCurrentUser();
        if (!isAdmin(currentUser)) {
            log.warn("El usuario con ID {} intentó crear un administrador sin permisos", currentUser.getId());

            throw new SecurityException("Solo los administradores pueden crear otros administradores");
        }

        log.debug("Validando campos únicos para nuevo administrador");

        validateUniqueFields(request);

        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.password()));

        user.setRole(roleService.findByNameOrThrow(Role.ADMIN));

        user.setPrivacyType(PrivacyType.PRIVATE);

        User savedUser = save(user);

        log.info("Administrador creado con ID {}", savedUser.getId());

        UserProfile profile = UserProfile.builder()
                .user(savedUser)
                .creationDate(LocalDate.now())
                .updateDate(LocalDate.now())
                .build();

        UserProfile savedProfile = userProfileRepository.save(profile);
        savedUser.setProfile(savedProfile);

        save(savedUser);

        adminActionService.logAction(
                currentUser,
                ActionType.CREATE_ADMIN,
                "Usuario administrador creado",
                "Usuario administrador creado por un administrador",
                savedUser.getId(),
                "users",
                savedUser);

        log.info("Acción de creación de administrador registrada por el admin con ID {}", currentUser.getId());

        return userMapper.toResponse(savedUser);
    }

    /**
     * Cuenta el número total de usuarios.
     * 
     * @return Número total de usuarios
     */
    @Override
    @Transactional(readOnly = true)
    public long countTotalUsers() {
        log.debug("Contando el número total de usuarios");
        return userRepository.count();
    }

    /**
     * Cuenta el número de usuarios activos.
     * 
     * @return Número de usuarios activos
     */
    @Override
    @Transactional(readOnly = true)
    public long countActiveUsers() {
        log.debug("Contando el número de usuarios activos");
        return userRepository.countByActiveTrue();
    }

    /**
     * Cuenta el número de usuarios inactivos.
     * 
     * @return Número de usuarios inactivos
     */
    @Override
    @Transactional(readOnly = true)
    public long countInactiveUsers() {
        log.debug("Contando el número de usuarios inactivos");

        return userRepository.countByActiveFalse();
    }

    /**
     * Cuenta el número de administradores activos en el sistema.
     *
     * @return número de administradores activos
     */
    @Override
    @Transactional(readOnly = true)
    public long countActiveAdmins() {
        log.debug("Contando administradores activos");
        return userRepository.countActiveAdmins();
    }

    /**
     * Valida que los campos únicos no existan.
     * 
     * @param request Datos de registro
     */
    private void validateUniqueFields(UserRegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            log.error("El nombre de usuario '{}' ya existe", request.username());

            throw new UsernameAlreadyExistsException("El nombre de usuario ya existe");
        }

        if (userRepository.existsByEmail(request.email())) {
            log.error("El email '{}' ya existe", request.email());

            throw new EmailAlreadyExistsException("Email already exists");
        }
    }

    /**
     * Actualiza el perfil extendido de un usuario.
     * 
     * @param userId  ID del usuario
     * @param request Datos del perfil
     * @return Perfil actualizado
     */
    @Override
    @Transactional
    public UserProfileResponse updateUserProfileExtended(Long userId, UserProfileRequest request) {
        log.debug("Actualizando perfil extendido del usuario con ID {}", userId);

        User user = findByIdOrThrow(userId);

        UserProfile profile = userProfileRepository.findByUser(user)
                .orElse(new UserProfile());

        boolean isNewProfile = profile.getId() == null;

        if (isNewProfile) {
            log.debug("Creando nuevo perfil para el usuario con ID {}", userId);

            profile.setCreationDate(LocalDate.now());
        }
        profile.setUpdateDate(LocalDate.now());

        updateProfileFields(profile, request);

        profile.setUser(user);

        UserProfile savedProfile = userProfileRepository.save(profile);

        log.info("Perfil extendido actualizado para el usuario con ID {}", userId);

        return mapToUserProfileResponse(savedProfile);
    }

    /**
     * Actualiza los campos del perfil de usuario.
     * 
     * @param profile Perfil a actualizar
     * @param request Datos de actualización
     */
    private void updateProfileFields(UserProfile profile, UserProfileRequest request) {
        log.debug("Iniciando actualización de campos del perfil del usuario con ID {}",
                profile.getUser() != null ? profile.getUser().getId() : "desconocido");

        if (request.birthDate() != null) {
            profile.setBirthDate(request.birthDate());

            log.debug("Fecha de nacimiento actualizada");
        }
        if (request.occupation() != null) {
            profile.setOccupation(request.occupation());

            log.debug("Ocupación actualizada");
        }
        if (request.interests() != null) {
            profile.setInterests(request.interests());

            log.debug("Intereses actualizados");
        }
        if (request.website() != null) {
            profile.setWebsite(request.website());

            log.debug("Sitio web actualizado");
        }
        if (request.location() != null) {
            profile.setLocation(request.location());

            log.debug("Ubicación actualizada");
        }
        if (request.socialMedia() != null) {
            profile.setSocialMedia(request.socialMedia());

            log.debug("Redes sociales actualizadas");
        }
        if (request.education() != null) {
            profile.setEducation(request.education());

            log.debug("Educación actualizada");
        }
        if (request.company() != null) {
            profile.setWorkplace(request.company());

            log.debug("Compañía actualizada");
        }
    }

    /**
     * Obtiene el perfil de un usuario.
     * 
     * @param userId ID del usuario
     * @return Perfil del usuario
     */
    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(Long userId) {
        log.debug("Obteniendo perfil del usuario con ID {}", userId);

        User user = findByIdOrThrow(userId);

        UserProfile profile = userProfileRepository.findByUser(user)
                .orElse(null);

        if (profile == null) {
            log.warn("El usuario con ID {} no tiene perfil asociado", userId);

            return null;
        }
        log.info("Perfil obtenido para el usuario con ID {}", userId);

        return mapToUserProfileResponse(profile);
    }

    /**
     * Mapea un perfil de usuario a su respuesta DTO.
     * 
     * @param profile Perfil a mapear
     * @return Respuesta DTO del perfil
     */
    private UserProfileResponse mapToUserProfileResponse(UserProfile profile) {
        return new UserProfileResponse(
                profile.getId(),
                profile.getBirthDate(),
                profile.getOccupation(),
                profile.getInterests(),
                profile.getWebsite(),
                profile.getLocation(),
                profile.getSocialMedia(),
                profile.getEducation(),
                profile.getWorkplace(),
                profile.getCreationDate(),
                profile.getUpdateDate());
    }

    /**
     * Obtiene el usuario actual con su perfil.
     * 
     * @return Usuario actual con perfil
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUserWithProfile() {
        User user = getCurrentUser();
        log.debug("Obtenisendo usuario actual con perfil. ID: {}", user.getId());
        return userMapper.toResponse(user);
    }

    /**
     * Notifica a todos los administradores activos sobre un nuevo usuario
     * registrado.
     * 
     * @param newUser Usuario recién registrado
     */
    private void notifyAdminsAboutNewUser(User newUser) {
        try {
            Role adminRole = roleService.findByNameOrThrow("ADMIN");
            java.util.List<User> admins = userRepository.findByRoleAndActiveTrue(adminRole);

            String fullName = newUser.getFirstName() + " " + newUser.getLastName();

            for (User admin : admins) {
                notificationService.createNotificationWithReference(
                        admin,
                        "Nuevo usuario registrado",
                        fullName + " se ha unido a MindHub",
                        NotificationType.ADMIN_ACTION,
                        newUser.getId(),
                        "users");
            }

            log.info("Notificaciones enviadas a {} administradores sobre nuevo usuario: {}",
                    admins.size(), newUser.getUsername());
        } catch (Exception e) {
            log.error("Error al notificar a administradores sobre nuevo usuario: {}",
                    e.getMessage(), e);
        }
    }

}
