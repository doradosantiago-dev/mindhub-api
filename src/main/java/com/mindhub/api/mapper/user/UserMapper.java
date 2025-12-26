package com.mindhub.api.mapper.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.mindhub.api.dto.auth.UserRegisterRequest;
import com.mindhub.api.dto.auth.UserResponse;
import com.mindhub.api.model.user.User;

/**
 * Mapper de MapStruct encargado de transformar entre entidades User y los DTOs
 * relacionados con la autenticación y gestión de usuarios.
 *
 * Define las reglas de conversión necesarias para transformar un
 * UserRegisterRequest en una entidad User lista para persistir,
 * y para convertir una entidad User en un UserResponse con los datos públicos.
 *
 * Se ignoran los campos que son gestionados automáticamente por la base de
 * datos o que no deben establecerse manualmente durante el proceso de registro.
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    /**
     * Convierte un DTO de registro en una entidad User.
     *
     * Los campos ignorados son gestionados automáticamente por la lógica
     * de negocio o la base de datos (como fechas, relaciones y banderas de estado).
     *
     * @param request DTO con los datos de registro del usuario
     * @return entidad User lista para persistir
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "lastActivityDate", ignore = true)
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "reactions", ignore = true)
    @Mapping(target = "reports", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    @Mapping(target = "chatBotMessages", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "privacyType", ignore = true)
    User toEntity(UserRegisterRequest request);

    /**
     * Convierte una entidad User en un DTO de respuesta.
     *
     * Se extraen datos anidados desde el objeto profile para exponer
     * información adicional del usuario.
     *
     * @param user entidad User a convertir
     * @return DTO UserResponse con los datos públicos del usuario
     */
    @Mapping(source = "profile.birthDate", target = "birthDate")
    @Mapping(source = "profile.occupation", target = "occupation")
    @Mapping(source = "profile.interests", target = "interests")
    @Mapping(source = "profile.website", target = "website")
    @Mapping(source = "profile.location", target = "location")
    @Mapping(source = "profile.socialMedia", target = "socialMedia")
    @Mapping(source = "profile.education", target = "education")
    @Mapping(source = "profile.workplace", target = "company")
    UserResponse toResponse(User user);

}
