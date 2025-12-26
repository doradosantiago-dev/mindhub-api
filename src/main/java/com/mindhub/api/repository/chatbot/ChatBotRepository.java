package com.mindhub.api.repository.chatbot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mindhub.api.model.chatbot.ChatBot;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de chatbots del sistema.
 *
 * Proporciona métodos para consultar y administrar los chatbots
 * activos en el sistema de conversación.
 */

@Repository
public interface ChatBotRepository extends JpaRepository<ChatBot, Long> {

    /**
     * Busca el chatbot activo del sistema.
     * 
     * Retorna el chatbot que está actualmente habilitado
     * para procesar conversaciones con los usuarios.
     * Si hay múltiples chatbots activos, retorna el primero por ID.
     * 
     * 
     * @return Optional con el chatbot activo, o vacío si no existe
     * 
     * @see ChatBot
     */
    @Query("SELECT c FROM ChatBot c WHERE c.active = true ORDER BY c.id ASC LIMIT 1")
    Optional<ChatBot> findByActiveTrue();

    /**
     * Obtiene todos los chatbots activos del sistema.
     * 
     * @return Lista de chatbots activos
     */
    @Query("SELECT c FROM ChatBot c WHERE c.active = true")
    List<ChatBot> findAllByActiveTrue();
}
