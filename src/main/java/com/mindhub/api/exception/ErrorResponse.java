package com.mindhub.api.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO estándar para representar respuestas de error en la API.
 *
 * Esta clase se utiliza en el manejo global de excepciones para devolver
 * información estructurada al cliente cuando ocurre un error.
 *
 * Incluye los siguientes datos:
 * timestamp: Momento en que ocurrió el error
 * status: Código de estado HTTP asociado
 * error: Tipo de error (por ejemplo, "Not Found", "Forbidden")
 * message: Descripción detallada del error
 * path: Endpoint que originó el error
 * validationErrors: Errores de validación específicos por campo
 *
 * Este objeto suele ser devuelto por un ControllerAdvice con ExceptionHandler.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private LocalDateTime timestamp;

    private int status;

    private String error;

    private String message;

    private String path;

    private Map<String, String> validationErrors;
}
