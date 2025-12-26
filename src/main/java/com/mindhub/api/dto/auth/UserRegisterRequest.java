package com.mindhub.api.dto.auth;


import com.mindhub.api.model.enums.PrivacyType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de request que representa los datos necesarios para registrar
 * un nuevo usuario en la plataforma.
 *
 * Este record se utiliza en el proceso de registro para recibir la
 * información básica de un usuario, incluyendo credenciales, datos
 * personales, de contacto y configuración inicial de privacidad.
 *
 * Incluye validaciones para asegurar que los campos obligatorios
 * estén presentes y cumplan con las restricciones de formato y longitud.
 *
 * @param username       nombre de usuario único en la plataforma.
 *                       - No puede estar en blanco.
 *                       - Debe tener entre 3 y 20 caracteres.
 * @param password       contraseña de acceso del usuario.
 *                       - No puede estar en blanco.
 *                       - Debe tener al menos 6 caracteres.
 * @param firstName      nombre del usuario.
 *                       - No puede estar en blanco.
 *                       - Máximo 50 caracteres.
 * @param lastName       apellidos del usuario.
 *                       - No puede estar en blanco.
 *                       - Máximo 100 caracteres.
 * @param email          dirección de correo electrónico del usuario.
 *                       - No puede estar en blanco.
 *                       - Debe tener un formato válido de email.
 * @param phone          número de teléfono de contacto (opcional, máximo 20
 *                       caracteres).
 * @param profilePicture URL de la foto de perfil (opcional, máximo 1000
 *                       caracteres).
 * @param address        dirección física del usuario (opcional, máximo 200
 *                       caracteres).
 * @param biography      biografía o descripción personal (opcional, máximo 500
 *                       caracteres).
 * @param privacyType    nivel de privacidad del perfil (PrivacyType).
 */

public record UserRegisterRequest(
        @NotBlank(message = "Se requiere nombre de usuario") @Size(min = 3, max = 20, message = "El nombre de usuario debe tener entre 3 y 20 caracteres") String username,

        @NotBlank(message = "Se requiere contraseña") @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres") String password,

        @NotBlank(message = "Se requiere el nombre") @Size(max = 50, message = "El nombre debe tener menos de 50 caracteres") String firstName,

        @NotBlank(message = "Se requiere el apellido") @Size(max = 100, message = "El apellido debe tener menos de 100 caracteres") String lastName,

        @NotBlank(message = "Se requiere correo electrónico") @Email(message = "Formato de correo electrónico no válido") String email,

        @Size(max = 20, message = "El número de teléfono debe tener menos de 20 caracteres") String phone,

        @Size(max = 1000, message = "La URL de la foto de perfil debe tener menos de 1000 caracteres") String profilePicture,

        @Size(max = 200, message = "La dirección debe tener menos de 200 caracteres") String address,

        @Size(max = 500, message = "La biografía debe tener menos de 500 caracteres") String biography,

        PrivacyType privacyType) {
}
