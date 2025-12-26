package com.mindhub.api.controller.auth;

import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindhub.api.dto.auth.UserLoginRequest;
import com.mindhub.api.dto.auth.UserRegisterRequest;
import com.mindhub.api.dto.auth.UserResponse;
import com.mindhub.api.service.user.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para operaciones de autenticación y registro de usuarios.
 *
 * Proporciona endpoints para la gestión de autenticación, incluyendo el
 * registro de nuevos usuarios y la autenticación de usuarios existentes.
 *
 * Los endpoints de autenticación no requieren autenticación previa y están
 * diseñados para ser accesibles públicamente durante el proceso de registro
 * e inicio de sesión.
 *
 * Implementan validación de datos y generación segura de tokens JWT.
 */

@RestController
@RequestMapping("/api/auth")
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "Endpoints para registro y autenticación de usuarios")
public class AuthController {

  private final UserService userService;

  /**
   * Constructor del controlador de autenticación.
   *
   * Se inyecta el servicio de usuarios con anotación @Lazy para
   * evitar dependencias circulares durante la inicialización de Spring.
   *
   * @param userService Servicio encargado de la gestión de usuarios
   */
  public AuthController(@Lazy UserService userService) {
    this.userService = userService;
  }

  /**
   * Registra un nuevo usuario en el sistema.
   *
   * Este endpoint permite a los usuarios crear una nueva cuenta en la plataforma.
   * El proceso incluye la validación de los datos de entrada, la verificación de
   * unicidad de nombre de usuario y correo electrónico, y la creación del perfil
   * con configuración por defecto.
   *
   * El usuario registrado se crea con estado activo y rol USER por defecto.
   *
   * @param request Objeto con los datos de registro (username, email, password,
   *                etc.)
   * @return ResponseEntity con los datos del usuario registrado y código 201
   *         (CREATED)
   */
  @PostMapping("/register")
  @Operation(summary = "Registrar nuevo usuario", description = "Crea una nueva cuenta de usuario en la plataforma con validación de datos")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "400", description = "Datos de registro inválidos o usuario ya existe"),
      @ApiResponse(responseCode = "422", description = "Error de validación en los datos de entrada")
  })
  public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegisterRequest request) {
    log.debug("Iniciando registro de usuario: {}", request.username());

    UserResponse response = userService.registerUser(request);

    log.debug("Usuario registrado exitosamente: {}", request.username());

    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  /**
   * Autentica un usuario existente y genera un token JWT.
   * 
   * Este endpoint permite a los usuarios autenticarse en la plataforma usando
   * sus credenciales (username y password). Si la autenticación es exitosa,
   * se genera un token JWT que debe ser incluido en las siguientes peticiones
   * para acceder a recursos protegidos.
   * 
   * La respuesta incluye el token JWT, los datos del usuario autenticado
   * y un mensaje de confirmación.
   * 
   * @param request Credenciales de autenticación (username, password)
   * @return ResponseEntity con token JWT, datos del usuario y mensaje de éxito
   */
  @PostMapping("/login")
  @Operation(summary = "Autenticar usuario", description = "Autentica un usuario con sus credenciales y genera un token JWT")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Autenticación exitosa", content = @Content(mediaType = "application/json", schema = @Schema(example = """
          {
            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
            "user": {
              "id": 1,
              "username": "usuario123",
              "email": "usuario@example.com",
              "firstName": "Juan",
              "lastName": "Pérez"
            },
            "message": "Login exitoso"
          }
          """))),
      @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
      @ApiResponse(responseCode = "400", description = "Datos de autenticación inválidos"),
      @ApiResponse(responseCode = "403", description = "Usuario inactivo o bloqueado")
  })
  public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody UserLoginRequest request) {
    log.debug("Iniciando autenticación para usuario: {}", request.username());

    String jwt = userService.authenticateUser(request);

    UserResponse userResponse = userService.findByUsername(request.username());

    Map<String, Object> response = Map.of(
        "token", jwt,
        "user", userResponse,
        "message", "Login exitoso");

    log.debug("Autenticación exitosa para usuario: {}", request.username());

    return ResponseEntity.ok(response);
  }

}
