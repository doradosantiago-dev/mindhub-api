package com.mindhub.api.dto.report;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de request que representa la información necesaria para reportar
 * un post dentro de la plataforma.
 *
 * Este record se utiliza en los endpoints de reportes para recibir
 * los datos de la denuncia realizada por un usuario sobre un post.
 * Incluye validaciones para asegurar que los campos requeridos estén
 * presentes y cumplan con las restricciones de formato y longitud.
 *
 * @param reason      motivo principal del reporte.
 *                    - No puede estar en blanco.
 *                    - Debe tener un máximo de 500 caracteres.
 * @param description descripción opcional más detallada del reporte.
 *                    - Máximo 1000 caracteres.
 * @param postId      identificador del post que se está reportando.
 *                    - No puede ser nulo.
 */

public record ReportRequest(
        @NotBlank(message = "Se requiere una razón") @Size(max = 500, message = "La razón debe tener menos de 500 caracteres") String reason,
        @Size(max = 1000, message = "La descripción debe tener menos de 1000 caracteres") String description,
        @NotNull(message = "Se requiere el ID de la publicación") Long postId) {
}
