package com.mindhub.api.controller.notification;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindhub.api.dto.notification.NotificationResponse;
import com.mindhub.api.service.notification.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para la gestión de notificaciones de usuario en la
 * plataforma MindHub.
 *
 * Proporciona endpoints para obtener notificaciones del usuario autenticado,
 * consultar notificaciones no leídas,
 * contar notificaciones no leídas, marcar notificaciones como leídas y marcar
 * todas las notificaciones como leídas.
 *
 * Todos los endpoints requieren autenticación y están diseñados para garantizar
 * la privacidad de las notificaciones de cada usuario.
 */

@Slf4j
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Obtiene todas las notificaciones del usuario autenticado con paginación.
     *
     * @param pageable parámetros de paginación (page, size, sort)
     * @return ResponseEntity con la lista paginada de notificaciones
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<NotificationResponse>> getMyNotifications(
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("Solicitando notificaciones para el usuario autenticado");

        try {
            Page<NotificationResponse> notifications = notificationService
                    .getUserNotifications(pageable);

            log.info("Notificaciones encontradas: {}", notifications.getTotalElements());

            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            log.error("Error al obtener las notificaciones: {}", e.getMessage(), e);

            throw e;
        }
    }

    /**
     * Obtiene todas las notificaciones no leídas del usuario autenticado con
     * paginación.
     *
     * @param pageable parámetros de paginación (page, size, sort)
     * @return ResponseEntity con la lista paginada de notificaciones no leídas
     */
    @GetMapping("/unread")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<NotificationResponse>> getUnreadNotifications(
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("Solicitando notificaciones no leídas para el usuario autenticado");

        Page<NotificationResponse> notifications = notificationService
                .getUnreadUserNotifications(pageable);

        return ResponseEntity.ok(notifications);
    }

    /**
     * Obtiene el número total de notificaciones no leídas del usuario autenticado.
     *
     * @return ResponseEntity con el conteo de notificaciones no leídas
     */
    @GetMapping("/unread/count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Long>> getUnreadCount() {
        log.info("Solicitando conteo de notificaciones no leídas");

        long count = notificationService.countUnreadNotifications();

        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * Marca una notificación específica como leída.
     *
     * @param id identificador de la notificación
     * @return ResponseEntity con mensaje de confirmación
     */
    @PutMapping("/{id}/read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> markAsRead(@PathVariable Long id) {
        log.info("Marcando la notificación {} como leída", id);

        notificationService.markAsRead(id);

        return ResponseEntity.ok(Map.of("message", "Notificación marcada como leída"));
    }

    /**
     * Marca todas las notificaciones del usuario autenticado como leídas.
     *
     * @return ResponseEntity con mensaje de confirmación
     */
    @PutMapping("/read-all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> markAllAsRead() {
        log.info("Marcando todas las notificaciones como leídas");

        notificationService.markAllAsRead();

        return ResponseEntity.ok(Map.of("message", "Todas las notificaciones marcadas como leídas"));

    }
}
