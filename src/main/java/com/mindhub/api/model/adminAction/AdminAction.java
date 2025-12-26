package com.mindhub.api.model.adminAction;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.mindhub.api.model.enums.ActionType;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * Entidad que registra las acciones administrativas realizadas en el sistema.
 *
 * Incluye un identificador único, el tipo de acción, una descripción,
 * el motivo, la fecha en que se registró, el identificador y la tabla
 * del objeto afectado, así como los identificadores del administrador
 * que ejecutó la acción y del usuario afectado en caso de existir.
 */

@Entity
@Table(name = "admin_actions", indexes = {
        @Index(name = "idx_admin_actions_action", columnList = "action"),
        @Index(name = "idx_admin_actions_date", columnList = "actionDate"),
        @Index(name = "idx_admin_actions_admin", columnList = "admin_id"),
        @Index(name = "idx_admin_actions_affected_user", columnList = "affected_user_id"),
        @Index(name = "idx_admin_actions_object", columnList = "objectTable, objectId")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType action;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(length = 1000)
    private String reason;

    @CreatedDate
    @Column(nullable = false)
    private LocalDate actionDate;

    @Column(nullable = false)
    private Long objectId;

    @Column(nullable = false, length = 100)
    private String objectTable;

    @Column(name = "admin_id", nullable = false)
    private Long adminId;

    @Column(name = "affected_user_id")
    private Long affectedUserId;

    @Column(name = "affected_username", length = 100)
    private String affectedUsername;

    @Column(name = "affected_first_name", length = 100)
    private String affectedFirstName;

    @Column(name = "affected_last_name", length = 100)
    private String affectedLastName;
}