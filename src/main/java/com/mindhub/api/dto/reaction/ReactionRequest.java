package com.mindhub.api.dto.reaction;


import com.mindhub.api.model.enums.ReactionType;

import jakarta.validation.constraints.NotNull;

/**
 * DTO de request que representa la información necesaria para registrar
 * una reacción en la plataforma.
 *
 * Este record se utiliza en los endpoints de reacciones para recibir
 * los datos de la interacción de un usuario con un post o un comentario.
 * Permite asociar una reacción (LIKE) a un
 * recurso específico.
 *
 * @param postId       identificador del post al que se aplica la reacción.
 *                     - No puede ser nulo.
 * @param commentId    identificador del comentario al que se aplica la reacción
 *                     (opcional, puede ser null si la reacción es directamente
 *                     al post).
 * @param reactionType tipo de reacción aplicada (ReactionType).
 *                     - No puede ser nulo.
 *                     - Define la naturaleza de la reacción (LIKE).
 */

public record ReactionRequest(
        @NotNull(message = "Se requiere el ID de la publicación") Long postId,
        Long commentId,
        @NotNull(message = "Se requiere el tipo de reacción") ReactionType reactionType) {
}
