package com.mindhub.api.repository.follow;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mindhub.api.model.follow.Follow;
import com.mindhub.api.model.user.User;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de relaciones de seguimiento entre usuarios.
 *
 * Proporciona métodos para consultar, contar y administrar las relaciones
 * de seguimiento (follows) entre usuarios del sistema.
 */

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    /**
     * Verifica si existe una relación de seguimiento entre dos usuarios.
     * 
     * @param follower Usuario que sigue
     * @param followed Usuario que es seguido
     * @return true si existe la relación, false en caso contrario
     * 
     * @see User
     * @see Follow
     */
    boolean existsByFollowerAndFollowed(User follower, User followed);

    /**
     * Busca una relación de seguimiento específica entre dos usuarios.
     * 
     * @param follower Usuario que sigue
     * @param followed Usuario que es seguido
     * @return Optional con la relación de seguimiento, o vacío si no existe
     * 
     * @see User
     * @see Follow
     */
    Optional<Follow> findByFollowerAndFollowed(User follower, User followed);

    /**
     * Cuenta el número de seguidores de un usuario.
     * 
     * Método optimizado para obtener el conteo de seguidores
     * sin cargar todos los datos de las relaciones.
     * 
     * 
     * @param user Usuario del cual contar los seguidores
     * @return Número total de seguidores del usuario
     * 
     * @see User
     */
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.followed = :user")
    long countFollowers(@Param("user") User user);

    /**
     * Cuenta el número de usuarios que sigue un usuario.
     * 
     * Método optimizado para obtener el conteo de seguidos
     * sin cargar todos los datos de las relaciones.
     * 
     * 
     * @param user Usuario del cual contar los seguidos
     * @return Número total de usuarios que sigue
     * 
     * @see User
     */
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower = :user")
    long countFollowed(@Param("user") User user);

    /**
     * Busca las relaciones de seguimiento donde el usuario es seguido.
     * 
     * @param user     Usuario que es seguido
     * @param pageable Configuración de paginación
     * @return Página de relaciones de seguimiento ordenadas por fecha
     * 
     * @see User
     * @see Follow
     */
    @Query("SELECT f FROM Follow f WHERE f.followed = :user ORDER BY f.followDate DESC")
    Page<Follow> findFollowsByFollowed(@Param("user") User user, Pageable pageable);

    /**
     * Busca las relaciones de seguimiento donde el usuario es el seguidor.
     * 
     * @param user     Usuario que sigue
     * @param pageable Configuración de paginación
     * @return Página de relaciones de seguimiento ordenadas por fecha
     * 
     * @see User
     * @see Follow
     */
    @Query("SELECT f FROM Follow f WHERE f.follower = :user ORDER BY f.followDate DESC")
    Page<Follow> findFollowsByFollower(@Param("user") User user, Pageable pageable);

    /**
     * Obtiene los IDs de los usuarios que sigue un usuario específico.
     * 
     * Método optimizado para obtener solo los IDs sin cargar
     * todos los datos de los usuarios seguidos.
     * 
     * 
     * @param user Usuario del cual obtener los IDs de seguidos
     * @return Lista de IDs de usuarios seguidos
     * 
     * @see User
     */
    @Query("SELECT f.followed.id FROM Follow f WHERE f.follower = :user")
    List<Long> findFollowedIdsByUser(@Param("user") User user);

}
