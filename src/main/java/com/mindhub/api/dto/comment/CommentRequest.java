package com.mindhub.api.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de request que representa la información necesaria para crear
 * un nuevo comentario en un post dentro de la plataforma.
 *
 * Este record se utiliza en los endpoints de comentarios para recibir
 * el contenido del comentario y el identificador del post al que se
 * asocia. Incluye validaciones para asegurar que los campos requeridos
 * estén presentes y cumplan con las restricciones de formato y longitud.
 *
 * @param content contenido textual del comentario.
 *                - No puede estar en blanco.
 *                - Debe tener entre 1 y 500 caracteres.
 * @param postId  identificador del post al que pertenece el comentario.
 *                - No puede ser nulo.
 */

public record CommentRequest(
        @NotBlank(message = "El contenido del comentario es requerido") @Size(min = 1, max = 500, message = "El comentario debe tener entre 1 y 500 caracteres") String content,
        @NotNull(message = "El ID del post es requerido") Long postId) {
}
