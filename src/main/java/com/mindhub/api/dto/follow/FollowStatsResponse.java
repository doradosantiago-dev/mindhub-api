package com.mindhub.api.dto.follow;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * DTO de respuesta que representa las estadísticas de seguimiento
 * asociadas a un usuario en la plataforma.
 *
 * Este objeto se utiliza para devolver información resumida sobre
 * un usuario específico, incluyendo su número de seguidores, seguidos
 * y la relación de seguimiento con el usuario actual.
 *
 * Se emplea en endpoints relacionados con perfiles de usuario,
 * listados de seguidores/seguidos o vistas rápidas de interacción
 * social.
 *
 * @param id         identificador único del usuario
 * @param username   nombre de usuario (username) del perfil consultado
 * @param followers  número total de usuarios que siguen a este usuario
 * @param followed   número total de usuarios a los que este usuario sigue
 * @param follows    indicador booleano que señala si el usuario actual sigue al
 *                   perfil consultado
 * @param followsYou indicador booleano que señala si el perfil consultado sigue
 *                   al usuario actual
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowStatsResponse {

    private Long id;
    private String username;
    private Long followers;
    private Long followed;
    private Boolean follows;
    private Boolean followsYou;
}
