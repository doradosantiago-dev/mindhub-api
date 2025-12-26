package com.mindhub.api.repository.reaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mindhub.api.model.enums.ReactionType;
import com.mindhub.api.model.post.Post;
import com.mindhub.api.model.reaction.Reaction;
import com.mindhub.api.model.user.User;

import java.util.Optional;

/**
 * Repositorio para la gestión de reacciones en publicaciones.
 *
 * Proporciona métodos para consultar, contar y administrar las reacciones
 * de usuarios en posts, incluyendo verificación de existencia y conteo
 * por tipo de reacción.
 */

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    /**
     * Busca reacciones de un post con paginación.
     * 
     * Retorna todas las reacciones de un post específico ordenadas
     * por fecha de creación descendente (más recientes primero).
     * 
     * 
     * @param post     Post del cual obtener las reacciones
     * @param pageable Configuración de paginación
     * @return Página de reacciones ordenadas por fecha
     * 
     * @see Reaction
     * @see Post
     */
    @Query("SELECT r FROM Reaction r WHERE r.post = :post ORDER BY r.creationDate DESC")
    Page<Reaction> findByPostOrderByCreationDateDesc(@Param("post") Post post, Pageable pageable);

    /**
     * Busca una reacción específica de un usuario en un post.
     * 
     * Retorna la reacción de un usuario específico en un post
     * específico, si existe.
     * 
     * 
     * @param user Usuario que realizó la reacción
     * @param post Post donde se realizó la reacción
     * @return Optional con la reacción, o vacío si no existe
     * 
     * @see Reaction
     * @see User
     * @see Post
     */
    @Query("SELECT r FROM Reaction r WHERE r.user = :user AND r.post = :post")
    Optional<Reaction> findByUserAndPost(@Param("user") User user, @Param("post") Post post);

    /**
     * Cuenta el número total de reacciones de un post.
     * 
     * Método optimizado para obtener el conteo de reacciones
     * sin cargar todos los datos de las reacciones.
     * 
     * 
     * @param post Post del cual contar las reacciones
     * @return Número total de reacciones del post
     * 
     * @see Post
     */
    @Query("SELECT COUNT(r) FROM Reaction r WHERE r.post = :post")
    long countByPost(@Param("post") Post post);

    /**
     * Cuenta el número de reacciones de un tipo específico en un post.
     *
     * Método optimizado para obtener el conteo de reacciones
     * de un tipo específico sin cargar todos los datos.
     * 
     * 
     * @param post Post del cual contar las reacciones
     * @param type Tipo de reacción a contar
     * @return Número total de reacciones del tipo especificado
     * 
     * @see Post
     * @see ReactionType
     */
    @Query("SELECT COUNT(r) FROM Reaction r WHERE r.post = :post AND r.type = :type")
    long countByPostAndType(@Param("post") Post post, @Param("type") ReactionType type);

    /**
     * Verifica si existe una reacción de un usuario en un post.
     * 
     * @param user Usuario que realizó la reacción
     * @param post Post donde se realizó la reacción
     * @return true si existe la reacción, false en caso contrario
     * 
     * @see User
     * @see Post
     */
    boolean existsByUserAndPost(User user, Post post);

    /**
     * Elimina todas las reacciones de un post.
     * 
     * Método de eliminación en cascada para limpiar todas
     * las reacciones de un post específico.
     * 
     * 
     * @param post Post cuyas reacciones se eliminarán
     * 
     * @see Post
     */
    void deleteByPost(Post post);
}
