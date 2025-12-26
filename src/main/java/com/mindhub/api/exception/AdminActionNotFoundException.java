package com.mindhub.api.exception;

/**
 * Excepción personalizada para manejar casos en los que una acción
 * administrativa
 * no es encontrada en el sistema.
 *
 * Esta excepción extiende de RuntimeException, lo que significa que es
 * una excepción no verificada (unchecked). Se utiliza típicamente en servicios
 * o repositorios cuando no se encuentra una entidad AdminAction.
 *
 */

public class AdminActionNotFoundException extends RuntimeException {

    /**
     * Constructor que recibe un mensaje descriptivo del error.
     *
     * @param message descripción del error
     */
    public AdminActionNotFoundException(String message) {
        super(message);
    }

}
