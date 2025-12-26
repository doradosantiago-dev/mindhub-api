package com.mindhub.api.model.enums;

/**
 * Enum que define los tipos de acciones administrativas disponibles en el
 * sistema.
 *
 * Cada valor representa una categoría específica de acción que un administrador
 * puede realizar sobre usuarios, publicaciones, reportes u otros elementos.
 * Se utiliza en la entidad
 * {@link com.nexora.mindHub.model.adminAction.AdminAction}
 * para clasificar y filtrar las acciones registradas.
 */

public enum ActionType {

    /**
     * Activación de un usuario previamente desactivado.
     * Permite que el usuario vuelva a acceder al sistema.
     */
    ACTIVATE_USER,

    /**
     * Desactivación de un usuario activo.
     * Impide que el usuario acceda al sistema temporalmente.
     */
    DEACTIVATE_USER,

    /**
     * Actualización de información de un usuario.
     * Modificación de datos personales, roles, o configuración.
     */
    UPDATE_USER,

    /**
     * Eliminación permanente de un usuario del sistema.
     * Acción irreversible que elimina todos los datos asociados.
     */
    DELETE_USER,

    /**
     * Eliminación de un post o publicación.
     * Puede ser por contenido inapropiado o violación de términos.
     */
    DELETE_POST,

    /**
     * Resolución favorable de un reporte.
     * El administrador considera válido el reporte y toma acción.
     */
    RESOLVE_REPORT,

    /**
     * Rechazo de un reporte.
     * El administrador considera que el reporte no es válido.
     */
    REJECT_REPORT,

    /**
     * Creación de un nuevo administrador.
     * Asignación de privilegios administrativos a un usuario.
     */
    CREATE_ADMIN,

    /**
     * Eliminación de privilegios administrativos.
     * Revocación del rol de administrador de un usuario.
     */
    DELETE_ADMIN
}
