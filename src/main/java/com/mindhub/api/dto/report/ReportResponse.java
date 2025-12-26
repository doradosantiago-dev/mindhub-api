package com.mindhub.api.dto.report;

import java.time.LocalDateTime;

import com.mindhub.api.dto.auth.UserResponse;
import com.mindhub.api.dto.post.PostSummaryResponse;
import com.mindhub.api.model.enums.ReportStatus;


/**
 * DTO de respuesta que representa la información de un reporte en la
 * plataforma.
 *
 * Este record se utiliza para devolver los detalles de un reporte realizado
 * por un usuario sobre un post. Incluye la razón del reporte, su estado,
 * las fechas relevantes y las entidades relacionadas (reportante y post).
 *
 * Se emplea en endpoints de administración o moderación para gestionar
 * reportes de contenido inapropiado o que incumple las normas de la comunidad.
 *
 * @param id          identificador único del reporte
 * @param reason      motivo principal del reporte (ej. spam, abuso, contenido
 *                    inapropiado)
 * @param description descripción adicional proporcionada por el usuario que
 *                    reporta
 * @param status      estado actual del reporte ({@link ReportStatus}: PENDING,
 *                    REVIEWED, REJECTED)
 * @param reportDate  fecha en la que se creó el reporte
 * @param reviewDate  fecha en la que el reporte fue revisado por un moderador
 *                    (puede ser null si está pendiente)
 * @param reporter    información del usuario que realizó el reporte
 *                    ({@link UserResponse})
 * @param post        resumen del post reportado ({@link PostSummaryResponse})
 *
 */
public record ReportResponse(
        Long id,
        String reason,
        String description,
        ReportStatus status,
        LocalDateTime reportDate,
        LocalDateTime reviewDate,
        UserResponse reporter,
        PostSummaryResponse post) {
}
