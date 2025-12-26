package com.mindhub.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de autenticación JWT para Spring Security.
 *
 * Este filtro se ejecuta una vez por cada petición HTTP y se encarga de extraer
 * el token JWT del encabezado Authorization,
 * validarlo utilizando JwtService, recuperar los detalles del usuario mediante
 * CustomUserDetailsService,
 * y establecer la autenticación en el contexto de seguridad si el token es
 * válido.
 *
 * Si el token no existe, es inválido o está vacío, la petición continúa sin
 * autenticación y será rechazada
 * en los endpoints que requieran seguridad.
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /** Servicio para operaciones relacionadas con JWT (extracción y validación). */
    private final JwtService jwtService;
    /** Servicio personalizado para cargar usuarios desde la base de datos. */
    private final CustomUserDetailsService userDetailsService;

    /**
     * Lógica principal del filtro JWT.
     *
     * Verifica si la cabecera Authorization contiene un token válido, extrae el
     * nombre de usuario del token,
     * carga los detalles del usuario y valida el token. Si el token es válido,
     * establece la autenticación
     * en el contexto de seguridad.
     *
     * @param request     petición HTTP entrante
     * @param response    respuesta HTTP saliente
     * @param filterChain cadena de filtros de seguridad
     * @throws ServletException en caso de error de servlet
     * @throws IOException      en caso de error de entrada/salida
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        log.debug("Procesando petición en JwtAuthenticationFilter, URI: {}", request.getRequestURI());

        final String authHeader = request.getHeader("Authorization");

        // Si no hay cabecera o no empieza con "Bearer ", continuar sin autenticación
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);

            return;
        }

        try {

            // Extraer el token eliminando el prefijo "Bearer "
            log.warn("Token JWT vacío tras limpiar espacios");
            String jwt = authHeader.substring(7).trim();

            if (jwt.isEmpty()) {
                log.warn("Token JWT vacío tras eliminar los espacios");

                filterChain.doFilter(request, response);

                return;
            }

            // Extraer el nombre de usuario del token
            String username = jwtService.extractUsername(jwt);
            log.debug("Usuario extraído del token: {}", username);

            // Validar si no hay autenticación previa en el contexto
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // Validar el token con los detalles del usuario
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));

                    // Establecer autenticación en el contexto de seguridad
                    log.info("Autenticación establecida en el contexto para usuario: {}", username);

                    SecurityContextHolder.getContext().setAuthentication(authToken);

                } else {
                    log.warn("“Token JWT no válido para el usuario: {}", username);
                }
            }
        } catch (Exception e) {
            log.error("Error al procesar el token JWT.: {}", e.getMessage());

        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
