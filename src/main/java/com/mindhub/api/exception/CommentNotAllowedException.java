package com.mindhub.api.exception;

/**
 * Excepción personalizada para manejar casos en los que no se permite
 * realizar un comentario en la plataforma.
 *
 * Esta excepción extiende de RuntimeException, lo que la convierte
 * en una excepción no verificada (unchecked). Se utiliza en escenarios de
 * negocio donde un usuario intenta comentar en un recurso en el que no
 * tiene permisos o cuando las condiciones de la aplicación no lo permiten.
 *
 * Esta excepción debe ser capturada y gestionada
 * por un manejador global de excepciones para devolver una respuesta
 * adecuada al cliente.
 *
 */

public class CommentNotAllowedException extends RuntimeException {

    /**
     * Constructor que recibe un mensaje descriptivo del error.
     *
     * @param message descripción del motivo por el cual no se permite el comentario
     */
    public CommentNotAllowedException(String message) {
        super(message);
    }
}
