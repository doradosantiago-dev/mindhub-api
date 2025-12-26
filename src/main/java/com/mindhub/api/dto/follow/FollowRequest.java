package com.mindhub.api.dto.follow;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.validation.constraints.NotNull;

/**
 * DTO de request que representa la acción de seguir a un usuario
 * dentro de la plataforma.
 *
 * Este objeto se utiliza en los endpoints de seguimiento para
 * indicar qué usuario se desea seguir. Incluye validaciones
 * para asegurar que el identificador del usuario objetivo
 * esté presente.
 *
 * @param userId identificador único del usuario al que se desea seguir,
 *               no puede ser nulo.
 * 
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowRequest {

    @NotNull(message = "Se requiere el ID de usuario")
    private Long userId;

}
