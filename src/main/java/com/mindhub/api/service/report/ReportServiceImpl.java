package com.mindhub.api.service.report;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mindhub.api.dto.report.ReportRequest;
import com.mindhub.api.dto.report.ReportResponse;
import com.mindhub.api.exception.PostDeletionException;
import com.mindhub.api.exception.PostNotFoundException;
import com.mindhub.api.exception.ReportNotFoundException;
import com.mindhub.api.exception.ReportReviewException;
import com.mindhub.api.mapper.report.ReportMapper;
import com.mindhub.api.model.enums.ActionType;
import com.mindhub.api.model.enums.NotificationType;
import com.mindhub.api.model.enums.ReportStatus;
import com.mindhub.api.model.post.Post;
import com.mindhub.api.model.report.Report;
import com.mindhub.api.model.role.Role;
import com.mindhub.api.model.user.User;
import com.mindhub.api.repository.comment.CommentRepository;
import com.mindhub.api.repository.post.PostRepository;
import com.mindhub.api.repository.reaction.ReactionRepository;
import com.mindhub.api.repository.report.ReportRepository;
import com.mindhub.api.repository.user.UserRepository;
import com.mindhub.api.service.admin.AdminActionService;
import com.mindhub.api.service.base.GenericServiceImpl;
import com.mindhub.api.service.notification.NotificationService;
import com.mindhub.api.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio de reportes.
 *
 * Gestiona la creación, revisión y consulta de reportes,
 * incluyendo validaciones de permisos, notificaciones y
 * eliminación de publicaciones reportadas.
 */

@Slf4j
@Service
@Transactional
public class ReportServiceImpl extends GenericServiceImpl<Report, Long> implements ReportService {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReactionRepository reactionRepository;
    private final UserRepository userRepository;
    private final ReportMapper reportMapper;
    private final UserService userService;
    private final NotificationService notificationService;
    private final AdminActionService adminActionService;

    public ReportServiceImpl(ReportRepository reportRepository,
            PostRepository postRepository,
            CommentRepository commentRepository,
            ReactionRepository reactionRepository,
            UserRepository userRepository,
            ReportMapper reportMapper,
            UserService userService,
            NotificationService notificationService,
            AdminActionService adminActionService) {
        super(reportRepository);
        this.reportRepository = reportRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.reactionRepository = reactionRepository;
        this.userRepository = userRepository;
        this.reportMapper = reportMapper;
        this.userService = userService;
        this.notificationService = notificationService;
        this.adminActionService = adminActionService;
    }

    /**
     * Crea un nuevo reporte.
     * 
     * @param request Datos del reporte
     * @return Reporte creado
     */
    @Override
    public ReportResponse createReport(ReportRequest request) {
        User currentUser = userService.getCurrentUser();

        log.debug("Usuario {} intenta crear un reporte para el post {}", currentUser.getId(), request.postId());

        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new PostNotFoundException("Post no encontrado"));

        if (post.getAuthor().getId().equals(currentUser.getId())) {
            log.warn("El usuario {} intentó reportar su propio post {}", currentUser.getId(), post.getId());

            throw new IllegalStateException("No puedes reportar tus propios post");
        }

        if (reportRepository.existsByReporterAndPost(currentUser, post)) {
            log.warn("El usuario {} ya había reportado el post {}", currentUser.getId(), post.getId());

            throw new IllegalStateException("Ya has reportado este post antes");
        }

        Report report = reportMapper.toEntity(request);

        report.setReporter(currentUser);

        report.setPost(post);

        report.setStatus(ReportStatus.PENDING);

        Report savedReport = save(report);

        log.info("Reporte {} creado por el usuario {} sobre la publicación {}", savedReport.getId(),
                currentUser.getId(),
                post.getId());

        createAdminNotificationForReport(savedReport);

        userService.updateLastActivity(currentUser.getId());

