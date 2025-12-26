package com.mindhub.api.dto.auth;

import java.time.LocalDate;

import com.mindhub.api.model.enums.PrivacyType;
import com.mindhub.api.model.role.Role;


/**
 * DTO de respuesta que representa la información pública y de perfil
 * de un usuario en la plataforma.
 *
 * Este record se utiliza para devolver datos de un usuario en distintos
 * contextos de la API (autenticación, perfiles, listados, etc.), sin exponer
 * información sensible como contraseñas o tokens.
 *
 * Incluye datos de identificación, contacto, perfil social, rol asignado,
 * configuración de privacidad y metadatos de actividad.
 *
 * @param id               identificador único del usuario
 * @param username         nombre de usuario (único en la plataforma)
 * @param firstName        nombre del usuario
 * @param lastName         apellidos del usuario
 * @param email            dirección de correo electrónico
 * @param phone            número de teléfono de contacto
 * @param profilePicture   URL de la foto de perfil
 * @param address          dirección física (si el usuario la proporciona)
 * @param biography        biografía o descripción personal
 * @param role             rol asignado al usuario ({@link Role})
 * @param privacyType      nivel de privacidad del perfil ({@link PrivacyType})
 * @param active           indicador booleano que señala si la cuenta está
 *                         activa
 * @param registrationDate fecha de registro en la plataforma
 * @param lastActivityDate fecha de la última actividad registrada
 * @param birthDate        fecha de nacimiento (en formato String para
 *                         flexibilidad de parseo)
 * @param occupation       ocupación o profesión
 * @param interests        intereses personales
 * @param website          sitio web personal o profesional
 * @param location         ubicación geográfica
 * @param socialMedia      enlaces o identificadores de redes sociales
 * @param education        nivel educativo o institución académica
 * @param company          empresa o lugar de trabajo actual
 *
 */
public record UserResponse(
        Long id,
        String username,
        String firstName,
        String lastName,
        String email,
        String phone,
        String profilePicture,
        String address,
        String biography,
        Role role,
        PrivacyType privacyType,
        Boolean active,
        LocalDate registrationDate,
        LocalDate lastActivityDate,

        String birthDate,
        String occupation,
        String interests,
        String website,
        String location,
        String socialMedia,
        String education,
        String company) {
}
