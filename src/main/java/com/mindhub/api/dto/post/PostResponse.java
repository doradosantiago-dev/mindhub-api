package com.mindhub.api.dto.post;

import java.time.LocalDate;

import com.mindhub.api.dto.auth.UserResponse;
import com.mindhub.api.model.enums.PrivacyType;


/**
 * DTO de respuesta que representa la información detallada de un post
 * en la plataforma.
 *
 * Este record se utiliza para devolver los datos de una publicación,
 * incluyendo su contenido, metadatos, autor y estadísticas básicas
 * de interacción (comentarios y likes).
 *
 * Se emplea en endpoints relacionados con la visualización de posts,
 * tanto en feeds públicos como privados, respetando el nivel de
 * privacidad configurado por el autor.
 *
 * @param id           identificador único del post
 * @param content      contenido textual de la publicación
 * @param imageUrl     URL de la imagen asociada al post (puede ser null si no
 *                     tiene)
 * @param privacyType  nivel de privacidad del post ({@link PrivacyType}:
 *                     PUBLIC, PRIVATE)
 * @param creationDate fecha en la que se creó el post
 * @param updateDate   fecha de la última actualización del post
 * @param author       información del autor del post ({@link UserResponse})
 * @param commentCount número total de comentarios asociados al post
 * @param likeCount    número total de reacciones positivas (likes) en el post
 *
 */
public record PostResponse(
        Long id,
        String content,
        String imageUrl,
        PrivacyType privacyType,
        LocalDate creationDate,
        LocalDate updateDate,
        UserResponse author,
        Integer commentCount,
        Integer likeCount) {
}
