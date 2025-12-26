package com.mindhub.api.model.chatbot;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

import com.mindhub.api.model.enums.MessageType;
import com.mindhub.api.model.user.User;

/**
 * Entidad que representa los mensajes intercambiados con el chatbot.
 *
 * Almacena el contenido del mensaje, su tipo, la sesión a la que pertenece
 * y la fecha de creación, además de las referencias al usuario y al chatbot
 * asociados.
 */

@Entity
@Table(name = "chatbot_messages", indexes = {
        @Index(name = "idx_chatbot_messages_user", columnList = "user_id"),
        @Index(name = "idx_chatbot_messages_session", columnList = "sessionId"),
        @Index(name = "idx_chatbot_messages_date", columnList = "creationDate"),
        @Index(name = "idx_chatbot_messages_user_session", columnList = "user_id, sessionId")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatBotMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type;

    @CreatedDate
    @Column(nullable = false)
    private LocalDate creationDate;

    @Column(nullable = false, length = 100)
    private String sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatbot_id", nullable = false)
    private ChatBot chatBot;
}