package com.mindhub.api.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindhub.api.model.chatbot.ChatBotMessage;
import com.mindhub.api.model.comment.Comment;
import com.mindhub.api.model.enums.PrivacyType;
import com.mindhub.api.model.follow.Follow;
import com.mindhub.api.model.notification.Notification;
import com.mindhub.api.model.post.Post;
import com.mindhub.api.model.reaction.Reaction;
import com.mindhub.api.model.report.Report;
import com.mindhub.api.model.role.Role;
import com.mindhub.api.model.userProfile.UserProfile;

/**
 * Entidad que representa a un usuario del sistema.
 *
 * Incluye credenciales, datos personales, configuración de privacidad,
 * rol asignado, estado de la cuenta y auditoría de fechas de registro
 * y última actividad. Mantiene relaciones con posts, comentarios,
 * reacciones, reportes, notificaciones, mensajes de chatbot y
 * vínculos de seguimiento entre usuarios.
 */

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_username", columnList = "username"),
        @Index(name = "idx_users_email", columnList = "email"),
        @Index(name = "idx_users_active", columnList = "active"),
        @Index(name = "idx_users_role", columnList = "role_id"),
        @Index(name = "idx_users_privacy", columnList = "privacy_type"),
        @Index(name = "idx_users_first_name", columnList = "firstName"),
        @Index(name = "idx_users_last_name", columnList = "lastName"),
        @Index(name = "idx_users_active_privacy", columnList = "active, privacy_type"),
        @Index(name = "idx_users_role_active", columnList = "role_id, active")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 500, columnDefinition = "TEXT")
    private String profilePicture;

    @Column(length = 200)
    private String address;

    @Column(length = 500, columnDefinition = "TEXT")
    private String biography;

    @Enumerated(EnumType.STRING)
    @Column(name = "privacy_type", nullable = false, length = 20)
    @Builder.Default
    private PrivacyType privacyType = PrivacyType.PUBLIC;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDate registrationDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDate lastActivityDate;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Post> posts;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Reaction> reactions;

    @OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Report> reports;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private UserProfile profile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Notification> notifications;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ChatBotMessage> chatBotMessages;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Follow> following;

    @OneToMany(mappedBy = "followed", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Follow> followers;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.getName()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
