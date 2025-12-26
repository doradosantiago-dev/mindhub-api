package com.mindhub.api.model.enums;

/**
 * Enum que define los diferentes tipos de notificaciones disponibles en el
 * sistema.
 *
 * Cada valor representa una categoría de evento que puede generar una
 * notificación
 * para el usuario, ya sea por interacciones sociales, reportes o acciones
 * administrativas.
 */

public enum NotificationType {

    /**
     * Notificación generada cuando un usuario realiza un comentario
     * en una publicación o contenido.
     */
    COMMENT,

    /**
     * Notificación generada cuando un usuario reacciona
     * (like) a una publicación o comentario.
     */
    REACTION,

    /**
     * Notificación generada cuando un usuario sigue a otro usuario.
     */
    FOLLOW,

    /**
     * Notificación relacionada con la creación o resolución
     * de un reporte dentro del sistema.
     */
    REPORT,

    /**
     * Notificación generada por una acción administrativa,
     * como la activación, desactivación o eliminación de un usuario o contenido.
     */
    ADMIN_ACTION
}
