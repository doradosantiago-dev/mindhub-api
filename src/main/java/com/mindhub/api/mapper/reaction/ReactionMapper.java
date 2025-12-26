package com.mindhub.api.mapper.reaction;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mindhub.api.dto.reaction.ReactionRequest;
import com.mindhub.api.dto.reaction.ReactionResponse;
import com.mindhub.api.model.reaction.Reaction;

/**
 * Mapper de MapStruct encargado de transformar entre entidades Reaction y sus
 * DTOs asociados (ReactionRequest y ReactionResponse).
 *
 * Define las reglas de conversión necesarias para transformar una entidad
 * Reaction en un DTO ReactionResponse,
 * y para convertir un DTO ReactionRequest en una entidad Reaction lista para
 * persistir.
 *
 * Se ignoran los campos gestionados automáticamente por la base de datos o que
 * deben asignarse mediante la lógica de negocio,
 * como el usuario y la publicación asociada.
 */

@Mapper(componentModel = "spring")
public interface ReactionMapper {

    /**
     * Convierte una entidad Reaction en un DTO ReactionResponse.
     *
     * El campo post.id se transforma en postId, el campo type se asigna a
     * reactionType,
     * y el campo user se omite para evitar exponer información sensible.
     *
     * @param reaction entidad Reaction a convertir
     * @return ReactionResponse con los datos de la reacción
     */
    @Mapping(target = "postId", source = "post.id")
    @Mapping(source = "type", target = "reactionType")
    @Mapping(target = "user", ignore = true)
    ReactionResponse toResponse(Reaction reaction);

    /**
     * Convierte un DTO ReactionRequest en una entidad Reaction.
     *
     * Se omiten los campos como id, creationDate, user y post, ya que son
     * gestionados automáticamente
     * o asignados mediante la lógica de negocio. El campo reactionType del request
     * se mapea al campo type de la entidad.
     *
     * @param request DTO con los datos de la reacción
     * @return entidad Reaction lista para persistir
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(source = "reactionType", target = "type")
    Reaction toEntity(ReactionRequest request);
}
