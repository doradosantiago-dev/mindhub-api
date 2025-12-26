package com.mindhub.api.model.follow;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

import com.mindhub.api.model.user.User;

/**
 * Entidad que representa la relación de seguimiento entre usuarios.
 *
 * Incluye la fecha en que se estableció el seguimiento y las referencias
 * al usuario que sigue y al usuario seguido.
 */

@Entity
@Table(name = "follows", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "follower_id", "followed_id" })
}, indexes = {
        @Index(name = "idx_follows_follower", columnList = "follower_id"),
        @Index(name = "idx_follows_followed", columnList = "followed_id"),
        @Index(name = "idx_follows_date", columnList = "followDate"),
        @Index(name = "idx_follows_follower_date", columnList = "follower_id, followDate"),
        @Index(name = "idx_follows_followed_date", columnList = "followed_id, followDate")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate followDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_id", nullable = false)
    private User followed;

    @PrePersist
    public void prePersist() {
        if (followDate == null) {
            followDate = LocalDate.now();
        }
    }
}
