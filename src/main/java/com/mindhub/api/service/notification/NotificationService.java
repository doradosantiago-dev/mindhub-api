package com.mindhub.api.service.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mindhub.api.dto.notification.NotificationResponse;
import com.mindhub.api.model.enums.NotificationType;
import com.mindhub.api.model.notification.Notification;
import com.mindhub.api.model.user.User;
import com.mindhub.api.service.base.GenericService;

/**
 * Servicio para la gestión de notificaciones.
 *
 * Define operaciones para crear, consultar y administrar notificaciones
 * de usuarios, incluyendo el marcado de lectura.
 */

public interface NotificationService extends GenericService<Notification, Long> {

        /**
         * Crea una notificación básica para un usuario.
         * 
         * @param user    Usuario destinatario de la notificación
         * @param title   Título de la notificación
         * @param message Mensaje de la notificación
         * @param type    Tipo de notificación
         * @return Notificación creada
         */
        Notification createNotification(User user, String title, String message,
                        NotificationType type);

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
        Notification createNotificationWithReference(User user, String title, String message,
                        NotificationType type,
                        Long referenceId, String referenceType);

        /**
         * Obtiene las notificaciones del usuario actual paginadas.
         * 
         * @param pageable Configuración de paginación
         * @return Página de notificaciones del usuario
         */
        Page<NotificationResponse> getUserNotifications(Pageable pageable);

        /**
         * Obtiene las notificaciones no leídas del usuario actual paginadas.
         * 
         * @param pageable Configuración de paginación
         * @return Página de notificaciones no leídas
         */
        Page<NotificationResponse> getUnreadUserNotifications(Pageable pageable);

        /**
         * Marca una notificación como leída.
         * 
         * @param id ID de la notificación a marcar como leída
         */
        void markAsRead(Long id);

        /**
         * Marca todas las notificaciones del usuario actual como leídas.
         */
        void markAllAsRead();

        /**
         * Cuenta las notificaciones no leídas del usuario actual.
         * 
         * @return Número de notificaciones no leídas
         */
        long countUnreadNotifications();

}
