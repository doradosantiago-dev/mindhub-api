package com.mindhub.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para la API de MindHub.
 *
 * Esta clase centraliza el tratamiento de excepciones lanzadas en los
 * controladores
 * y servicios de la aplicación, devolviendo siempre un ErrorResponse
 * con un formato uniforme.
 *
 * Cada método está anotado con ExceptionHandler
 * para capturar un tipo específico de excepción y devolver el código HTTP
 * adecuado.
 *
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Maneja la excepción UserNotFoundException.
     *
     * Se lanza cuando no se encuentra un usuario en la base de datos.
     * Devuelve un objeto ErrorResponse con estado 404 (Not Found),
     * indicando que el recurso solicitado no existe.
     *
     * @return ResponseEntity con los detalles del error y estado 404
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        log.error("Usuario no encontrado: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Usuario no encontrado")
                .message(ex.getMessage())
                .path("/api/users")
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja la excepción PostNotFoundException.
     *
     * Se lanza cuando no se encuentra un post en la base de datos.
     * Devuelve un objeto ErrorResponse con estado 404 (Not Found),
     * indicando que el recurso solicitado no existe.
     *
     * @return ResponseEntity con los detalles del error y estado 404
     */
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePostNotFoundException(PostNotFoundException ex) {
        log.error("Post no encontrado: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Post no encontrado")
                .message(ex.getMessage())
                .path("/api/posts")
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja la excepción CommentNotFoundException.
     *
     * Se lanza cuando no se encuentra un comentario en la base de datos.
     * Devuelve un objeto ErrorResponse con estado 404 (Not Found),
     * indicando que el recurso solicitado no existe.
     *
     * @return ResponseEntity con los detalles del error y estado 404
     */
    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCommentNotFoundException(CommentNotFoundException ex) {
        log.error("Comentario no encontrado: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Comentario no encontrado")
                .message(ex.getMessage())
                .path("/api/comments")
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja la excepción CommentNotAllowedException.
     *
     * Se lanza cuando un usuario intenta realizar un comentario en un recurso
     * para el cual no tiene permisos. Devuelve un objeto ErrorResponse con
     * estado 403 (Forbidden), indicando que la acción no está permitida.
     *
     * @return ResponseEntity con los detalles del error y estado 403
     */
    @ExceptionHandler(CommentNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleCommentNotAllowedException(CommentNotAllowedException ex) {
        log.error("Comentario no permitido: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("Comentario no permitido")
                .message(ex.getMessage())
                .path("/api/comments")
                .build();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    /**
     * Maneja la excepción CommentViewNotAllowedException.
     *
     * Se lanza cuando un usuario intenta visualizar un comentario al que no
     * tiene acceso. Devuelve un objeto ErrorResponse con estado 403 (Forbidden),
     * indicando que la visualización no está permitida.
     *
     * @return ResponseEntity con los detalles del error y estado 403
     */
    @ExceptionHandler(CommentViewNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleCommentViewNotAllowedException(CommentViewNotAllowedException ex) {
        log.error("Visualización de comentarios no permitida: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("Visualización de comentarios no permitida")
                .message(ex.getMessage())
                .path("/api/comments")
                .build();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    /**
     * Maneja la excepción ReportNotFoundException.
     *
     * Se lanza cuando no se encuentra un reporte en la base de datos. Devuelve
     * un objeto ErrorResponse con estado 404 (Not Found), indicando que el
     * recurso solicitado no existe.
     *
     * @return ResponseEntity con los detalles del error y estado 404
     */
    @ExceptionHandler(ReportNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReportNotFoundException(ReportNotFoundException ex) {
        log.error("Reporte no encontrado: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Reporte no encontrado")
                .message(ex.getMessage())
                .path("/api/reports")
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja la excepción UsernameAlreadyExistsException.
     *
     * Se lanza cuando se intenta registrar un usuario con un nombre de usuario
     * que ya existe en el sistema. Devuelve un objeto ErrorResponse con estado
     * 409 (Conflict), indicando un conflicto de datos.
     *
     * @return ResponseEntity con los detalles del error y estado 409
     */
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        log.error("El nombre de usuario ya existe: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Conflicto de datos")
                .message(ex.getMessage())
                .path("/api/auth/register")
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Maneja la excepción EmailAlreadyExistsException.
     *
     * Se lanza cuando se intenta registrar un usuario con un correo electrónico
     * que ya existe en el sistema. En este caso se devuelve un objeto ErrorResponse
     * con estado 409 (Conflict), indicando un conflicto de datos.
     *
     * @return ResponseEntity con los detalles del error y estado 409
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        log.error("El email ya existe: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Conflicto de datos")
                .message(ex.getMessage())
                .path("/api/auth/register")
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Maneja la excepción MaxAdminExceededException.
     *
     * Se lanza cuando se supera el número máximo permitido de administradores
     * en la plataforma. Devuelve un objeto ErrorResponse con estado 403
     * (Forbidden),
     * indicando que la operación no está permitida.
     *
     * @return ResponseEntity con los detalles del error y estado 403
     */
    @ExceptionHandler(MaxAdminExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxAdminExceededException(MaxAdminExceededException ex) {
        log.error("Límite máximo de administradores superado: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("Operacion no permitida")
                .message(ex.getMessage())
                .path("/api/auth/register")
                .build();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    /**
     * Maneja la excepción FollowException.
     *
     * Se lanza cuando ocurre un error en las operaciones de seguir o dejar de
     * seguir
     * a un usuario. Devuelve un objeto ErrorResponse con estado 400 (Bad Request),
     * indicando que la solicitud no es válida.
     *
     * @return ResponseEntity con los detalles del error y estado 400
     */
    @ExceptionHandler(FollowException.class)
    public ResponseEntity<ErrorResponse> handleFollowException(FollowException ex) {
        log.error("Error al seguir: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Error al seguir")
                .message(ex.getMessage())
                .path("/api/follows")
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja la excepción BadCredentialsException de Spring Security.
     *
     * Se lanza cuando un usuario intenta autenticarse con credenciales inválidas.
     * En este caso se devuelve un objeto ErrorResponse con estado 401
     * (Unauthorized),
     * indicando que el nombre de usuario o la contraseña son incorrectos.
     *
     * @return ResponseEntity con los detalles del error y estado 401
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        log.error("Credenciales no válidas: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Credenciales no válidas")
                .message("Nombre de usuario o contraseña incorrectos")
                .path("/api/auth/login")
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Maneja la excepción UsernameNotFoundException de Spring Security.
     *
     * Se lanza cuando el sistema de autenticación no encuentra al usuario
     * solicitado.
     * Devuelve un objeto ErrorResponse con estado 401 (Unauthorized), indicando que
     * el usuario no existe en el sistema.
     *
     * @return ResponseEntity con los detalles del error y estado 401
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        log.error("Usuario no encontrado en la autenticación: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Usuario no encontrado")
                .message("Usuario no encontrado")
                .path("/api/auth")
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Maneja la excepción AccessDeniedException de Spring Security.
     *
     * Se lanza cuando un usuario autenticado intenta acceder a un recurso para el
     * cual no tiene permisos. Devuelve un objeto ErrorResponse con estado 403
     * (Forbidden), indicando que la operación no está permitida para el usuario.
     *
     * @return ResponseEntity con los detalles del error y estado 403
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("Acceso denegado: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("Acceso denegado")
                .message("No tienes permiso para realizar esta operación")
                .path("/api")
                .build();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    /**
     * Maneja la excepción IllegalStateException.
     *
     * Se utiliza para capturar estados ilegales en la aplicación. Incluye lógica
     * condicional para determinar la respuesta:
     * Si el mensaje de la excepción contiene la palabra "disabled", devuelve un
     * error con estado 403 (Forbidden) y el mensaje "Account disabled".
     * En cualquier otro caso, devuelve un error con estado 400 (Bad Request) y
     * el mensaje "Invalid operation".
     *
     * @return ResponseEntity con los detalles del error y el estado HTTP
     *         correspondiente
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        log.error("Estado ilegal: {}", ex.getMessage());

        // Determinar el código de estado según el mensaje de la excepción
        int status = ex.getMessage() != null && ex.getMessage().toLowerCase().contains("disabled")
                ? HttpStatus.FORBIDDEN.value()
                : HttpStatus.BAD_REQUEST.value();

        // Determinar el tipo de error según el mensaje
        String error = ex.getMessage() != null && ex.getMessage().toLowerCase().contains("disabled")
                ? "Account disabled"
                : "Invalid operation";
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(error)
                .message(ex.getMessage())
                .path("/api/auth/login")
                .build();
        return new ResponseEntity<>(errorResponse, status == 403 ? HttpStatus.FORBIDDEN : HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones de validación de datos en los DTOs anotados con @Valid.
     *
     * Captura los errores de validación de campos y construye un mapa con el nombre
     * del campo y el mensaje de error correspondiente. Devuelve un objeto
     * ErrorResponse
     * con estado 400 (Bad Request), incluyendo los detalles de los errores de
     * validación
     * para que el cliente pueda corregir los datos enviados.
     *
     * @return ResponseEntity con los detalles del error de validación y estado 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Error de validación: {}", ex.getMessage());

        // Mapa para almacenar los errores de validación campo -> mensaje
        Map<String, String> errors = new HashMap<>();

        // Recorremos todos los errores de validación y los agregamos al mapa
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // Construimos la respuesta de error con detalles de validación
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Error de validación")
                .message("Los datos proporcionados no son válidos")
                .path("/api")
                .validationErrors(errors)
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones genéricas de tipo RuntimeException.
     *
     * Se utiliza como mecanismo de fallback para errores no controlados en tiempo
     * de ejecución. Cuando ocurre una excepción de este tipo, se devuelve un objeto
     * ErrorResponse con estado 500 (Internal Server Error), proporcionando al
     * cliente
     * información estructurada sobre el fallo.
     *
     * @return ResponseEntity con los detalles del error y estado 500
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        log.error("Error en tiempo de ejecución: {}", ex.getMessage(), ex);
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Error interno del servidor")
                .message("Se ha producido un error inesperado")
                .path("/api")
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Maneja excepciones genéricas de tipo Exception.
     *
     * Actúa como último recurso para capturar cualquier excepción no contemplada
     * en otros handlers específicos. Devuelve un objeto ErrorResponse con estado
     * 500 (Internal Server Error), garantizando que siempre exista una respuesta
     * consistente ante errores inesperados.
     *
     * @return ResponseEntity con los detalles del error y estado 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Error genérico: {}", ex.getMessage(), ex);
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Error interno del servidor")
                .message("Se produjo un error inesperado")
                .path("/api")
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Maneja errores relacionados con la eliminación de posts.
     *
     * Devuelve un objeto ErrorResponse con estado 500 (Internal Server Error)
     * cuando ocurre un fallo durante la eliminación de un post.
     *
     * @return ResponseEntity con los detalles del error y estado 500
     */
    @ExceptionHandler(PostDeletionException.class)
    public ResponseEntity<ErrorResponse> handlePostDeletionException(PostDeletionException ex) {
        log.error("Error al eliminar el post: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("La eliminación del post falló")
                .message(ex.getMessage())
                .path("/api/reports")
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Maneja errores durante la revisión de reportes.
     *
     * Devuelve un objeto ErrorResponse con estado 400 (Bad Request)
     * cuando ocurre un fallo en la validación o procesamiento de un reporte.
     *
     * @return ResponseEntity con los detalles del error y estado 400
     */
    @ExceptionHandler(ReportReviewException.class)
    public ResponseEntity<ErrorResponse> handleReportReviewException(ReportReviewException ex) {
        log.error("Error en la revisión del reporte: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("La revisión del reporte falló")
                .message(ex.getMessage())
                .path("/api/reports")
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja errores de validación de usuario.
     *
     * Devuelve un objeto ErrorResponse con estado 400 (Bad Request)
     * cuando los datos de un usuario no cumplen las reglas de validación.
     *
     * @return ResponseEntity con los detalles del error y estado 400
     */
    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<ErrorResponse> handleUserValidationException(UserValidationException ex) {
        log.error("Error al validar el usuario: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("La validación del usuario falló")
                .message(ex.getMessage())
                .path("/api/users")
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
