package com.mindhub.api.mapper.notification;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mindhub.api.dto.notification.NotificationResponse;
import com.mindhub.api.model.notification.Notification;

/**
 * Mapper de MapStruct encargado de transformar entidades Notification en
 * objetos de respuesta NotificationResponse.
 *
 * Define reglas de conversión específicas para mantener consistencia en
 * la capa de presentación, renombrando algunos campos como type,
 * referenceId y referenceTable.
 */

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    /**
     * Convierte una entidad Notification en un DTO NotificationResponse.
     *
     * El campo type se transforma en notificationType, el campo referenceId se
     * asigna a referenciaId,
     * y el campo referenceTable se mapea a referenciaTabla.
     *
     * @param notification entidad Notification a convertir
     * @return NotificationResponse con los datos de la notificación
     */
    @Mapping(source = "type", target = "notificationType")
    @Mapping(source = "referenceId", target = "referenciaId")
    @Mapping(source = "referenceTable", target = "referenciaTabla")
    NotificationResponse toResponse(Notification notification);

}
