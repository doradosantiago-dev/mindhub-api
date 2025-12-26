package com.mindhub.api.mapper.chatBotMessage;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mindhub.api.dto.chatbot.ChatBotMessageResponse;
import com.mindhub.api.model.chatbot.ChatBotMessage;
import com.mindhub.api.mapper.user.UserMapper; // Importamos el UserMapper

/**
 * Mapper de MapStruct encargado de transformar entidades ChatBotMessage en
 * objetos de respuesta ChatBotMessageResponse.
 *
 * Define las reglas de conversión para mapear campos específicos, como el
 * nombre del chatbot al campo chatBotName,
 * el tipo de mensaje al campo messageType y la conversión del usuario mediante
 * el UserMapper.
 *
 * También permite convertir listas completas de mensajes en listas de objetos
 * DTO.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class }) // Añadimos el 'uses'
public interface ChatBotMessageMapper {

    /**
     * Convierte una entidad ChatBotMessage en un DTO ChatBotMessageResponse.
     *
     * @param chatBotMessage entidad a convertir
     * @return DTO con los datos del mensaje del chatbot
     */
    @Mapping(target = "chatBotName", source = "chatBot.name")
    @Mapping(source = "type", target = "messageType")
    // Eliminamos la expresión manual. MapStruct usará UserMapper automáticamente para el campo 'user'
    ChatBotMessageResponse toResponse(ChatBotMessage chatBotMessage);

    /**
     * Convierte una lista de entidades ChatBotMessage en una lista de DTOs
     * ChatBotMessageResponse.
     *
     * @param chatBotMessages lista de entidades a convertir
     * @return lista de DTOs con los datos de los mensajes
     */
    List<ChatBotMessageResponse> toResponseList(List<ChatBotMessage> chatBotMessages);
}