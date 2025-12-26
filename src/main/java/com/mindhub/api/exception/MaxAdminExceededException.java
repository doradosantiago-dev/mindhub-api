package com.mindhub.api.exception;

/**
 * Excepción personalizada para manejar casos en los que se supera
 * el número máximo permitido de administradores en la plataforma.
 *
 * Esta excepción extiende de RuntimeException, lo que la convierte
 * en una excepción no verificada (unchecked). Se utiliza en escenarios de
 * negocio donde existe una restricción en la cantidad de administradores
 * que pueden registrarse o asignarse en el sistema.
 *
 * Esta excepción debe ser capturada y gestionada
 * por el GlobalExceptionHandler para devolver una respuesta
 * adecuada al cliente.
 *
 */
public class MaxAdminExceededException extends RuntimeException {

    /**
     * Constructor que recibe un mensaje descriptivo del error.
     *
     * @param message descripción del motivo por el cual se superó
     *                el número máximo de administradores
     */
    public MaxAdminExceededException(String message) {
        super(message);
    }
}
