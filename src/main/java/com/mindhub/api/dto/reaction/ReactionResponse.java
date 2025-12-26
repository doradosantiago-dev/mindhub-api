package com.mindhub.api.dto.reaction;

import java.time.LocalDate;

import com.mindhub.api.dto.auth.UserResponse;
import com.mindhub.api.model.enums.ReactionType;


/**
 * DTO de respuesta que representa una reacción realizada por un usuario
 * sobre un post en la plataforma.
 *
 * Este record se utiliza para devolver información sobre la reaccion
 * (like) asociadas a un post, incluyendo el tipo de
 * reacción, el usuario que la realizó y la fecha de creación.
 *
 * Se emplea en endpoints relacionados con la interacción de usuarios
 * con publicaciones, permitiendo mostrar estadísticas o detalles de
 * participación en la comunidad.
 *
 * @param id           identificador único de la reacción
 * @param reactionType tipo de reacción realizada ({@link ReactionType})
 * @param creationDate fecha en la que se creó la reacción
 * @param user         información del usuario que realizó la reacción
 *                     ({@link UserResponse})
 * @param postId       identificador del post al que pertenece la reacción
 *
 */
public record ReactionResponse(
        Long id,
        ReactionType reactionType,
        LocalDate creationDate,
        UserResponse user,
        Long postId) {
}
