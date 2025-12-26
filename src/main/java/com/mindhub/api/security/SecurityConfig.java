package com.mindhub.api.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.extern.slf4j.Slf4j;

/**
 * Configuración de seguridad de Spring Security para la aplicación.
 *
 * Define las reglas de acceso a los endpoints, la gestión de sesiones,
 * la configuración de CORS y los proveedores de autenticación.
 * Integra un filtro JWT personalizado para manejar la autenticación
 * basada en tokens.
 */

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /**
     * Configura la cadena de filtros de seguridad.
     *
     * - Habilita CORS con configuración personalizada. <br>
     * - Deshabilita CSRF (no necesario en APIs REST con JWT). <br>
     * - Define endpoints públicos, protegidos y de administrador. <br>
     * - Establece la política de sesión como STATELESS (sin estado). <br>
     * - Registra el proveedor de autenticación y el filtro JWT. <br>
     * 
     *
     * @param http               configuración de seguridad HTTP
     * @param jwtAuthFilter      filtro de autenticación JWT
     * @param userDetailsService servicio de usuarios para autenticación
     * @return la cadena de filtros de seguridad configurada
     * @throws Exception en caso de error de configuración
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter,
            UserDetailsService userDetailsService) throws Exception {

        log.info("Inicializando configuración de seguridad y filtros JWT");

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        // Endpoints públicos
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/api-docs/**")
                        .permitAll()
                        // Permitir acceso público a archivos estáticos subidos
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/posts/public").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/search").permitAll()

                        // Endpoints que requieren autenticación
                        .requestMatchers("/api/users/me/**").authenticated()
                        .requestMatchers("/api/posts/**").authenticated()
                        .requestMatchers("/api/comments/**").authenticated()
                        .requestMatchers("/api/reactions/**").authenticated()
                        .requestMatchers("/api/reports/**").authenticated()
                        .requestMatchers("/api/notifications/**").authenticated()
                        .requestMatchers("/api/chatbot/**").authenticated()

                        // Endpoints restringidos a administradores
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/users/{id}/activate").hasRole("ADMIN")
                        .requestMatchers("/api/users/{id}/deactivate").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN")
                        .requestMatchers("/api/reports/pending").hasRole("ADMIN")
                        .requestMatchers("/api/reports/{id}/resolve").hasRole("ADMIN")
                        .requestMatchers("/api/reports/{id}/reject").hasRole("ADMIN")
                        .requestMatchers("/api/posts/reported").hasRole("ADMIN")

                        .anyRequest().authenticated())
                // Configuración de sesiones: sin estado (JWT)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Proveedor de autenticación
                .authenticationProvider(authenticationProvider(userDetailsService))
                // Registro del filtro JWT antes del filtro de usuario/contraseña
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuración global de CORS.
     *
     * Permite todas las cabeceras, métodos y orígenes.
     * Se habilita el uso de credenciales.
     * 
     *
     * @return configuración de CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        log.debug("Configurando CORS para origen: {}", configuration.getAllowedOriginPatterns());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Configura el proveedor de autenticación basado en DAO.
     *
     * Utiliza un {@link UserDetailsService} para cargar los usuarios
     * y un {@link PasswordEncoder} para validar las contraseñas.
     * 
     *
     * @param userDetailsService servicio de usuarios
     * @return proveedor de autenticación
     */
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        log.info("Registrando AuthenticationProvider con BCryptPasswordEncoder");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        // Configura los dos componentes necesarios por separado
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    /**
     * Expone el {@link AuthenticationManager} para gestionar autenticaciones.
     *
     * @param config configuración de autenticación
     * @return administrador de autenticación
     * @throws Exception en caso de error
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        log.debug("Exponiendo AuthenticationManager");
        return config.getAuthenticationManager();
    }

    /**
     * Codificador de contraseñas basado en BCrypt.
     *
     * @return instancia de {@link PasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.trace("Usando BCryptPasswordEncoder como codificador de contraseñas");
        return new BCryptPasswordEncoder();
    }
}
