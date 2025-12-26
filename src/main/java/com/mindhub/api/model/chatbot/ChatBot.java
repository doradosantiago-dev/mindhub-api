package com.mindhub.api.model.chatbot;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entidad que representa un chatbot configurado en el sistema.
 *
 * Incluye nombre, descripción, modelo de IA utilizado y estado de activación.
 * Mantiene además auditoría de fechas de creación y actualización, y define
 * la relación con los mensajes asociados.
 */

@Entity
@Table(name = "chatbots", indexes = {
        @Index(name = "idx_chatbots_active", columnList = "active"),
        @Index(name = "idx_chatbots_name", columnList = "name"),
        @Index(name = "idx_chatbots_model", columnList = "model")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatBot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 100)
    private String model;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @CreatedDate
    @Column(nullable = false)
    private LocalDate creationDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDate updateDate;

    @OneToMany(mappedBy = "chatBot", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ChatBotMessage> messages;
}