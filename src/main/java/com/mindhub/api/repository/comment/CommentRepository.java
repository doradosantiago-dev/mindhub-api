package com.mindhub.api.repository.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mindhub.api.model.comment.Comment;
import com.mindhub.api.model.post.Post;

/**
 * Repositorio para la gestión de comentarios en publicaciones.
 *
 * Proporciona métodos para consultar, contar y administrar los comentarios
 * realizados por usuarios en los posts del sistema.
 */

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Busca comentarios de un post con paginación.
     * 
     * Retorna todos los comentarios de un post específico ordenados
     * por fecha de creación ascendente (cronológicamente).
     * 
     * 
     * @param post     Post del cual obtener los comentarios
     * @param pageable Configuración de paginación
     * @return Página de comentarios ordenados cronológicamente
     * 
     * @see Comment
     * @see Post
     */
    @Query("SELECT c FROM Comment c WHERE c.post = :post ORDER BY c.creationDate ASC")
    Page<Comment> findByPostOrderByCreationDateAsc(@Param("post") Post post, Pageable pageable);

    /**
     * Elimina todos los comentarios de un post.
     * 
     * Método de eliminación en cascada para limpiar todos
     * los comentarios de un post específico.
     * 
     * 
     * @param post Post cuyos comentarios se eliminarán
     * 
     * @see Post
     */
    void deleteByPost(Post post);
}
