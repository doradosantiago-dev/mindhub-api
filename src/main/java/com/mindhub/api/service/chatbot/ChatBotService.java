package com.mindhub.api.service.chatbot;

import java.util.List;

import com.mindhub.api.dto.chatbot.ChatBotMessageResponse;

/**
 * Servicio para gestionar las interacciones con el chatbot del sistema.
 *
 * Ofrece funcionalidades para enviar mensajes al chatbot,
 * consultar el historial de conversaciones y administrar
 * las sesiones de usuario.
 */

public interface ChatBotService {

    /**
     * Envía un mensaje al chatbot y obtiene la respuesta.
     * 
     * @param message Mensaje del usuario
     * @return Respuesta del chatbot
     */
    ChatBotMessageResponse sendMessage(String message);

    /**
     * Obtiene el historial completo de la conversación actual del usuario.
     * 
     * @return Lista de mensajes de la conversación actual
     */
    List<ChatBotMessageResponse> getCurrentUserConversationHistory();

    /**
     * Elimina toda la conversación actual del usuario.
     */
    void clearCurrentUserConversation();
}
