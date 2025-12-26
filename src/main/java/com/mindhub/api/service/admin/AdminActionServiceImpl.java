package com.mindhub.api.service.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mindhub.api.dto.admin.AdminActionResponse;
import com.mindhub.api.exception.AdminActionNotFoundException;
import com.mindhub.api.mapper.adminAction.AdminActionMapper;
import com.mindhub.api.model.adminAction.AdminAction;
import com.mindhub.api.model.enums.ActionType;
import com.mindhub.api.model.user.User;
import com.mindhub.api.repository.adminAction.AdminActionRepository;
import com.mindhub.api.service.base.GenericServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio de acciones administrativas.
 *
 * Gestiona el registro y la consulta de acciones realizadas
 * por administradores en el sistema.
 */
@Slf4j
@Service
public class AdminActionServiceImpl extends GenericServiceImpl<AdminAction, Long> implements AdminActionService {

    private final AdminActionRepository adminActionRepository;
    private final AdminActionMapper adminActionMapper;

    public AdminActionServiceImpl(AdminActionRepository adminActionRepository,
            AdminActionMapper adminActionMapper) {
        super(adminActionRepository);

        this.adminActionRepository = adminActionRepository;

        this.adminActionMapper = adminActionMapper;
    }

    /**
     * Obtiene el ID de la entidad AdminAction.
     * 
     * @param entity Entidad AdminAction
     * @return ID de la entidad
     */
    @Override
    protected Long getEntityId(AdminAction entity) {
        return entity.getId();
    }

    /**
     * Crea una excepción personalizada para entidades no encontradas.
     * 
     * @param message Mensaje de error
     * @return AdminActionNotFoundException con el mensaje especificado
     */
    @Override
    protected RuntimeException createNotFoundException(String message) {
        return new AdminActionNotFoundException(message);
    }

    /**
     * Obtiene el nombre de la entidad para logging y mensajes de error.
     * 
     * @return Nombre de la entidad "AdminAction"
     */
    @Override
    public String getEntityName() {
        return "AdminAction";
    }

    /**
     * Registra una nueva acción administrativa en el sistema.
     * 
     * @param admin        Administrador que realiza la acción
     * @param actionType   Tipo de acción administrativa
     * @param title        Título o motivo de la acción
     * @param description  Descripción detallada de la acción
     * @param entityId     ID de la entidad afectada
     * @param entityType   Tipo de entidad afectada
     * @param affectedUser Usuario afectado por la acción (puede ser null)
     * @return La acción administrativa registrada
     */
    @Override
    @Transactional
    public AdminAction logAction(User admin, ActionType actionType, String title,
            String description, Long entityId, String entityType, User affectedUser) {

        log.debug("Registrando acción administrativa: {} - {}", actionType, title);

        AdminAction action = AdminAction.builder()
                .adminId(admin.getId())
                .action(actionType)
                .description(description)
                .reason(title)
                .objectId(entityId)
                .objectTable(entityType)
                .affectedUserId(affectedUser != null ? affectedUser.getId() : null)
                .affectedUsername(affectedUser != null ? affectedUser.getUsername() : null)
                .affectedFirstName(affectedUser != null ? affectedUser.getFirstName() : null)
                .affectedLastName(affectedUser != null ? affectedUser.getLastName() : null)
                .build();

        AdminAction savedAction = save(action);

        log.info("Acción administrativa registrada: ID={}, Tipo={}, Admin={}",
                savedAction.getId(), actionType, admin.getUsername());

        return savedAction;
    }

    /**
     * Obtiene todas las acciones administrativas paginadas.
     * 
     * @param pageable Configuración de paginación
     * @return Página de respuestas de acciones administrativas
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AdminActionResponse> getAllActions(Pageable pageable) {
        log.debug("Consultando todas las acciones administrativas, página: {}", pageable.getPageNumber());

        return adminActionRepository.findAll(pageable)
                .map(adminActionMapper::toResponse);
    }

}
