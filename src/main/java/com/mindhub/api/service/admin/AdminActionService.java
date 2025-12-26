package com.mindhub.api.service.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mindhub.api.dto.admin.AdminActionResponse;
import com.mindhub.api.model.adminAction.AdminAction;
import com.mindhub.api.model.enums.ActionType;
import com.mindhub.api.model.user.User;
import com.mindhub.api.service.base.GenericService;

/**
 * Servicio para la gestión de acciones administrativas.
 *
 * Proporciona funcionalidades para registrar y consultar
 * acciones realizadas por administradores en el sistema.
 */

public interface AdminActionService extends GenericService<AdminAction, Long> {

    /**
     * Registra una acción administrativa en el sistema.
     *
     * @param admin        administrador que realiza la acción
     * @param actionType   tipo de acción realizada
     * @param title        título de la acción
     * @param description  descripción detallada
     * @param entityId     identificador de la entidad afectada
     * @param entityType   tipo de entidad afectada
     * @param affectedUser usuario afectado por la acción
     * @return acción administrativa registrada
     */
    AdminAction logAction(User admin, ActionType actionType, String title,
            String description, Long entityId, String entityType, User affectedUser);

    /**
     * Obtiene todas las acciones administrativas de forma paginada.
     *
     * @param pageable configuración de paginación
     * @return página de acciones administrativas
     */
    Page<AdminActionResponse> getAllActions(Pageable pageable);

}
