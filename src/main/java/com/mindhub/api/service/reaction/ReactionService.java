package com.mindhub.api.service.reaction;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mindhub.api.dto.reaction.ReactionRequest;
import com.mindhub.api.dto.reaction.ReactionResponse;
import com.mindhub.api.model.enums.ReactionType;
import com.mindhub.api.model.post.Post;
import com.mindhub.api.model.reaction.Reaction;
import com.mindhub.api.service.base.GenericService;

/**
 * Servicio para la gestión de reacciones en publicaciones.
 *
 * Define operaciones para crear, actualizar, eliminar y consultar
 * reacciones, incluyendo validaciones de permisos y privacidad.
 */

public interface ReactionService extends GenericService<Reaction, Long> {

    /**
     * Crea o actualiza una reacción a un post.
     * 
     * @param request Datos de la reacción
     * @return Reacción creada o actualizada
     */
    ReactionResponse createOrUpdateReaction(ReactionRequest request);

    /**
     * Elimina una reacción de un post.
     * 
     * @param postId ID del post
     */
    void removeReaction(Long postId);

    /**
     * Obtiene las reacciones de un post paginadas.
     * 
     * @param postId   ID del post
     * @param pageable Configuración de paginación
     * @return Página de reacciones
     */
    Page<ReactionResponse> getPostReactions(Long postId, Pageable pageable);

    /**
     * Cuenta el número total de reacciones de un post.
     * 
     * @param post Post a contar reacciones
     * @return Número total de reacciones
     */
    long countReactionsByPost(Post post);

    /**
     * Cuenta el número de reacciones de un tipo específico en un post.
     * 
     * @param post Post a contar reacciones
     * @param type Tipo de reacción
     * @return Número de reacciones del tipo especificado
     */
    long countReactionsByPostAndType(Post post, ReactionType type);

    /**
     * Verifica si un usuario ha reaccionado a un post.
     * 
     * @param userId ID del usuario
     * @param postId ID del post
     * @return true si el usuario ha reaccionado, false en caso contrario
     */
    boolean hasUserReactedToPost(Long userId, Long postId);

    /**
     * Obtiene la reacción del usuario actual para un post.
     * 
     * @param postId ID del post
     * @return Reacción del usuario o null si no existe
     */
    ReactionResponse getUserReactionForPost(Long postId);

    /**
     * Obtiene el resumen de reacciones por tipo para un post.
     * 
     * @param postId ID del post
     * @return Mapa con el conteo de reacciones por tipo
     */
    Map<ReactionType, Long> getReactionsSummaryByPost(Long postId);
}
