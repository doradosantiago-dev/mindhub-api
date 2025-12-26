package com.mindhub.api.exception;

/**
 * Excepción personalizada para manejar casos en los que una notificación
 * no es encontrada en la plataforma.
 *
 * Esta excepción extiende de RuntimeException, lo que la convierte
 * en una excepción no verificada (unchecked). Se utiliza en escenarios de
 * negocio donde se intenta acceder a una notificación inexistente en la base
 * de datos o que ya ha sido eliminada.
 *
 * Esta excepción debe ser capturada y gestionada
 * por el GlobalExceptionHandler para devolver una respuesta
 * adecuada al cliente.
 *
 */
public class NotificationNotFoundException extends RuntimeException {

    /**
     * Constructor que recibe un mensaje descriptivo del error.
     *
     * @param message descripción del motivo por el cual no se encontró la
     *                notificación
     */
    public NotificationNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor que recibe un mensaje descriptivo y la causa original.
     *
     * @param message descripción del motivo del error
     * @param cause   excepción original que causó este error
     */
    public NotificationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