        return reportMapper.toResponse(savedReport);
    }

    /**
     * Obtiene un reporte por su ID como DTO.
     * 
     * @param id ID del reporte
     * @return Reporte encontrado
     */
    @Override
    @Transactional(readOnly = true)
    public ReportResponse findByIdAsDto(Long id) {
        log.debug("Buscando reporte con id {}", id);

        Report report = findByIdOrThrow(id);

        User currentUser = userService.getCurrentUser();

        if (!report.getReporter().getId().equals(currentUser.getId()) && !userService.isCurrentUserAdmin()) {
            log.error("El usuario {} intentó acceder a un reporte sin permisos", currentUser.getId());

            throw new IllegalStateException("No tienes permisos para ver este reporte");
        }

        return reportMapper.toResponse(report);
    }

    /**
     * Obtiene los reportes pendientes paginados.
     * 
     * @param pageable Configuración de paginación
     * @return Página de reportes pendientes
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReportResponse> getPendingReports(Pageable pageable) {
        log.debug("Consultando reportes pendientes");

        if (!userService.isCurrentUserAdmin()) {
            log.error("Acceso denegado: usuario sin permisos intentó ver reportes pendientes");

            throw new IllegalStateException("Solo los admins pueden ver reportes pendientes");
        }

        return reportRepository.findPendingReports(ReportStatus.PENDING, pageable)
                .map(reportMapper::toResponse);
    }

    /**
     * Obtiene los reportes de una publicación específica paginados.
     * 
     * @param postId   ID de la publicación
     * @param pageable Configuración de paginación
     * @return Página de reportes de la publicación
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReportResponse> getReportsByPost(Long postId, Pageable pageable) {
        log.debug("Consultando reportes de la publicación {}", postId);

        if (!userService.isCurrentUserAdmin()) {
            log.error("Acceso denegado: usuario sin permisos intentó ver reportes de una publicación");

            throw new IllegalStateException("Solo los admins pueden ver reportes por publicación");
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Publicación no encontrada"));
        return reportRepository.findByPostOrderByReportDateDesc(post, pageable)
                .map(reportMapper::toResponse);
    }

    /**
     * Revisa un reporte y actualiza su estado.
     * 
     * @param id           ID del reporte
     * @param status       Nuevo estado del reporte
     * @param adminComment Comentario del administrador
     */
    @Override
    @Transactional
    public void reviewReport(Long id, ReportStatus status, String adminComment) {
        log.debug("Revisando reporte {} con nuevo estado {}", id, status);

        User currentUser = validateAdminPermissions();

        Report report = validateReportForReview(id);

        updateReportStatus(report, status);

        log.info("Reporte {} actualizado a estado {} por admin {}", id, status, currentUser.getId());

        logAdminAction(currentUser, report, status, adminComment);

        if (status == ReportStatus.RESOLVED) {
            log.info("El reporte {} fue resuelto, se eliminará la publicación {}", id, report.getPost().getId());

            deleteReportedPost(report, currentUser);
        }

        notifyReporter(report, status);
    }

    /**
     * Cuenta el número de reportes pendientes.
     * 
     * @return Número de reportes pendientes
     */
    @Override
    @Transactional(readOnly = true)
    public long countPendingReports() {

        log.debug("Contando reportes pendientes");

        return reportRepository.countPendingReports();
    }

    /**
     * Cuenta el número de reportes rechazados.
     * 
     * @return Número de reportes rechazados
     */
    @Override
    @Transactional(readOnly = true)
    public long countRejectedReports() {
        log.debug("Contando reportes rechazados");

        return reportRepository.countRejectedReports();
    }

    /**
     * Cuenta el número de reportes resueltos.
     * 
     * @return Número de reportes resueltos
     */
    @Override
    @Transactional(readOnly = true)
    public long countResolvedReports() {
        log.debug("Contando reportes resueltos");

        return reportRepository.countResolvedReports();
    }

    /**
     * Obtiene todos los reportes con filtros aplicados.
     * 
     * @param status   Filtro por estado
     * @param search   Filtro de búsqueda
     * @param pageable Configuración de paginación
     * @return Página de reportes filtrados
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReportResponse> getAllReportsWithFilters(String status, String search, Pageable pageable) {
        log.debug("Buscando reportes con filtros -> status: {}, search: {}", status, search);

        if (!userService.isCurrentUserAdmin()) {
            log.error("Acceso denegado: usuario sin permisos intentó ver todos los reportes");

            throw new IllegalStateException("Solo los administradores pueden ver todos los reportes");
        }

        Page<Report> reports = reportRepository.findReportsWithFilters(status, search, pageable);

        log.info("Se encontraron {} reportes con los filtros aplicados", reports.getTotalElements());

        return reports.map(reportMapper::toResponse);
    }

    /**
     * Valida que el usuario actual tenga permisos de administrador.
     * 
     * @return Usuario actual
     */
    private User validateAdminPermissions() {
        User currentUser = userService.getCurrentUser();

        if (!userService.isCurrentUserAdmin()) {
            log.error("El usuario {} intentó revisar reportes sin permisos", currentUser.getId());

            throw new ReportReviewException("Solo los administradores pueden revisar los reportes.");
        }

        return currentUser;
    }

    /**
     * Valida que el reporte esté pendiente para revisión.
     * 
     * @param id ID del reporte
     * @return Reporte validado
     */
    private Report validateReportForReview(Long id) {
        Report report = findByIdOrThrow(id);

        if (report.getStatus() != ReportStatus.PENDING) {
            log.warn("Intento de revisar un reporte {} que no está pendiente (estado actual: {})", id,
                    report.getStatus());

            throw new ReportReviewException("Solo se pueden revisar los reportes pendientes");
        }

        return report;
    }

    /**
     * Actualiza el estado de un reporte.
     * 
     * @param report Reporte a actualizar
     * @param status Nuevo estado
     */
    private void updateReportStatus(Report report, ReportStatus status) {
        report.setStatus(status);

        report.setReviewDate(LocalDateTime.now());

        save(report);

        log.info("Reporte {} actualizado a estado {}", report.getId(), status);
    }

    /**
     * Elimina la publicación reportada.
     * 
     * @param report Reporte que causó la eliminación
     * @param admin  Administrador que realiza la acción
     */
    private void deleteReportedPost(Report report, User admin) {
        Post postToDelete = report.getPost();

        User postAuthor = postToDelete.getAuthor();

        if (postRepository.existsById(postToDelete.getId())) {
            log.info("Eliminando publicación {} reportada por usuario {}", postToDelete.getId(),
                    report.getReporter().getId());

            deletePostAndRelatedData(postToDelete, admin, postAuthor);
        }
    }

    /**
     * Elimina una publicación y todos sus datos relacionados.
     * 
     * @param post       Publicación a eliminar
     * @param admin      Administrador que realiza la acción
     * @param postAuthor Autor de la publicación
     */
    private void deletePostAndRelatedData(Post post, User admin, User postAuthor) {
        log.debug("Eliminando datos relacionados de la publicación {}", post.getId());

        commentRepository.deleteByPost(post);

        reactionRepository.deleteByPost(post);

        postRepository.deleteById(post.getId());

        if (!postRepository.existsById(post.getId())) {
            log.info("Publicación {} eliminada correctamente por el admin {}", post.getId(), admin.getId());

            logPostDeletionAction(admin, post, postAuthor);

            notifyPostAuthor(postAuthor);
        } else {
            log.error("No se pudo eliminar la publicación {}", post.getId());
            throw new PostDeletionException("No se pudo eliminar la publicación con ID: " + post.getId());
        }
    }

    /**
     * Registra la acción de eliminación de la publicación.
     * 
     * @param admin      Administrador que realiza la acción
     * @param post       Publicación eliminada
     * @param postAuthor Autor de la publicación
     */
    private void logPostDeletionAction(User admin, Post post, User postAuthor) {
        String postContent = post.getContent().length() > 100
                ? post.getContent().substring(0, 100) + "..."
                : post.getContent();

        log.debug("Registrando acción de eliminación de la publicación {} por admin {}", post.getId(), admin.getId());

        adminActionService.logAction(admin, ActionType.DELETE_POST,
                "Publicación eliminada por reporte",
                "Publicación eliminada debido a un reporte resuelto - Contenido: " + postContent,
                post.getId(), "posts", postAuthor);
    }

    /**
     * Notifica al autor del post sobre su eliminación.
     * 
     * @param postAuthor Autor del post
     */
    private void notifyPostAuthor(User postAuthor) {
        log.debug("Notificando al autor {} sobre la eliminación de su post", postAuthor.getId());

        notificationService.createNotification(postAuthor,
                "Publicación eliminada",
                "Tu publicación ha sido eliminada por infringir las normas de la comunidad",
                NotificationType.ADMIN_ACTION);
    }

    /**
     * Registra la acción del administrador.
     * 
     * @param admin        Administrador que realiza la acción
     * @param report       Reporte revisado
     * @param status       Estado del reporte
     * @param adminComment Comentario del administrador
     */
    private void logAdminAction(User admin, Report report, ReportStatus status, String adminComment) {
        String actionTitle = status == ReportStatus.RESOLVED
                ? "Reporte resuelto y publicación eliminada"
                : "Reporte rechazado";

        String actionDescription = String.format("Reporte %s - Razón: %s - Comentario del admin: %s",
                status.toString().toLowerCase(), report.getReason(), adminComment);

        ActionType actionType = status == ReportStatus.RESOLVED
                ? ActionType.RESOLVE_REPORT
                : ActionType.REJECT_REPORT;

        log.debug("Registrando acción de admin {} sobre reporte {} con estado {}", admin.getId(), report.getId(),
                status);

        // Para reportes, el affected user debe ser el autor del post reportado, no el
        // reporter
        User postAuthor = report.getPost() != null ? report.getPost().getAuthor() : null;

        adminActionService.logAction(admin, actionType,
                actionTitle, actionDescription, report.getId(), "reports",
                postAuthor);
    }

    /**
     * Notifica al reporter sobre el resultado de su reporte.
     * 
     * @param report Reporte revisado
     * @param status Estado del reporte
     */
    private void notifyReporter(Report report, ReportStatus status) {
        String message = status == ReportStatus.RESOLVED
                ? "Tu reporte ha sido resuelto. La publicación ha sido eliminada."
                : "Tu reporte ha sido revisado y rechazado.";

        log.debug("Notificando al usuario {} sobre el resultado del reporte {}", report.getReporter().getId(),
                report.getId());

        notificationService.createNotification(report.getReporter(),
                "Reporte revisado", message, NotificationType.ADMIN_ACTION);
    }

    /**
     * Crea notificaciones para todos los administradores sobre un nuevo reporte.
     * 
     * @param report Reporte creado
     */
    private void createAdminNotificationForReport(Report report) {
        log.debug("Creando notificación para administradores sobre el reporte {}", report.getId());

        List<User> allUsers = userRepository.findAll();

        List<User> adminUsers = allUsers.stream()
                .filter(user -> user.getRole() != null &&
                        Role.ADMIN.equals(user.getRole().getName()) &&
                        user.getActive())
                .collect(Collectors.toList());

        if (adminUsers.isEmpty()) {
            log.warn("No hay administradores activos para notificar sobre el reporte {}", report.getId());
            return;
        }

        String title = "Nuevo reporte pendiente";
        String message = String.format("Se ha recibido un nuevo reporte: %s",
                truncateText(report.getReason(), 50));

        adminUsers.forEach(admin -> {
            log.debug("Notificando al admin {} sobre el reporte {}", admin.getId(), report.getId());

            notificationService.createNotificationWithReference(
                    admin,
                    title,
                    message,
                    NotificationType.ADMIN_ACTION,
                    report.getId(),
                    "reports");
        });
    }

    /**
     * Trunca un texto a una longitud máxima.
     * 
     * @param text      Texto a truncar
     * @param maxLength Longitud máxima
     * @return Texto truncado
     */
    private String truncateText(String text, int maxLength) {
        if (text == null) {
            log.warn("Texto nulo recibido para truncar");

            return "Sin razón especificada";
        }
        return text.length() > maxLength ? text.substring(0, maxLength) + "..." : text;
    }

    /**
     * Obtiene el identificador único de la entidad Report.
     *
     * @param entity entidad Report
     * @return identificador de la entidad
     */
    @Override
    protected Long getEntityId(Report entity) {
        return entity.getId();
    }

    /**
     * Crea una excepción específica cuando no se encuentra un reporte.
     *
     * @param message mensaje descriptivo del error
     * @return excepción de tipo ReportNotFoundException
     */
    @Override
    protected RuntimeException createNotFoundException(String message) {
        return new ReportNotFoundException(message);
    }

    /**
     * Devuelve el nombre de la entidad gestionada por este servicio.
     *
     * @return nombre de la entidad ("Report")
     */
    @Override
    public String getEntityName() {
        return "Report";
    }
}
