package com.mindhub.api.mapper.post;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mindhub.api.dto.post.PostCreateRequest;
import com.mindhub.api.dto.post.PostResponse;
import com.mindhub.api.mapper.user.UserMapper;
import com.mindhub.api.model.post.Post;

/**
 * Mapper de MapStruct encargado de transformar entre entidades Post y sus DTOs
 * asociados (PostCreateRequest y PostResponse).
 *
 * Define las reglas de conversión necesarias para transformar un
 * PostCreateRequest en una entidad Post lista para persistir,
 * y para convertir una entidad Post en un PostResponse que se expone a través
 * de la API.
 *
 * Se ignoran los campos gestionados automáticamente por la base de datos o que
 * deben asignarse mediante la lógica de negocio,
 * como el autor, los comentarios, las reacciones y los reportes.
 */

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface PostMapper {

    /**
     * Convierte un DTO PostCreateRequest en una entidad Post.
     *
     * Se ignoran campos como id, creationDate, updateDate, author, comments,
     * reactions y reports, ya que son gestionados automáticamente o asignados
     * en la lógica de negocio. El campo privacyType se mapea directamente.
     *
     * @param request DTO con los datos de creación del post
     * @return entidad Post lista para persistir
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "reactions", ignore = true)
    @Mapping(target = "reports", ignore = true)
    @Mapping(source = "privacyType", target = "privacyType")
    Post toEntity(PostCreateRequest request);

    /**
     * Convierte una entidad Post en un DTO PostResponse.
     *
     * El campo privacyType se mapea directamente. Los campos commentCount y
     * likeCount se inicializan en 0,
     * ya que se espera que sean calculados posteriormente en la lógica de negocio.
     * El campo author se transforma utilizando UserMapper.
     *
     * @param post entidad Post a convertir
     * @return PostResponse con los datos del post
     */
    @Mapping(source = "privacyType", target = "privacyType")
    @Mapping(target = "commentCount", constant = "0")
    @Mapping(target = "likeCount", constant = "0")
    @Mapping(source = "author", target = "author")
    PostResponse toResponse(Post post);

}
