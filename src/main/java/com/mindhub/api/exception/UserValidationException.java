package com.mindhub.api.exception;

/**
 * Excepción personalizada para manejar casos en los que la validación
 * de datos de un usuario falla en la plataforma.
 *
 * Esta excepción extiende de RuntimeException, lo que la convierte
 * en una excepción no verificada (unchecked). Se utiliza en escenarios de
 * negocio donde los datos de un usuario no cumplen con las reglas de validación
 * definidas.
 *
 * Esta excepción debe ser capturada y gestionada
 * por el GlobalExceptionHandler para devolver una respuesta
 * adecuada al cliente.
 *
 */
public class UserValidationException extends RuntimeException {

    /**
     * Constructor que recibe un mensaje descriptivo del error.
     *
     * @param message descripción del motivo por el cual falló la validación del
     *                usuario
     */
    public UserValidationException(String message) {
        super(message);
    }

    /**
     * Constructor que recibe un mensaje descriptivo y la causa original.
     *
     * @param message descripción del motivo del error
     * @param cause   excepción original que causó este error
     */
    public UserValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
