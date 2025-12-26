package com.mindhub.api.mapper.adminAction;

import org.springframework.stereotype.Component;

import com.mindhub.api.dto.admin.AdminActionResponse;
import com.mindhub.api.model.adminAction.AdminAction;
import com.mindhub.api.model.user.User;
import com.mindhub.api.repository.user.UserRepository;

/**
 * Mapper responsable de transformar entidades del tipo AdminAction en objetos
 * de respuesta AdminActionResponse.
 *
 * Además de la conversión básica, este mapper incorpora la lógica necesaria
 * para recuperar información adicional de los usuarios implicados en la acción
 * administrativa (el administrador que ejecuta la acción y el usuario
 * afectado).
 * 
 */

@Component
public class AdminActionMapper {

    private final UserRepository userRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param userRepository Repositorio de usuarios para obtener datos de admin y
     *                       usuario afectado.
     */
    public AdminActionMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Convierte una entidad AdminAction en un DTO AdminActionResponse.
     *
     * Obtiene los datos del administrador y del usuario afectado a partir de sus
     * IDs. Si alguno no existe, se devuelven valores por defecto.
     *
     * @param adminAction entidad AdminAction a convertir
     * @return DTO con los datos de la acción administrativa
     */

    public AdminActionResponse toResponse(AdminAction adminAction) {
        if (adminAction == null) {
            return null;
        }

        User admin = adminAction.getAdminId() != null ? userRepository.findById(adminAction.getAdminId()).orElse(null)
                : null;

        return new AdminActionResponse(
                adminAction.getId(),
                admin != null ? admin.getUsername() : "N/A",
                admin != null ? admin.getFirstName() : "N/A",
                admin != null ? admin.getLastName() : "N/A",
                adminAction.getAction() != null ? adminAction.getAction().name() : null,
                adminAction.getReason(),
                adminAction.getDescription(),
                adminAction.getObjectId(),
                adminAction.getObjectTable(),

                // Siempre usar los valores guardados en AdminAction para preservar el historial
                adminAction.getAffectedUsername(),
                adminAction.getAffectedFirstName(),
                adminAction.getAffectedLastName(),
                adminAction.getActionDate());
    }
}
