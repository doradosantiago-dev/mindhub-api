package com.mindhub.api.dto.user;


import com.mindhub.api.model.enums.PrivacyType;

import jakarta.validation.constraints.Size;

/**
 * DTO de request que representa los datos que un usuario puede actualizar
 * en su propio perfil dentro de la plataforma.
 *
 * Este record se utiliza en los endpoints de usuario para modificar
 * información personal, de contacto, biografía y nivel de privacidad.
 * Incluye validaciones para asegurar que los campos cumplan con las
 * restricciones de formato y longitud.
 *
 * @param firstName      nombre del usuario.
 *                       - Opcional.
 *                       - Máximo 50 caracteres.
 * @param lastName       apellidos del usuario.
 *                       - Opcional.
 *                       - Máximo 100 caracteres.
 * @param email          dirección de correo electrónico del usuario.
 *                       - Opcional.
 *                       - No tiene validación de formato aquí (se asume
 *                       validación en otra capa).
 * @param phone          número de teléfono de contacto.
 *                       - Opcional.
 *                       - Máximo 20 caracteres.
 * @param profilePicture URL de la foto de perfil.
 *                       - Opcional.
 * @param address        dirección física del usuario.
 *                       - Opcional.
 *                       - Máximo 200 caracteres.
 * @param biography      biografía o descripción personal.
 *                       - Opcional.
 *                       - Máximo 500 caracteres.
 * @param privacyType    nivel de privacidad del perfil (PrivacyType).
 *                       - Opcional.
 *                       - Define si el perfil es público, privado o visible
 *                       solo para amigos.
 */

public record UserUpdateRequest(
                @Size(max = 50, message = "El nombre debe tener menos de 50 caracteres") String firstName,
                @Size(max = 100, message = "El apellido debe tener menos de 100 caracteres") String lastName,
                String email,
                @Size(max = 20, message = "El número de teléfono debe tener menos de 20 caracteres") String phone,
                String profilePicture,
                @Size(max = 200, message = "La dirección debe tener menos de 200 caracteres") String address,
                @Size(max = 500, message = "La biografía debe tener menos de 500 caracteres") String biography,
                PrivacyType privacyType) {
}
