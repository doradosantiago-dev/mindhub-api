package com.mindhub.api.exception;

/**
 * Excepción personalizada para manejar errores relacionados con las
 * operaciones de seguimiento (follow) entre usuarios en la plataforma.
 *
 * Esta excepción extiende de RuntimeException, lo que la convierte
 * en una excepción no verificada (unchecked). Se utiliza en escenarios de
 * negocio donde ocurren problemas al intentar seguir o dejar de seguir
 * a un usuario, como restricciones de permisos, inconsistencias en la
 * lógica de negocio o errores inesperados.
 *
 * Esta excepción debe ser capturada y gestionada
 * por un manejador global de excepciones para devolver una respuesta
 * adecuada al cliente.
 *
 */

public class FollowException extends RuntimeException {

    /**
     * Constructor que recibe un mensaje descriptivo del error.
     *
     * @param message descripción del motivo del error en la operación de follow
     */
    public FollowException(String message) {
        super(message);
    }

}
