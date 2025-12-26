package com.mindhub.api.model.reaction;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.mindhub.api.model.enums.ReactionType;
import com.mindhub.api.model.post.Post;
import com.mindhub.api.model.user.User;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * Entidad que representa una reacción de usuario en un post.
 *
 * Incluye el tipo de reacción, la fecha de creación y las referencias
 * al usuario y al post asociados.
 */

@Entity
@Table(name = "reactions", uniqueConstraints = {
                @UniqueConstraint(columnNames = { "user_id", "post_id" })
}, indexes = {
                @Index(name = "idx_reactions_user", columnList = "user_id"),
                @Index(name = "idx_reactions_post", columnList = "post_id"),
                @Index(name = "idx_reactions_type", columnList = "type"),
                @Index(name = "idx_reactions_date", columnList = "creationDate"),
                @Index(name = "idx_reactions_post_date", columnList = "post_id, creationDate"),
                @Index(name = "idx_reactions_user_date", columnList = "user_id, creationDate"),
                @Index(name = "idx_reactions_post_type", columnList = "post_id, type")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reaction {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private ReactionType type;

        @CreatedDate
        @Column(nullable = false)
        private LocalDate creationDate;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "post_id", nullable = false)
        private Post post;
}
