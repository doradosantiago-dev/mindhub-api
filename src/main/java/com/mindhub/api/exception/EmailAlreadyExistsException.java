package com.mindhub.api.exception;

/**
 * Excepción personalizada para manejar casos en los que se intenta registrar
 * o actualizar un usuario con un correo electrónico que ya existe en el
 * sistema.
 *
 * Esta excepción extiende de RuntimeException, lo que la convierte
 * en una excepción no verificada (unchecked). Se utiliza en escenarios de
 * negocio donde la unicidad del correo electrónico es un requisito y se
 * detecta un conflicto.
 *
 * Esta excepción debe ser capturada y gestionada
 * por un manejador global de excepciones para devolver una respuesta
 * adecuada al cliente.
 *
 */

public class EmailAlreadyExistsException extends RuntimeException {

    /**
     * Constructor que recibe un mensaje descriptivo del error.
     *
     * @param message descripción del motivo por el cual el correo electrónico ya
     *                existe
     */
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
