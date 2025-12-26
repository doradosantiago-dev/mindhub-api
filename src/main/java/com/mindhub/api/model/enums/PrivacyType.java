package com.mindhub.api.model.enums;

/**
 * Enum que define los niveles de privacidad disponibles en el sistema.
 *
 * Se utiliza para controlar la visibilidad de perfiles de usuario y
 * publicaciones,
 * determinando si el contenido es accesible públicamente o solo por el
 * propietario
 * y las personas autorizadas.
 */
public enum PrivacyType {

    /** El recurso es visible públicamente para todos los usuarios. */
    PUBLIC,

    /** El recurso es privado y solo accesible por el propietario o autorizados. */
    PRIVATE
}
