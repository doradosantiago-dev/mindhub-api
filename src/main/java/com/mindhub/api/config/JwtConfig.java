package com.mindhub.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Clase de configuración para la gestión de parámetros relacionados con JWT
 * (JSON Web Token) en la aplicación.
 *
 * Centraliza la configuración de seguridad asociada a la generación
 * y validación de tokens JWT, incluyendo la clave secreta utilizada
 * para la firma y el tiempo de expiración.
 */

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    /**
     * Clave secreta utilizada para firmar y validar los tokens JWT.
     * 
     * Debe mantenerse en un lugar seguro y no exponerse en el código fuente.
     * Lo recomendable es definirla como variable de entorno en
     * application.properties y referenciarla
     * en el archivo de configuración.
     * 
     */
    private String secret = "mySecretKeyThatIsLongEnoughForHS256AlgorithmAndSecureEnoughForProduction";

    /**
     * Tiempo de expiración de los tokens JWT en milisegundos.
     * 
     * Por defecto se establece en 86.400.000 ms (24 horas).
     * 
     */
    private long expiration = 86400000;
}
