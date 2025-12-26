package com.mindhub.api.model.enums;

/**
 * Enum que define los tipos de reacciones disponibles en el sistema.
 *
 * Actualmente solo está implementada la reacción de "LIKE", pero este
 * enum está diseñado para poder ampliarse en el futuro con más tipos
 * de reacciones como LOVE, HAHA, WOW, SAD o ANGRY. Se utiliza en la
 * entidad Reaction para registrar y gestionar las interacciones de
 * los usuarios con el contenido.
 */

public enum ReactionType {

    /**
     * Indica que un usuario ha dado "LIKE" a un contenido.
     * Representa una valoración positiva o de aprobación.
     */
    LIKE

    // En el futuro podrían añadirse más tipos de reacciones:
    // LOVE, HAHA, WOW, SAD, ANGRY, etc.
}
