package com.mindhub.api.model.comment;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.mindhub.api.model.post.Post;
import com.mindhub.api.model.user.User;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * Entidad que representa los comentarios realizados en los posts.
 *
 * Incluye el contenido del comentario, el autor, el post asociado
 * y la auditoría de fechas de creación y actualización.
 */

@Entity
@Table(name = "comments", indexes = {
        @Index(name = "idx_comments_post", columnList = "post_id"),
        @Index(name = "idx_comments_author", columnList = "author_id"),
        @Index(name = "idx_comments_date", columnList = "creationDate"),
        @Index(name = "idx_comments_post_date", columnList = "post_id, creationDate")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String content;

    @CreatedDate
    @Column(nullable = false)
    private LocalDate creationDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDate updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}