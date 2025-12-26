package com.mindhub.api.service.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mindhub.api.dto.comment.CommentRequest;
import com.mindhub.api.dto.comment.CommentResponse;
import com.mindhub.api.model.comment.Comment;
import com.mindhub.api.service.base.GenericService;

/**
 * Servicio para la gestión de comentarios en publicaciones.
 *
 * Define operaciones para crear, actualizar, eliminar y consultar
 * comentarios, incluyendo validaciones de privacidad.
 */

public interface CommentService extends GenericService<Comment, Long> {

    /**
     * Crea un nuevo comentario en un post.
     * 
     * @param request Datos del comentario a crear
     * @return Comentario creado
     */
    CommentResponse createComment(CommentRequest request);

    /**
     * Actualiza un comentario existente.
     * 
     * @param id      ID del comentario a actualizar
     * @param request Nuevos datos del comentario
     * @return Comentario actualizado
     */
    CommentResponse updateComment(Long id, CommentRequest request);

    /**
     * Obtiene un comentario por su ID.
     * 
     * @param id ID del comentario
     * @return Comentario encontrado
     */
    CommentResponse getCommentById(Long id);

    /**
     * Obtiene los comentarios de un post paginados.
     * 
     * @param postId   ID del post
     * @param pageable Configuración de paginación
     * @return Página de comentarios del post
     */
    Page<CommentResponse> getPostComments(Long postId, Pageable pageable);

}
