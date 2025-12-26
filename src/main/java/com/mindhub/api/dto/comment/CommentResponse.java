package com.mindhub.api.dto.comment;

import java.time.LocalDate;

import com.mindhub.api.dto.auth.UserResponse;

/**
 * DTO de respuesta que representa un comentario realizado en un post
 * dentro de la plataforma.
 *
 * Este record se utiliza para devolver información sobre los comentarios
 * asociados a una publicación, incluyendo su contenido, autor y fechas
 * relevantes.
 *
 * Se emplea en endpoints relacionados con la visualización de comentarios
 * en posts, así como en listados o notificaciones de actividad.
 *
 * @param id           identificador único del comentario
 * @param content      contenido textual del comentario
 * @param creationDate fecha en la que se creó el comentario
 * @param editDate     fecha de la última edición del comentario (puede ser null
 *                     si nunca se editó)
 * @param author       información del autor del comentario
 *                     ({@link UserResponse})
 * @param postId       identificador del post al que pertenece el comentario
 *
 */
public record CommentResponse(
        Long id,
        String content,
        LocalDate creationDate,
        LocalDate editDate,
        UserResponse author,
        Long postId) {
}
