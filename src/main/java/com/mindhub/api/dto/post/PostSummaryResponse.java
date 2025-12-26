package com.mindhub.api.dto.post;

import com.mindhub.api.dto.auth.UserResponse;

/**
 * DTO de respuesta que representa un resumen de un post en la plataforma.
 *
 * Este record se utiliza para devolver información básica de una publicación,
 * sin necesidad de incluir todos los detalles del post completo. Es útil en
 * contextos donde se requiere mostrar una vista simplificada, como en listados,
 * notificaciones o referencias dentro de otros recursos (ej. reportes).
 *
 * @param id      identificador único del post
 * @param content contenido textual principal del post (puede estar truncado en
 *                vistas resumidas)
 * @param author  información básica del autor del post ({@link UserResponse})
 * @param exists  indicador booleano que señala si el post aún existe en la base
 *                de datos
 *                (false en caso de haber sido eliminado o no disponible)
 *
 */
public record PostSummaryResponse(
        Long id,
        String content,
        UserResponse author,
        boolean exists) {
}
