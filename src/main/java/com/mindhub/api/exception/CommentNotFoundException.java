package com.mindhub.api.exception;

/**
 * Excepción personalizada para manejar casos en los que un comentario
 * no es encontrado en la plataforma.
 *
 * Esta excepción extiende de RuntimeException, lo que la convierte
 * en una excepción no verificada (unchecked). Se utiliza en escenarios de
 * negocio donde se intenta acceder a un comentario inexistente en la base
 * de datos o que ya ha sido eliminado.
 *
 * Esta excepción debe ser capturada y gestionada
 * por un manejador global de excepciones para devolver una respuesta
 * adecuada al cliente.
 *
 */

public class CommentNotFoundException extends RuntimeException {

    /**
     * Constructor que recibe un mensaje descriptivo del error.
     *
     * @param message descripción del motivo por el cual no se encontró el
     *                comentario
     */
    public CommentNotFoundException(String message) {
        super(message);
    }
}
