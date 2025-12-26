package com.mindhub.api.mapper.follow;

import org.springframework.stereotype.Component;

import com.mindhub.api.dto.follow.FollowResponse;
import com.mindhub.api.dto.follow.FollowStatsResponse;
import com.mindhub.api.model.follow.Follow;
import com.mindhub.api.model.user.User;

/**
 * Mapper encargado de convertir entidades relacionadas con {@link Follow}
 * en sus respectivos DTOs de respuesta.
 * <p>
 * Este mapper no utiliza MapStruct, ya que requiere lógica manual
 * para construir los objetos de salida con datos combinados de
 * distintas entidades.
 */

@Component
public class FollowMapper {

    /**
     * Convierte una entidad Follow en un DTO FollowResponse.
     * 
     * Incluye información tanto del seguidor como del seguido,
     * como sus IDs, nombres, usernames y fotos de perfil.
     *
     * @param follow Entidad Follow a convertir.
     * @return DTO con los datos del seguimiento.
     */

    public FollowResponse toResponse(Follow follow) {
        if (follow == null) {
            return null;
        }

        return FollowResponse.builder()
                .id(follow.getId())
                .followerId(follow.getFollower().getId())
                .followerUsername(follow.getFollower().getUsername())
                .followerName(follow.getFollower().getFirstName() + " " + follow.getFollower().getLastName())
                .followerProfilePicture(follow.getFollower().getProfilePicture())
                .followedId(follow.getFollowed().getId())
                .followedUsername(follow.getFollowed().getUsername())
                .followedName(follow.getFollowed().getFirstName() + " " + follow.getFollowed().getLastName())
                .followedProfilePicture(follow.getFollowed().getProfilePicture())
                .followDate(follow.getFollowDate())
                .build();
    }

    /**
     * Convierte un User en un DTO FollowStatsResponse,
     * incluyendo estadísticas de seguidores y seguidos.
     * 
     * También permite indicar si el usuario actual sigue al otro
     * y si el otro usuario lo sigue a él.
     *
     * @param user       Usuario al que pertenecen las estadísticas.
     * @param followers  Número de seguidores.
     * @param followed   Número de usuarios seguidos.
     * @param follows    Indica si el usuario actual sigue a este usuario.
     * @param followsYou Indica si este usuario sigue al usuario actual.
     * @return DTO con estadísticas de seguimiento.
     */
    public FollowStatsResponse toStatsResponse(User user, Long followers, Long followed, Boolean follows,
            Boolean followsYou) {
        if (user == null) {
            return null;
        }

        return FollowStatsResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .followers(followers)
                .followed(followed)
                .follows(follows)
                .followsYou(followsYou)
                .build();
    }
}
