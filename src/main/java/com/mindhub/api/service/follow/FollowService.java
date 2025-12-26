package com.mindhub.api.service.follow;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mindhub.api.dto.follow.FollowStats;
import com.mindhub.api.model.follow.Follow;
import com.mindhub.api.model.user.User;

/**
 * Servicio para la gestión de relaciones de seguimiento entre usuarios.
 *
 * Define operaciones para seguir o dejar de seguir a otros usuarios,
 * consultar estadísticas de seguidores y administrar relaciones de seguimiento.
 */

public interface FollowService {

    /**
     * Sigue a un usuario.
     * 
     * @param follower Usuario que va a seguir
     * @param userId   ID del usuario a seguir
     * @return Relación de seguimiento creada
     */
    Follow follow(User follower, Long userId);

    /**
     * Deja de seguir a un usuario.
     * 
     * @param follower Usuario que va a dejar de seguir
     * @param userId   ID del usuario a dejar de seguir
     */
    void unfollow(User follower, Long userId);

    /**
     * Verifica si un usuario sigue a otro.
     * 
     * @param follower Usuario que podría estar siguiendo
     * @param followed Usuario que podría estar siendo seguido
     * @return true si follower sigue a followed, false en caso contrario
     */
    boolean follows(User follower, User followed);

    /**
     * Cuenta el número de seguidores de un usuario.
     * 
     * @param user Usuario del cual contar seguidores
     * @return Número de seguidores
     */
    long countFollowers(User user);

    /**
     * Cuenta el número de usuarios que sigue un usuario.
     * 
     * @param user Usuario del cual contar seguidos
     * @return Número de usuarios seguidos
     */
    long countFollowing(User user);

    /**
     * Obtiene las relaciones de seguimiento donde el usuario es seguido.
     * 
     * @param user     Usuario que es seguido
     * @param pageable Configuración de paginación
     * @return Página de relaciones de seguimiento
     */
    Page<Follow> getFollowsByFollowed(User user, Pageable pageable);

    /**
     * Obtiene las relaciones de seguimiento donde el usuario es seguidor.
     * 
     * @param user     Usuario que es seguidor
     * @param pageable Configuración de paginación
     * @return Página de relaciones de seguimiento
     */
    Page<Follow> getFollowsByFollower(User user, Pageable pageable);

    /**
     * Obtiene estadísticas de seguimiento entre dos usuarios.
     * 
     * @param currentUser Usuario actual
     * @param user        Usuario del cual obtener estadísticas
     * @return Estadísticas de seguimiento
     */
    FollowStats getFollowStats(User currentUser, User user);
}
