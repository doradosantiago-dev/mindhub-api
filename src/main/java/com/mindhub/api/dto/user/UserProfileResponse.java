package com.mindhub.api.dto.user;

import java.time.LocalDate;

/**
 * DTO de respuesta que representa el perfil público de un usuario.
 *
 * Este record se utiliza para devolver información detallada del perfil
 * de un usuario en la API, sin exponer datos sensibles como contraseñas
 * o tokens de autenticación.
 * 
 * Incluye datos personales básicos, información profesional y social,
 * así como metadatos de creación y actualización del perfil.
 *
 * @param id           identificador único del usuario
 * @param dateOfBirth  fecha de nacimiento del usuario
 * @param occupation   ocupación o profesión actual
 * @param interests    intereses personales declarados por el usuario
 * @param website      sitio web personal o profesional
 * @param location     ubicación geográfica del usuario
 * @param socialMedia  enlaces o identificadores de redes sociales
 * @param education    nivel educativo o institución académica
 * @param company      empresa o lugar de trabajo actual
 * @param creationDate fecha de creación del perfil en la plataforma
 * @param updateDate   fecha de última actualización del perfil
 *
 */
public record UserProfileResponse(
        Long id,
        LocalDate dateOfBirth,
        String occupation,
        String interests,
        String website,
        String location,
        String socialMedia,
        String education,
        String company,
        LocalDate creationDate,
        LocalDate updateDate) {
}
