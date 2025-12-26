package com.mindhub.api.exception;

/**
 * Excepción personalizada para manejar casos en los que un usuario
 * no tiene permitido visualizar un comentario en la plataforma.
 *
 * Esta excepción extiende de RuntimeException, lo que la convierte
 * en una excepción no verificada (unchecked). Se utiliza en escenarios de
 * negocio donde las reglas de privacidad, permisos o configuraciones de
 * visibilidad impiden que un usuario acceda a un comentario específico.
 *
 * Esta excepción debe ser capturada y gestionada
 * por un manejador global de excepciones para devolver una respuesta
 * adecuada al cliente.
 *
 */

public class CommentViewNotAllowedException extends RuntimeException {

    /**
     * Constructor que recibe un mensaje descriptivo del error.
     *
     * @param message descripción del motivo por el cual no se permite
     *                visualizar el comentario
     */
    public CommentViewNotAllowedException(String message) {
        super(message);
    }
}
