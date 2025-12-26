package com.mindhub.api.service.notification;

import java.time.LocalDate;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mindhub.api.dto.notification.NotificationResponse;
import com.mindhub.api.exception.NotificationNotFoundException;
import com.mindhub.api.mapper.notification.NotificationMapper;
import com.mindhub.api.model.enums.NotificationType;
import com.mindhub.api.model.notification.Notification;
import com.mindhub.api.model.user.User;
import com.mindhub.api.repository.notification.NotificationRepository;
import com.mindhub.api.service.base.GenericServiceImpl;
import com.mindhub.api.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio de notificaciones.
 *
 * Gestiona la creación, consulta y marcado de notificaciones
 * de usuarios, con validaciones de permisos.
 */

@Slf4j
@Service
@Transactional
public class NotificationServiceImpl extends GenericServiceImpl<Notification, Long> implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserService userService;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
            NotificationMapper notificationMapper,
            @Lazy UserService userService) {
        super(notificationRepository);
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.userService = userService;
    }

    /**
     * Crea una notificación básica para un usuario.
     * 
     * @param user    Usuario destinatario de la notificación
     * @param title   Título de la notificación
     * @param message Mensaje de la notificación
     * @param type    Tipo de notificación
     * @return Notificación creada
     */
    @Override
    public Notification createNotification(User user, String title, String message,
            NotificationType type) {
        log.debug("Creando notificación para usuario {}: [{}] {}", user.getId(), title, message);

        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .message(message)
                .type(type)
                .read(false)
                .build();
        log.info("Notificación {} creada para usuario {}", user.getId());

        return save(notification);
    }

    /**
     * Crea una notificación con referencia a otra entidad.
     * 
     * @param user          Usuario destinatario de la notificación
     * @param title         Título de la notificación
     * @param message       Mensaje de la notificación
     * @param type          Tipo de notificación
     * @param referenceId   ID de la entidad referenciada
     * @param referenceType Tipo de entidad referenciada
     * @return Notificación creada
     */
    @Override
    public Notification createNotificationWithReference(User user, String title, String message,
            NotificationType type,
            Long referenceId, String referenceType) {
        log.debug("Creando notificación con referencia para usuario {}: [{}] ref={} ({})",
                user.getId(), title, referenceId, referenceType);

        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .message(message)
                .type(type)
                .read(false)
                .referenceId(referenceId)
                .referenceTable(referenceType)
                .build();

        log.info("Notificación {} con referencia creada para usuario {}", user.getId());

        return save(notification);
    }

    /**
     * Obtiene las notificaciones del usuario actual paginadas.
     * 
     * @param pageable Configuración de paginación
     * @return Página de notificaciones del usuario
     */
    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getUserNotifications(Pageable pageable) {

        User currentUser = userService.getCurrentUser();

        log.debug("Consultando notificaciones del usuario {} página {}", currentUser.getId(), pageable.getPageNumber());

        return notificationRepository.findByUserOrderByCreationDateDesc(currentUser, pageable)
                .map(notificationMapper::toResponse);
    }

    /**
     * Obtiene las notificaciones no leídas del usuario actual paginadas.
     * 
     * @param pageable Configuración de paginación
     * @return Página de notificaciones no leídas
     */
    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getUnreadUserNotifications(Pageable pageable) {
        User currentUser = userService.getCurrentUser();

        log.debug("Consultando notificaciones NO leídas del usuario {} página {}", currentUser.getId(),
                pageable.getPageNumber());

        return notificationRepository.findByUserAndReadFalseOrderByCreationDateDesc(
                currentUser, pageable)
                .map(notificationMapper::toResponse);
    }

    /**
     * Marca una notificación como leída.
     * 
     * @param id ID de la notificación a marcar como leída
     */
    @Override
    public void markAsRead(Long id) {
        Notification notification = findByIdOrThrow(id);

        User currentUser = userService.getCurrentUser();

        if (!notification.getUser().getId().equals(currentUser.getId())) {
            log.warn("Usuario {} intentó marcar como leída la notificación {} de otro usuario",

                    currentUser.getId(), id);

            throw new IllegalStateException("No puedes marcar como leída una notificación de otro usuario");
        }

        if (!notification.getRead()) {
            notification.setRead(true);

            notification.setReadDate(LocalDate.now());

            save(notification);

            log.info("Notificación {} marcada como leída por usuario {}", id, currentUser.getId());
        }
    }

    /**
     * Marca todas las notificaciones del usuario actual como leídas.
     */
    @Override
    public void markAllAsRead() {
        User currentUser = userService.getCurrentUser();

        notificationRepository.markAllAsReadForUser(currentUser.getId());

        log.info("Todas las notificaciones del usuario {} marcadas como leídas", currentUser.getId());
    }

    /**
     * Cuenta las notificaciones no leídas del usuario actual.
     * 
     * @return Número de notificaciones no leídas
     */
    @Override
    @Transactional(readOnly = true)
    public long countUnreadNotifications() {
        User currentUser = userService.getCurrentUser();

        log.debug("Usuario {} tiene {} notificaciones no leídas", currentUser.getId());

        return notificationRepository.countByUserAndReadFalse(currentUser);
    }

    @Override
    protected Long getEntityId(Notification entity) {
        log.debug("Obteniendo ID de notificación {}", entity.getId());

        return entity.getId();
    }

    @Override
    protected RuntimeException createNotFoundException(String message) {
        log.error("Notificación no encontrada: {}", message);

        return new NotificationNotFoundException(message);
    }

    @Override
    public String getEntityName() {
        return "Notification";
    }
}
