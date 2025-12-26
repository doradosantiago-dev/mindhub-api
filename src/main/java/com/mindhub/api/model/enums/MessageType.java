package com.mindhub.api.model.enums;

/**
 * Enum que define los tipos de mensajes disponibles en el sistema.
 *
 * Permite distinguir si un mensaje fue enviado por un usuario
 * o generado automáticamente por el chatbot. Se utiliza en la
 * entidad Message para clasificar y filtrar los mensajes registrados.
 */

public enum MessageType {

    /**
     * Mensaje enviado por un usuario humano.
     * Representa la interacción directa del usuario con el sistema.
     */
    USER,
    /**
     * Mensaje generado automáticamente por el chatbot.
     * Representa respuestas o notificaciones emitidas por el sistema.
     */
    CHATBOT
}
