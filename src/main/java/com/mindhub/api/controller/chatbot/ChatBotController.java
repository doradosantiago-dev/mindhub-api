package com.mindhub.api.controller.chatbot;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindhub.api.dto.chatbot.ChatBotMessageResponse;
import com.mindhub.api.service.chatbot.ChatBotService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para operaciones del chatbot en la plataforma MindHub.
 *
 * Proporciona endpoints para la interacción con el chatbot, incluyendo el
 * envío de mensajes al chatbot, obtención del historial de conversación del
 * usuario
 * y limpieza del historial de conversación.
 *
 * Todos los endpoints requieren autenticación de usuario y están diseñados
 * para proporcionar una experiencia de chat interactiva y personalizada
 * para cada usuario.
 *
 * Todos los endpoints están restringidos a usuarios autenticados para
 * garantizar la privacidad de las conversaciones.
 */

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "ChatBot", description = "Endpoints para interacción con el chatbot de la plataforma")
@SecurityRequirement(name = "bearerAuth")
public class ChatBotController {

        private final ChatBotService chatBotService;

        /**
         * Envía un mensaje al chatbot y recibe una respuesta.
         * 
         * Este endpoint permite a los usuarios interactuar con el chatbot enviando
         * mensajes y recibiendo respuestas inteligentes. El mensaje se procesa
         * y se genera una respuesta contextual basada en el contenido del mensaje.
         * 
         * La conversación se almacena en el historial del usuario para mantener
         * el contexto de la conversación.
         * 
         * @param request Mapa que contiene el mensaje del usuario
         * @return ResponseEntity con la respuesta del chatbot
         */
        @PostMapping("/send")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Enviar mensaje al chatbot", description = "Envía un mensaje al chatbot y recibe una respuesta inteligente")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Respuesta del chatbot generada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatBotMessageResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "400", description = "Mensaje inválido o vacío")
        })
        public ResponseEntity<ChatBotMessageResponse> sendMessage(
                        @RequestBody Map<String, String> request) {
                String message = request.get("message");

                log.debug("Mensaje recibido para ChatBot: {}", message);

                ChatBotMessageResponse response = chatBotService.sendMessage(message);

                log.debug("Respuesta del ChatBot generada exitosamente");

                return ResponseEntity.ok(response);
        }

        /**
         * Obtiene el historial completo de conversación del usuario actual.
         * 
         * Este endpoint permite a los usuarios recuperar todo su historial de
         * conversación con el chatbot. La conversación se mantiene privada
         * y solo es accesible para el usuario autenticado.
         * 
         * El historial incluye tanto los mensajes enviados por el usuario como
         * las respuestas generadas por el chatbot, ordenados cronológicamente.
         * 
         * @return ResponseEntity con la lista de mensajes del historial de conversación
         */
        @GetMapping("/conversation")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Obtener historial de conversación", description = "Recupera el historial completo de conversación del usuario con el chatbot")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Historial de conversación obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatBotMessageResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación")
        })
        public ResponseEntity<List<ChatBotMessageResponse>> getCurrentUserConversation() {
                log.debug("Obteniendo historial de conversación del usuario actual");

                List<ChatBotMessageResponse> messages = chatBotService.getCurrentUserConversationHistory();

                log.debug("Historial de conversación obtenido: {} mensajes", messages.size());

                return ResponseEntity.ok(messages);
        }

        /**
         * Elimina todo el historial de conversación del usuario actual.
         * 
         * Este endpoint permite a los usuarios limpiar completamente su historial
         * de conversación con el chatbot. Esta acción es irreversible y elimina
         * todos los mensajes almacenados para el usuario.
         * 
         * Después de la limpieza, el usuario comenzará una nueva conversación
         * sin contexto previo.
         * 
         * @return ResponseEntity con mensaje de confirmación de eliminación
         */
        @DeleteMapping("/conversation")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Limpiar historial de conversación", description = "Elimina completamente el historial de conversación del usuario con el chatbot")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Historial de conversación eliminado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                                        {
                                          "message": "Tu conversación a sido eliminada exitosamente"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación")
        })
        public ResponseEntity<Map<String, String>> clearCurrentUserConversation() {
                log.debug("Limpiando historial de conversación del usuario actual");

                chatBotService.clearCurrentUserConversation();

                log.debug("Historial de conversación eliminado exitosamente");

                return ResponseEntity.ok(Map.of("message", "Tu conversación a sido eliminada exitosamente"));
        }

}
