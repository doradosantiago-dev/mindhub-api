package com.mindhub.api.dto.admin;

import java.time.LocalDate;

/**
 * DTO de respuesta que representa una acción administrativa realizada
 * dentro de la plataforma.
 *
 * Este record se utiliza para devolver información sobre acciones ejecutadas
 * por un administrador, incluyendo detalles del administrador que la realizó,
 * el tipo de acción, la entidad afectada y el usuario impactado.
 *
 * Se emplea en endpoints de auditoría, historial de acciones administrativas
 * o paneles de control para la gestión de la plataforma.
 *
 * @param id                    identificador único de la acción administrativa
 * @param adminUsername         nombre de usuario del administrador que realizó
 *                              la acción
 * @param adminFirstName        nombre del administrador
 * @param adminLastName         apellidos del administrador
 * @param actionType            tipo de acción realizada (ej. ACTIVE_USER,
 *                              DELETE_POST, UPDATE_ROLE)
 * @param title                 título breve o resumen de la acción
 * @param description           descripción detallada de la acción realizada
 * @param entityId              identificador de la entidad afectada (ej. id de
 *                              post, usuario, reporte)
 * @param entityType            tipo de entidad afectada (ej. "User", "Post",
 *                              "Report")
 * @param affectedUserUsername  nombre de usuario del usuario afectado por la
 *                              acción (si aplica)
 * @param affectedUserFirstName nombre del usuario afectado
 * @param affectedUserLastName  apellidos del usuario afectado
 * @param actionDate            fecha en la que se realizó la acción
 *                              administrativa
 */
public record AdminActionResponse(
        Long id,
        String adminUsername,
        String adminFirstName,
        String adminLastName,
        String actionType,
        String title,
        String description,
        Long entityId,
        String entityType,
        String affectedUserUsername,
        String affectedUserFirstName,
        String affectedUserLastName,
        LocalDate actionDate) {
}