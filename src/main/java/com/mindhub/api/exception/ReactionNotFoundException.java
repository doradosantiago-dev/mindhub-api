package com.mindhub.api.exception;

/**
 * Excepción personalizada para manejar casos en los que una reacción
 * no es encontrada en la plataforma.
 *
 * Esta excepción extiende de RuntimeException, lo que la convierte
 * en una excepción no verificada (unchecked). Se utiliza en escenarios de
 * negocio donde se intenta registrar un nuevo usuario con un nombre de usuario
 * que ya está en uso.
 *
 * Esta excepción debe ser capturada y gestionada
 * por el GlobalExceptionHandler para devolver una respuesta
 * adecuada al cliente.
 *
 */
public class ReactionNotFoundException extends RuntimeException {

    /**
     * Constructor que recibe un mensaje descriptivo del error.
     *
     * @param message descripción del motivo por el cual no se encontró la reacción
     */
    public ReactionNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor que recibe un mensaje descriptivo y la causa original.
     *
     * @param message descripción del motivo del error
     * @param cause   excepción original que causó este error
     */
    public ReactionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
