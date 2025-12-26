package com.mindhub.api.service.chatbot;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mindhub.api.dto.chatbot.ChatBotMessageResponse;
import com.mindhub.api.mapper.chatBotMessage.ChatBotMessageMapper;
import com.mindhub.api.model.chatbot.ChatBot;
import com.mindhub.api.model.chatbot.ChatBotMessage;
import com.mindhub.api.model.enums.MessageType;
import com.mindhub.api.model.user.User;
import com.mindhub.api.repository.chatBotMessage.ChatBotMessageRepository;
import com.mindhub.api.repository.chatbot.ChatBotRepository;
import com.mindhub.api.service.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio de chatbot.
 *
 * Gestiona las interacciones con el chatbot, incluyendo el envío de mensajes,
 * procesamiento de respuestas y gestión del historial de conversaciones.
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatBotServiceImpl implements ChatBotService {

        private final ChatBotRepository chatBotRepository;
        private final ChatBotMessageRepository chatBotMessageRepository;
        private final ChatBotMessageMapper chatBotMessageMapper;
        private final UserService userService;
        private final ChatBotResponseService chatBotResponseService;

        /**
         * Envía un mensaje al chatbot y obtiene la respuesta.
         * 
         * Procesa el mensaje del usuario, genera una respuesta contextual
         * basada en el historial de conversación y guarda ambos mensajes.
         * 
         * @param message Mensaje del usuario a procesar
         * @return Respuesta del chatbot
         */
        @Override
        public ChatBotMessageResponse sendMessage(String message) {
                User currentUser = userService.getCurrentUser();

                String userSessionId = "user_" + currentUser.getId();

                ChatBot chatBot = getActiveChatBot();

                log.debug("Usuario {} envía mensaje al chatbot: {}", currentUser.getId(), message);

                ChatBotMessage userMessage = ChatBotMessage.builder()
                                .content(message)
                                .type(MessageType.USER)
                                .sessionId(userSessionId)
                                .user(currentUser)
                                .chatBot(chatBot)
                                .build();

                chatBotMessageRepository.save(userMessage);

                log.info("Mensaje de usuario {} guardado en sesión {}", currentUser.getId(), userSessionId);

                List<ChatBotMessage> historial = chatBotMessageRepository
                                .findByUserAndSessionIdOrderByCreationDateAsc(currentUser, userSessionId);

                String response = chatBotResponseService.generarRespuestaSimulada(message, historial);

                ChatBotMessage botMessage = ChatBotMessage.builder()
                                .content(response)
                                .type(MessageType.CHATBOT)
                                .sessionId(userSessionId)
                                .user(currentUser)
                                .chatBot(chatBot)
                                .build();

                ChatBotMessage savedBotMessage = chatBotMessageRepository.save(botMessage);

                log.info("Respuesta del chatbot guardada para usuario {} en sesión {}", currentUser.getId(),
                                userSessionId);

                return chatBotMessageMapper.toResponse(savedBotMessage);
        }

        /**
         * Obtiene el historial completo de la conversación actual del usuario.
         * 
         * @return Lista de mensajes de la conversación actual ordenados por fecha de
         *         creación ascendente
         */
        @Override
        @Transactional(readOnly = true)
        public List<ChatBotMessageResponse> getCurrentUserConversationHistory() {
                User currentUser = userService.getCurrentUser();
                String sessionId = "user_" + currentUser.getId();

                log.debug("Consultando historial de conversación del usuario {} en sesión {}", currentUser.getId(),
                                sessionId);

                List<ChatBotMessage> messages = chatBotMessageRepository
                                .findByUserAndSessionIdOrderByCreationDateAsc(currentUser, sessionId);

                return chatBotMessageMapper.toResponseList(messages);
        }

        /**
         * Elimina toda la conversación actual del usuario.
         * 
         * Elimina todos los mensajes de la sesión actual del usuario,
         * tanto del usuario como del chatbot.
         */
        @Override
        public void clearCurrentUserConversation() {
                User currentUser = userService.getCurrentUser();
                String sessionId = "user_" + currentUser.getId();

                log.debug("Eliminando historial de conversación del usuario {} en sesión {}", currentUser.getId(),
                                sessionId);

                List<ChatBotMessage> messages = chatBotMessageRepository
                                .findByUserAndSessionIdOrderByCreationDateAsc(currentUser, sessionId);

                chatBotMessageRepository.deleteAll(messages);

                log.info("Conversación del usuario {} eliminada correctamente", currentUser.getId());
        }

        /**
         * Obtiene el chatbot del sistema.
         * 
         * @return Chatbot activo
         * @throws RuntimeException si no existe ningún chatbot
         */
        private ChatBot getActiveChatBot() {
                return chatBotRepository.findByActiveTrue()
                                .orElseThrow(() -> new RuntimeException(
                                                "No se encontró un chatbot en el sistema. Reinicie la aplicación."));
        }

}
