package com.mindhub.api.exception;

/**
 * Excepción personalizada para manejar casos en los que un reporte
 * no es encontrado en la plataforma.
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
public class ReportNotFoundException extends RuntimeException {

    /**
     * Constructor que recibe un mensaje descriptivo del error.
     *
     * @param message descripción del motivo por el cual no se encontró el reporte
     */
    public ReportNotFoundException(String message) {
        super(message);
    }
}
