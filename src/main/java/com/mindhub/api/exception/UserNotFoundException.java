package com.mindhub.api.exception;

/**
 * Excepción personalizada para manejar casos en los que un usuario
 * no es encontrado en la plataforma.
 *
 * Esta excepción extiende de RuntimeException, lo que la convierte
 * en una excepción no verificada (unchecked). Se utiliza en escenarios de
 * negocio donde se intenta acceder a un usuario inexistente en la base de datos
 * o que ya ha sido eliminado.
 *
 * Esta excepción debe ser capturada y gestionada
 * por el GlobalExceptionHandler para devolver una respuesta
 * adecuada al cliente.
 *
 */
public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor que recibe un mensaje descriptivo del error.
     *
     * @param message descripción del motivo por el cual no se encontró el usuario
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
