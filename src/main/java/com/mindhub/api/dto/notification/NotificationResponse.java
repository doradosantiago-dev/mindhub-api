package com.mindhub.api.dto.notification;

import java.time.LocalDate;

import com.mindhub.api.model.enums.NotificationType;


/**
 * DTO de respuesta que representa una notificación generada en la plataforma.
 *
 * Este record se utiliza para devolver información sobre las notificaciones
 * enviadas a los usuarios, incluyendo su título, mensaje, tipo, estado de
 * lectura
 * y metadatos asociados.
 *
 * Se emplea en endpoints relacionados con la gestión y visualización de
 * notificaciones dentro de la aplicación, permitiendo al cliente mostrar
 * alertas, recordatorios o avisos de actividad.
 *
 * @param id               identificador único de la notificación
 * @param title            título breve de la notificación
 * @param message          mensaje descriptivo o detalle de la notificación
 * @param notificationType tipo de notificación ({@link NotificationType}
 * @param read             indicador booleano que señala si la notificación ya
 *                         fue leída por el usuario
 * @param creationDate     fecha en la que se creó la notificación
 * @param readDate         fecha en la que la notificación fue marcada como
 *                         leída (puede ser null si aún no se ha leído)
 * @param referenciaId     identificador del recurso relacionado con la
 *                         notificación (ej. id de post, comentario, reporte)
 * @param referenciaTabla  nombre de la tabla o entidad relacionada con la
 *                         notificación (ej. "posts", "comments", "reports")
 */
public record NotificationResponse(
        Long id,
        String title,
        String message,
        NotificationType notificationType,
        Boolean read,
        LocalDate creationDate,
        LocalDate readDate,
        Long referenciaId,
        String referenciaTabla) {
}
