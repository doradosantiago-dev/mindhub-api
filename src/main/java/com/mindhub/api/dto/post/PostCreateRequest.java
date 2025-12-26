package com.mindhub.api.dto.post;

import com.mindhub.api.model.enums.PrivacyType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de request que representa la información necesaria para crear
 * un nuevo post en la plataforma.
 *
 * Este record se utiliza en los endpoints de publicaciones para recibir
 * el contenido del post, una imagen opcional y el nivel de privacidad
 * configurado por el autor. Incluye validaciones para asegurar que los
 * campos requeridos estén presentes y cumplan con las restricciones
 * de formato y longitud.
 *
 * @param content     contenido textual del post.
 *                    - No puede estar en blanco.
 *                    - Debe tener un máximo de 1000 caracteres.
 * @param imageUrl    URL de la imagen asociada al post (opcional).
 *                    - Máximo 1000 caracteres.
 * @param privacyType nivel de privacidad del post (PrivacyType).
 *                    - No puede ser nulo.
 *                    - Define si el post es público, privado, visible solo
 *                    para amigos, etc.
 */

public record PostCreateRequest(
        @NotBlank(message = "Se requiere contenido") @Size(max = 1000, message = "El contenido debe tener menos de 1000 caracteres") String content,
        @Size(max = 1000, message = "La URL de la imagen debe tener menos de 1000 caracteres") String imageUrl,
        @NotNull(message = "Se requiere el tipo de privacidad") PrivacyType privacyType) {
}
