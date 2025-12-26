package com.mindhub.api.model.report;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.mindhub.api.model.enums.ReportStatus;
import com.mindhub.api.model.post.Post;
import com.mindhub.api.model.user.User;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Entidad que representa un reporte de usuario sobre un post.
 *
 * Incluye la razón, una descripción opcional, el estado del reporte
 * y las fechas de creación y revisión, además de las referencias
 * al usuario que lo emite y al post afectado.
 */

@Entity
@Table(name = "reports", uniqueConstraints = {
                @UniqueConstraint(columnNames = { "reporter_id", "post_id" })
}, indexes = {
                @Index(name = "idx_reports_reporter", columnList = "reporter_id"),
                @Index(name = "idx_reports_post", columnList = "post_id"),
                @Index(name = "idx_reports_status", columnList = "status"),
                @Index(name = "idx_reports_date", columnList = "reportDate"),
                @Index(name = "idx_reports_reporter_date", columnList = "reporter_id, reportDate"),
                @Index(name = "idx_reports_post_date", columnList = "post_id, reportDate"),
                @Index(name = "idx_reports_status_date", columnList = "status, reportDate")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, length = 500)
        private String reason;

        @Column(length = 1000)
        private String description;

        @Builder.Default
        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private ReportStatus status = ReportStatus.PENDING;

        @CreatedDate
        @Column(nullable = false)
        private LocalDateTime reportDate;

        @LastModifiedDate
        private LocalDateTime reviewDate;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "reporter_id", nullable = false)
        private User reporter;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "post_id")
        private Post post;
}
