package com.mindhub.api.service.follow;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mindhub.api.dto.follow.FollowStats;
import com.mindhub.api.exception.FollowException;
import com.mindhub.api.model.enums.NotificationType;
import com.mindhub.api.model.follow.Follow;
import com.mindhub.api.model.user.User;
import com.mindhub.api.repository.follow.FollowRepository;
import com.mindhub.api.service.notification.NotificationService;
import com.mindhub.api.service.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio de relaciones de seguimiento.
 *
 * Gestiona las operaciones de seguimiento entre usuarios,
 * incluyendo validaciones y estadísticas de seguidores.
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;
    private final NotificationService notificationService;

    /**
     * Sigue a un usuario.
     * 
     * @param follower Usuario que va a seguir
     * @param userId   ID del usuario a seguir
     * @return Relación de seguimiento creada
     */
    @Override
    public Follow follow(User follower, Long userId) {
        log.debug("Usuario {} intenta seguir al usuario {}", follower.getId(), userId);

        User followed = userService.findByIdOrThrow(userId);

        if (follower.getId().equals(followed.getId())) {
            log.warn("Usuario {} intentó seguirse a sí mismo", follower.getId());

            throw new FollowException("No puedes seguirte a ti mismo");
        }

        if (followRepository.existsByFollowerAndFollowed(follower, followed)) {
            log.warn("Usuario {} ya sigue al usuario {}", follower.getId(), userId);

            throw new FollowException("Ya estás siguiendo a este usuario");
        }

        Follow follow = Follow.builder()
                .follower(follower)
                .followed(followed)
                .build();

        Follow savedFollow = followRepository.save(follow);

        log.info("Usuario {} comenzó a seguir al usuario {}", follower.getId(), userId);

        // Enviar notificación al usuario seguido
        notificationService.createNotification(
                followed,
                "Nuevo seguidor",
                follower.getFirstName() + " " + follower.getLastName() + " ha comenzado a seguirte",
                NotificationType.FOLLOW);

        log.debug("Notificación de seguimiento enviada al usuario {}", followed.getId());

        return savedFollow;
    }

    /**
     * Deja de seguir a un usuario.
     * 
     * @param follower Usuario que va a dejar de seguir
     * @param userId   ID del usuario a dejar de seguir
     */
    @Override
    public void unfollow(User follower, Long userId) {
        log.debug("Usuario {} intenta dejar de seguir al usuario {}", follower.getId(), userId);

        User followed = userService.findByIdOrThrow(userId);

        Follow follow = followRepository.findByFollowerAndFollowed(follower, followed)
                .orElseThrow(() -> new FollowException("No sigues a este usuario"));

        followRepository.delete(follow);

        log.info("Usuario {} dejó de seguir al usuario {}", follower.getId(), userId);
    }

    /**
     * Verifica si un usuario sigue a otro.
     * 
     * @param follower Usuario que podría estar siguiendo
     * @param followed Usuario que podría estar siendo seguido
     * @return true si follower sigue a followed, false en caso contrario
     */
    @Override
    @Transactional(readOnly = true)
    public boolean follows(User follower, User followed) {
        log.debug("Verificando si usuario {} sigue a {}", follower.getId(), followed.getId());

        return followRepository.existsByFollowerAndFollowed(follower, followed);
    }

    /**
     * Cuenta el número de seguidores de un usuario.
     * 
     * @param user Usuario del cual contar seguidores
     * @return Número de seguidores
     */
    @Override
    @Transactional(readOnly = true)
    public long countFollowers(User user) {
        log.debug("Contando seguidores del usuario {}", user.getId());

        return followRepository.countFollowers(user);
    }

    /**
     * Cuenta el número de usuarios que sigue un usuario.
     * 
     * @param user Usuario del cual contar seguidos
     * @return Número de usuarios seguidos
     */
    @Override
    @Transactional(readOnly = true)
    public long countFollowing(User user) {
        log.debug("Contando seguidos del usuario {}", user.getId());

        return followRepository.countFollowed(user);
    }

    /**
     * Obtiene las relaciones de seguimiento donde el usuario es seguido.
     * 
     * @param user     Usuario que es seguido
     * @param pageable Configuración de paginación
     * @return Página de relaciones de seguimiento
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Follow> getFollowsByFollowed(User user, Pageable pageable) {
        log.debug("Consultando seguidores del usuario {} página {}", user.getId(), pageable.getPageNumber());

        return followRepository.findFollowsByFollowed(user, pageable);
    }

    /**
     * Obtiene las relaciones de seguimiento donde el usuario es seguidor.
     * 
     * @param user     Usuario que es seguidor
     * @param pageable Configuración de paginación
     * @return Página de relaciones de seguimiento
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Follow> getFollowsByFollower(User user, Pageable pageable) {
        log.debug("Consultando seguidos del usuario {} página {}", user.getId(), pageable.getPageNumber());

        return followRepository.findFollowsByFollower(user, pageable);
    }

    /**
     * Obtiene estadísticas de seguimiento entre dos usuarios.
     * 
     * @param currentUser Usuario actual
     * @param user        Usuario del cual obtener estadísticas
     * @return Estadísticas de seguimiento
     */
    @Override
    @Transactional(readOnly = true)
    public FollowStats getFollowStats(User currentUser, User user) {
        log.debug("Obteniendo estadísticas de seguimiento entre usuario {} y usuario {}",
                currentUser != null ? currentUser.getId() : "anon", user.getId());
        long followers = countFollowers(user);
        long following = countFollowing(user);

        boolean followsYou = false;
        boolean follows = false;

        if (currentUser != null && !currentUser.getId().equals(user.getId())) {
            followsYou = follows(user, currentUser);

            follows = follows(currentUser, user);
        }

        log.info("Estadísticas de usuario {} -> followers={}, following={}, follows={}, followsYou={}",
                user.getId(), followers, following, follows, followsYou);

        return FollowStats.builder()
                .followers(followers)
                .followed(following)
                .follows(follows)
                .followsYou(followsYou)
                .build();
    }
}
