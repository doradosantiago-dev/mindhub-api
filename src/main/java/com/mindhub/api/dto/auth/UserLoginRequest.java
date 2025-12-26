package com.mindhub.api.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de request que representa las credenciales de inicio de sesión
 * de un usuario en la plataforma.
 *
 * Este record se utiliza en el proceso de autenticación para recibir
 * el nombre de usuario y la contraseña ingresados por el cliente.
 * Incluye validaciones para asegurar que los campos no estén vacíos
 * y cumplan con las restricciones de longitud.
 *
 * @param username nombre de usuario del cliente que intenta iniciar sesión.
 *                 - No puede estar en blanco.
 *                 - Debe tener un máximo de 20 caracteres.
 * @param password contraseña del cliente que intenta iniciar sesión.
 *                 - No puede estar en blanco.
 *                 - Debe tener al menos 6 caracteres.
 */

public record UserLoginRequest(
        @NotBlank(message = "El nombre de usuario es obligatorio") @Size(max = 20, message = "El nombre de usuario debe tener menos de 20 caracteres") String username,

        @NotBlank(message = "La contraseña es obligatoria") @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres") String password) {
}
