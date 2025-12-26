package com.mindhub.api.model.enums;

/**
 * Enum que define los diferentes estados posibles de un reporte en el sistema.
 *
 * Cada valor representa la situación actual de un reporte realizado por un
 * usuario,
 * permitiendo al sistema y a los administradores gestionar su ciclo de vida
 * desde la creación hasta su resolución o rechazo. Se utiliza en la entidad
 * Report para clasificar y filtrar los reportes registrados.
 */

public enum ReportStatus {

    /**
     * El reporte ha sido creado y está pendiente de revisión
     * por parte de un administrador.
     */
    PENDING,

    /**
     * El reporte ha sido revisado y se ha considerado válido,
     * aplicando las acciones correspondientes.
     */
    RESOLVED,

    /**
     * El reporte ha sido revisado y se ha considerado inválido
     * o no justificado, por lo que se rechaza.
     */
    REJECTED
}
