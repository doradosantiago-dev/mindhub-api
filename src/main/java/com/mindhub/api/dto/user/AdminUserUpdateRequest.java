package com.mindhub.api.dto.user;


import com.mindhub.api.model.enums.PrivacyType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de request que representa los datos que un administrador puede
 * actualizar en el perfil de un usuario dentro de la plataforma.
 *
 * Este record se utiliza en los endpoints de administración para modificar
 * información de un usuario, como sus datos personales, de contacto,
 * biografía y nivel de privacidad. Incluye validaciones para asegurar
 * que los campos cumplan con las restricciones de formato y longitud.
 *
 * @param username       nombre de usuario único en el sistema.
 *                       - Opcional.
 *                       - Mínimo 3 caracteres, máximo 30 caracteres.
 * @param firstName      nombre del usuario.
 *                       - Opcional.
 *                       - Máximo 50 caracteres.
 * @param lastName       apellidos del usuario.
 *                       - Opcional.
 *                       - Máximo 100 caracteres.
 * @param email          dirección de correo electrónico del usuario.
 *                       - Opcional.
 *                       - Debe tener un formato válido de email.
 * @param phone          número de teléfono de contacto.
 *                       - Opcional.
 *                       - Máximo 20 caracteres.
 * @param profilePicture URL de la foto de perfil.
 *                       - Opcional.
 *                       - Máximo 1000 caracteres.
 * @param address        dirección física del usuario.
 *                       - Opcional.
 *                       - Máximo 200 caracteres.
 * @param biography      biografía o descripción personal.
 *                       - Opcional.
 *                       - Máximo 500 caracteres.
 * @param privacyType    nivel de privacidad del perfil (PrivacyType).
 *                       - No puede ser nulo.
 *                       - Define si el perfil es público, privado o visible
 *                       solo para amigos.
 * @param roleName       nombre del rol del usuario.
 *                       - Opcional.
 *                       - Permite cambiar el rol del usuario (USER o ADMIN).
 */

public record AdminUserUpdateRequest(
        @Size(min = 3, max = 30, message = "El nombre de usuario debe tener entre 3 y 30 caracteres") String username,
        @Size(max = 50, message = "El nombre debe tener menos de 50 caracteres") String firstName,
        @Size(max = 100, message = "El apellido debe tener menos de 100 caracteres") String lastName,
        @Email(message = "Formato de correo electrónico no válido") String email,
        @Size(max = 20, message = "El número de teléfono debe tener menos de 20 caracteres") String phone,
        @Size(max = 1000, message = "La foto de perfil debe tener menos de 1000 caracteres") String profilePicture,
        @Size(max = 200, message = "La dirección debe tener menos de 200 caracteres") String address,
        @Size(max = 500, message = "La biografía debe tener menos de 500 caracteres") String biography,
        @NotNull(message = "Se requiere el tipo de privacidad") PrivacyType privacyType,
        String roleName) {
}
