package com.mindhub.api.mapper.comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.mindhub.api.dto.comment.CommentRequest;
import com.mindhub.api.dto.comment.CommentResponse;
import com.mindhub.api.model.comment.Comment;
import com.mindhub.api.mapper.user.UserMapper; // Importación necesaria

/**
 * Mapper de MapStruct encargado de transformar entre entidades Comment y sus
 * DTOs asociados (CommentRequest y CommentResponse).
 *
 * Define las reglas de conversión necesarias para transformar un CommentRequest
 * en una entidad Comment lista para persistir,
 * y para convertir una entidad Comment en un CommentResponse que se expone a
 * través de la API.
 *
 * Se ignoran los campos que son gestionados automáticamente por la base de
 * datos o que deben asignarse mediante la lógica de negocio,
 * como el autor del comentario y la publicación asociada.
 */
@Mapper(componentModel = "spring", 
        unmappedTargetPolicy = ReportingPolicy.IGNORE, 
        uses = { UserMapper.class }) // Se añade 'uses' para resolver la dependencia de UserMapper
public interface CommentMapper {

    /**
     * Convierte un DTO CommentRequest en una entidad Comment.
     *
     * Se ignoran campos como id, creationDate, updateDate, author y post,
     * ya que son gestionados automáticamente o asignados en la lógica de negocio.
     *
     * @param request DTO con los datos del comentario
     * @return entidad Comment lista para persistir
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "post", ignore = true)
    Comment toEntity(CommentRequest request);

    /**
     * Convierte una entidad Comment en un DTO CommentResponse.
     *
     * El campo updateDate se transforma en editDate, el identificador de la
     * publicación (post.id) se asigna a postId,
     * y el autor del comentario se convierte automáticamente utilizando el UserMapper configurado en 'uses'.
     *
     * @param comment entidad Comment a convertir
     * @return CommentResponse con los datos del comentario
     */
    @Mapping(source = "updateDate", target = "editDate")
    @Mapping(target = "postId", source = "post.id")
    // Se elimina la expresión manual java(...) para evitar errores de compilación en target
    CommentResponse toResponse(Comment comment);

}