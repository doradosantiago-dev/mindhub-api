package com.mindhub.api.config;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.mindhub.api.model.chatbot.ChatBot;
import com.mindhub.api.model.enums.PrivacyType;
import com.mindhub.api.model.role.Role;
import com.mindhub.api.model.user.User;
import com.mindhub.api.model.userProfile.UserProfile;
import com.mindhub.api.repository.chatbot.ChatBotRepository;
import com.mindhub.api.repository.user.UserRepository;
import com.mindhub.api.repository.userProfile.UserProfileRepository;
import com.mindhub.api.service.role.RoleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Inicializa datos por defecto de la aplicación al arrancar.
 *
 * Garantiza que existan entidades esenciales como los roles,
 * un usuario administrador y un chatbot inicial. De esta forma,
 * el sistema siempre cuenta con una base mínima de datos para
 * funcionar correctamente.
 *
 * Los valores podrían definirse en el fichero de configuración
 * application.properties. Si no se encuentran configurados,
 * se usarán valores por defecto pensados para desarrollo local.
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final ChatBotRepository chatBotRepository;
    private final RoleService roleService;

    @Value("${app.admin.username:admin}")
    private String adminUsername;

    @Value("${app.admin.password:password}")
    private String adminPassword;

    @Value("${app.admin.firstName:Juan Carlos}")
    private String adminFirstName;

    @Value("${app.admin.lastName:García}")
    private String adminLastName;

    @Value("${app.admin.email:admin@redsocial.com}")
    private String adminEmail;

    @Value("${app.chatbot.name:Virtual Assistant FCT}")
    private String chatbotName;

    @Value("${app.chatbot.description:Intelligent chatbot for social network}")
    private String chatbotDescription;

    @Value("${app.chatbot.model:Simulated Predefined Intelligent System}")
    private String chatbotModel;

    /**
     * Ejecuta la lógica de inicialización al arrancar la aplicación.
     *
     * Garantiza que se creen los roles por defecto, un administrador inicial
     * y un chatbot.
     * 
     *
     * @param args argumentos de inicio
     */
    @Override
    public void run(String... args) {
        try {
            initializeDefaultRoles();
            initializeDefaultAdmin();
            initializeDefaultChatBot();

            log.info("Inicialización de datos de la aplicación finalizada correctamente");
        } catch (Exception e) {
            log.error("Error durante la inicialización de los datos de la aplicación: {}", e.getMessage(), e);

            throw new RuntimeException("Error al inicializar los datos de la aplicación.", e);
        }
    }

    /**
     * Crea los roles por defecto si no existen.
     */
    private void initializeDefaultRoles() {
        roleService.createDefaultRolesIfNotExist();
    }

    /**
     * Crea un usuario administrador por defecto si no existe ya.
     *
     * El administrador se crea con rol ADMIN, una foto de perfil
     * por defecto y configuración de privacidad privada.
     * 
     */
    @SuppressWarnings("null")
    private void initializeDefaultAdmin() {
        Role adminRole = roleService.findByNameOrThrow(Role.ADMIN);

        // Verificar si el admin ya existe por email o username
        if (userRepository.existsByEmail(adminEmail) || userRepository.existsByUsername(adminUsername)) {
            log.info("El usuario administrador ya existe. Email: {} o Username: {}", adminEmail, adminUsername);
            return;
        }

        User admin = User.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .firstName(adminFirstName)
                .lastName(adminLastName)
                .email(adminEmail)
                .profilePicture(
                        "https://images.unsplash.com/photo-1560250097-0b93528c311a?w=150&h=150&fit=crop&crop=face")
                .role(adminRole)
                .privacyType(PrivacyType.PRIVATE)
                .active(true)
                .registrationDate(LocalDate.now())
                .lastActivityDate(LocalDate.now())
                .build();

        User savedAdmin = userRepository.save(admin);

        log.info("Admin creado: {}", savedAdmin.getUsername());

        UserProfile adminProfile = UserProfile.builder()
                .user(savedAdmin)
                .creationDate(LocalDate.now())
                .build();

        userProfileRepository.save(adminProfile);
    }

    /**
     * Crea un chatbot por defecto si no existe.
     *
     * El chatbot se marca como activo y se inicializa con valores por defecto
     * para nombre, descripción y modelo. Solo se permite un chatbot en el sistema.
     * 
     */
    @SuppressWarnings("null")
    private void initializeDefaultChatBot() {
        // Verificar si ya existe algún chatbot
        long chatbotCount = chatBotRepository.count();
        if (chatbotCount > 0) {
            log.info("Ya existe un chatbot en el sistema. No se creará uno nuevo.");

            // Asegurar que solo haya uno activo
            chatBotRepository.findAllByActiveTrue().stream()
                    .skip(1)
                    .forEach(bot -> {
                        bot.setActive(false);
                        chatBotRepository.save(bot);
                    });
            return;
        }

        ChatBot chatBot = ChatBot.builder()
                .name(chatbotName)
                .description(chatbotDescription)
                .model(chatbotModel)
                .active(true)
                .creationDate(LocalDate.now())
                .build();

        chatBotRepository.save(chatBot);

        log.info("Chatbot creado: {}", chatBot.getName());
    }
}
