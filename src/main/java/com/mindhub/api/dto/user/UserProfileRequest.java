package com.mindhub.api.dto.user;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * DTO de request que representa los datos de perfil que un usuario puede
 * actualizar dentro de la plataforma.
 *
 * Este record se utiliza en los endpoints de perfil de usuario para modificar
 * información personal y social, como fecha de nacimiento, ocupación,
 * intereses, redes sociales, entre otros. Incluye validaciones para asegurar
 * que los campos cumplan con las restricciones de formato y longitud.
 *
 * @param birthDate   fecha de nacimiento del usuario (opcional).
 * @param occupation  ocupación o profesión del usuario.
 *                    - Opcional.
 *                    - Máximo 100 caracteres.
 * @param interests   intereses personales del usuario.
 *                    - Opcional.
 *                    - Máximo 200 caracteres.
 * @param website     sitio web personal o profesional.
 *                    - Opcional.
 *                    - Máximo 100 caracteres.
 * @param location    ubicación geográfica del usuario.
 *                    - Opcional.
 *                    - Máximo 100 caracteres.
 * @param socialMedia enlaces o identificadores de redes sociales.
 *                    - Opcional.
 *                    - Máximo 200 caracteres.
 * @param education   nivel educativo o institución académica.
 *                    - Opcional.
 *                    - Máximo 100 caracteres.
 * @param company     empresa o lugar de trabajo actual.
 *                    - Opcional.
 *                    - Máximo 100 caracteres.
 */

public record UserProfileRequest(
        LocalDate birthDate,
        @Size(max = 100, message = "La ocupación debe tener menos de 100 caracteres") String occupation,
        @Size(max = 200, message = "Los intereses deben tener menos de 200 caracteres") String interests,
        @Size(max = 100, message = "El sitio web debe tener menos de 100 caracteres") String website,
        @Size(max = 100, message = "La ubicación debe tener menos de 100 caracteres") String location,
        @Size(max = 200, message = "Las redes sociales deben tener menos de 200 caracteres") String socialMedia,
        @Size(max = 100, message = "La formación académica debe tener menos de 100 caracteres") String education,
        @Size(max = 100, message = "La empresa debe tener menos de 100 caracteres") String company) {
}
