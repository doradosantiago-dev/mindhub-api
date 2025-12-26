package com.mindhub.api.dto.chatbot;

import java.time.LocalDate;

import com.mindhub.api.dto.auth.UserResponse;
import com.mindhub.api.model.enums.MessageType;


/**
 * DTO de respuesta que representa un mensaje dentro de una sesión de chat
 * con el chatbot de la plataforma.
 *
 * Este record se utiliza para devolver información sobre los mensajes
 * intercambiados entre un usuario y el chatbot, incluyendo el contenido,
 * el tipo de mensaje, el autor y metadatos de la sesión.
 *
 * Se emplea en endpoints relacionados con la interacción conversacional,
 * historial de chats o análisis de sesiones de soporte automatizado.
 *
 * @param id           identificador único del mensaje
 * @param content      contenido textual del mensaje
 * @param messageType  tipo de mensaje ({@link MessageType}: ej. USER, CHATBOT)
 * @param creationDate fecha en la que se creó el mensaje
 * @param sessionId    identificador único de la sesión de chat a la que
 *                     pertenece el mensaje
 * @param user         información del usuario que envió el mensaje
 *                     ({@link UserResponse}),
 *                     puede ser null si el mensaje fue generado por el chatbot
 * @param chatBotName  nombre del chatbot que generó el mensaje (si aplica)
 *
 */
public record ChatBotMessageResponse(
        Long id,
        String content,
        MessageType messageType,
        LocalDate creationDate,
        String sessionId,
        UserResponse user,
        String chatBotName) {
}
