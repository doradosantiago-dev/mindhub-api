package com.mindhub.api.repository.chatBotMessage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mindhub.api.model.chatbot.ChatBotMessage;
import com.mindhub.api.model.user.User;

import java.util.List;

/**
 * Repositorio para la gestión de chatbots del sistema.
 *
 * Proporciona métodos para consultar y administrar los chatbots
 * activos en el sistema de conversación.
 */

@Repository
public interface ChatBotMessageRepository extends JpaRepository<ChatBotMessage, Long> {

    /**
     * Busca mensajes del chatbot por usuario y sesión.
     * 
     * Retorna todos los mensajes de una conversación específica
     * ordenados por fecha de creación ascendente (cronológicamente).
     * 
     * 
     * @param user      Usuario propietario de los mensajes
     * @param sessionId Identificador de la sesión de conversación
     * @return Lista de mensajes de la conversación ordenados cronológicamente
     * 
     * @see ChatBotMessage
     * @see User
     */
    @Query("SELECT m FROM ChatBotMessage m WHERE m.user = :user AND m.sessionId = :sessionId ORDER BY m.creationDate ASC")
    List<ChatBotMessage> findByUserAndSessionIdOrderByCreationDateAsc(@Param("user") User user,
            @Param("sessionId") String sessionId);

}
