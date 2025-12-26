package com.mindhub.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI para la documentación de la API.
 *
 * Define la configuración de Swagger/OpenAPI para generar
 * documentación automática de los endpoints de la API REST.
 */

@Configuration
public class OpenApiConfig {

        /**
         * Configura la documentación de OpenAPI para la aplicación.
         * 
         * Define la información general de la API, servidores disponibles,
         * esquemas de seguridad y componentes reutilizables.
         * 
         * 
         * @return Configuración completa de OpenAPI
         */
        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .info(createApiInfo())
                                .servers(createServers())
                                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                                .components(new Components()
                                                .addSecuritySchemes("bearerAuth", createAPIKeyScheme()));
        }

        /**
         * Crea la información general de la API.
         * 
         * @return Información de la API
         */
        private Info createApiInfo() {
                return new Info()
                                .title("MindHub API")
                                .description("API REST para la plataforma social de desarrolladores MindHub. " +
                                                "Proporciona endpoints para gestión de usuarios, publicaciones, " +
                                                "comentarios, reacciones, reportes y sistema de notificaciones.")
                                .version("1.0.0")
                                .contact(createContact())
                                .license(createLicense());
        }

        /**
         * Crea la información de contacto del equipo de desarrollo.
         * 
         * @return Información de contacto
         */
        private Contact createContact() {
                return new Contact()
                                .name("MindHub Development Team");

        }

        /**
         * Crea la información de licencia de la API.
         * 
         * @return Información de licencia
         */
        private License createLicense() {
                return new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT");
        }

        /**
         * Define los servidores disponibles para la API.
         * 
         * @return Lista de servidores
         */
        private List<Server> createServers() {
                return List.of(
                                new Server()
                                                .url("http://localhost:8080")
                                                .description("Servidor de desarrollo local"));
        }

        /**
         * Crea el esquema de seguridad para autenticación JWT.
         * 
         * @return Esquema de seguridad
         */
        private SecurityScheme createAPIKeyScheme() {
                return new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .bearerFormat("JWT")
                                .scheme("bearer")
                                .description("Token JWT para autenticación. " +
                                                "Incluye el token en el header Authorization: Bearer {token}");
        }
}
