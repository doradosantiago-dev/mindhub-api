package com.mindhub.api.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.mindhub.api.config.JwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio para generar, validar y extraer información de tokens JWT.
 *
 * Usa la configuración de JwtConfig para la clave secreta y el tiempo
 * de expiración. Se integra con JwtAuthenticationFilter para validar
 * peticiones entrantes.
 */

@Slf4j
@Service
public class JwtService {

    /** Configuración de JWT (clave secreta y expiración). */
    private final JwtConfig jwtConfig;

    public JwtService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    /**
     * Extrae el nombre de usuario (subject) de un token JWT.
     *
     * @param token token JWT
     * @return nombre de usuario contenido en el token
     */
    public String extractUsername(String token) {
        log.debug("Extrayendo username del token");

        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae un claim específico del token.
     *
     * @param token          token JWT
     * @param claimsResolver función que resuelve el claim deseado
     * @param <T>            tipo del claim
     * @return valor del claim solicitado
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        log.trace("Extrayendo claim del token");

        final Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    /**
     * Genera un token JWT para un usuario autenticado.
     *
     * @param userDetails detalles del usuario
     * @return token JWT firmado
     */
    public String generateToken(UserDetails userDetails) {
        log.debug("Generando token para usuario: {}", userDetails.getUsername());

        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Genera un token JWT con claims adicionales.
     *
     * @param extraClaims claims extra a incluir en el token
     * @param userDetails detalles del usuario
     * @return token JWT firmado
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        log.trace("Generando token con claims extra para {}", userDetails.getUsername());

        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Valida un token JWT contra los detalles de un usuario.
     *
     * @param token       token JWT
     * @param userDetails detalles del usuario
     * @return true si el token es válido, false en caso contrario
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        log.debug("Validando token para usuario: {}", userDetails.getUsername());

        final String username = extractUsername(token);

        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Verifica si un token ha expirado.
     */
    private boolean isTokenExpired(String token) {
        log.trace("Comprobando expiración del token");

        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrae la fecha de expiración de un token.
     */
    private Date extractExpiration(String token) {
        log.trace("Extrayendo fecha de expiración del token");

        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae todos los claims de un token JWT.
     */
    private Claims extractAllClaims(String token) {
        log.trace("Parseando claims del token");

        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Obtiene la clave secreta para firmar y validar tokens.
     *
     * @return clave secreta en formato HMAC-SHA
     */
    private javax.crypto.SecretKey getSignInKey() {
        log.trace("Obteniendo clave secreta para firmar token");

        byte[] keyBytes = jwtConfig.getSecret().getBytes();

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
