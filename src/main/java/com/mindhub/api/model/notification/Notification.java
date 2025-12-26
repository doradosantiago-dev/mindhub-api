package com.mindhub.api.model.notification;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.mindhub.api.model.enums.NotificationType;
import com.mindhub.api.model.user.User;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * Entidad que representa una notificación del sistema.
 *
 * Incluye título, mensaje, tipo, estado de lectura, referencias opcionales
 * a otros objetos y la auditoría de fechas de creación y actualización.
 */

@Entity
@Table(name = "notifications", indexes = {
        @Index(name = "idx_notifications_user", columnList = "user_id"),
        @Index(name = "idx_notifications_read", columnList = "read"),
        @Index(name = "idx_notifications_date", columnList = "creationDate"),
        @Index(name = "idx_notifications_type", columnList = "type"),
        @Index(name = "idx_notifications_user_read", columnList = "user_id, read"),
        @Index(name = "idx_notifications_user_date", columnList = "user_id, creationDate")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 500)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Builder.Default
    @Column(nullable = false)
    private Boolean read = false;

    @CreatedDate
    @Column(nullable = false)
    private LocalDate creationDate;

    @Column(nullable = true)
    private LocalDate readDate;

    @Column(nullable = true)
    private Long referenceId;

    @Column(nullable = true, length = 100)
    private String referenceTable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
