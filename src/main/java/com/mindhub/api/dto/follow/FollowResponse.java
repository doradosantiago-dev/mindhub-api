package com.mindhub.api.dto.follow;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

/**
 * DTO de respuesta que representa la relación de seguimiento entre dos usuarios
 * en la plataforma.
 *
 * Este objeto se utiliza para devolver información sobre quién sigue a quién,
 * incluyendo datos básicos de ambos usuarios (seguidor y seguido) y la fecha
 * en la que se estableció la relación de seguimiento.
 *
 * @param id                     identificador único de la relación de
 *                               seguimiento
 * @param followerId             identificador del usuario que sigue (seguidor)
 * @param followerUsername       nombre de usuario (username) del seguidor
 * @param followerName           nombre completo del seguidor
 * @param followerProfilePicture URL de la foto de perfil del seguidor
 * @param followedId             identificador del usuario que es seguido
 * @param followedUsername       nombre de usuario (username) del seguido
 * @param followedName           nombre completo del seguido
 * @param followedProfilePicture URL de la foto de perfil del seguido
 * @param followDate             fecha en la que se estableció la relación de
 *                               seguimiento
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowResponse {

    private Long id;
    private Long followerId;
    private String followerUsername;
    private String followerName;
    private String followerProfilePicture;
    private Long followedId;
    private String followedUsername;
    private String followedName;
    private String followedProfilePicture;
    private LocalDate followDate;
}
