package com.mindhub.api.model.userProfile;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindhub.api.model.user.User;

/**
 * Entidad que representa el perfil extendido de un usuario.
 *
 * Incluye información adicional como fecha de nacimiento, ocupación,
 * intereses, sitio web, ubicación, redes sociales, educación,
 * lugar de trabajo y auditoría de fechas de creación y actualización.
 */

@Entity
@Table(name = "user_profiles", indexes = {
        @Index(name = "idx_user_profiles_user", columnList = "user_id"),
        @Index(name = "idx_user_profiles_birth_date", columnList = "birth_date"),
        @Index(name = "idx_user_profiles_location", columnList = "location"),
        @Index(name = "idx_user_profiles_occupation", columnList = "occupation"),
        @Index(name = "idx_user_profiles_creation_date", columnList = "creation_date")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(length = 100)
    private String occupation;

    @Column(length = 500)
    private String interests;

    @Column(length = 255)
    private String website;

    @Column(length = 100)
    private String location;

    @Column(name = "social_media", length = 255)
    private String socialMedia;

    @Column(length = 200)
    private String education;

    @Column(length = 200)
    private String workplace;

    @CreatedDate
    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDate updateDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
}
