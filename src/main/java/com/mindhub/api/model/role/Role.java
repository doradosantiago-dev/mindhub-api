package com.mindhub.api.model.role;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mindhub.api.model.user.User;

/**
 * Entidad que representa un rol de usuario en el sistema.
 *
 * Incluye nombre, descripción, icono, color, estado de activación,
 * rol por defecto y auditoría de fechas
 * de creación y actualización. Mantiene la relación con los usuarios
 * que poseen dicho rol.
 */

@Entity
@Table(name = "roles", indexes = {
        @Index(name = "idx_roles_name", columnList = "name"),
        @Index(name = "idx_roles_active", columnList = "active"),
        @Index(name = "idx_roles_default", columnList = "defaultRole"),
        @Index(name = "idx_roles_name_active", columnList = "name, active"),
        @Index(name = "idx_roles_default_active", columnList = "defaultRole, active")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @Column(length = 200)
    private String description;

    @Column(length = 100)
    private String icon;

    @Column(length = 7)
    private String color;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @Builder.Default
    @Column(nullable = false)
    private Boolean defaultRole = false;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDate creationDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDate updateDate;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<User> users;

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";

    public boolean isAdmin() {
        return ADMIN.equals(this.name);
    }

    public boolean isUser() {
        return USER.equals(this.name);
    }
}
