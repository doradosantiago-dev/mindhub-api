package com.mindhub.api.model.post;

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
import com.mindhub.api.model.comment.Comment;
import com.mindhub.api.model.enums.PrivacyType;
import com.mindhub.api.model.reaction.Reaction;
import com.mindhub.api.model.report.Report;
import com.mindhub.api.model.user.User;

/**
 * Entidad que representa un post dentro del sistema.
 *
 * Incluye el contenido, la configuración de privacidad, el autor
 * y las relaciones con comentarios, reacciones y reportes,
 * además de la auditoría de fechas de creación y actualización.
 */

@Entity
@Table(name = "posts", indexes = {
        @Index(name = "idx_posts_author", columnList = "author_id"),
        @Index(name = "idx_posts_privacy", columnList = "privacyType"),
        @Index(name = "idx_posts_date", columnList = "creationDate"),
        @Index(name = "idx_posts_author_date", columnList = "author_id, creationDate"),
        @Index(name = "idx_posts_author_privacy", columnList = "author_id, privacyType"),
        @Index(name = "idx_posts_privacy_date", columnList = "privacyType, creationDate")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(length = 1000)
    private String imageUrl;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrivacyType privacyType = PrivacyType.PUBLIC;

    @CreatedDate
    @Column(nullable = false)
    private LocalDate creationDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDate updateDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Reaction> reactions;

    @OneToMany(mappedBy = "post", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = false)
    @JsonIgnore
    private List<Report> reports;

    @PreRemove
    public void preRemove() {
        if (reports != null) {
            reports.forEach(report -> {
                // Si el reporte está pendiente, marcarlo como resuelto automáticamente
                if (report.getStatus() == com.mindhub.api.model.enums.ReportStatus.PENDING) {
                    report.setStatus(com.mindhub.api.model.enums.ReportStatus.RESOLVED);
                    report.setReviewDate(java.time.LocalDateTime.now());
                }
                // Desvincular la publicación del reporte
                report.setPost(null);
            });
        }
    }
}
