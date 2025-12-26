package com.mindhub.api.mapper.report;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.mindhub.api.dto.post.PostSummaryResponse;
import com.mindhub.api.dto.report.ReportRequest;
import com.mindhub.api.dto.report.ReportResponse;
import com.mindhub.api.mapper.user.UserMapper;
import com.mindhub.api.model.post.Post;
import com.mindhub.api.model.report.Report;
import com.mindhub.api.repository.post.PostRepository;

/**
 * Mapper encargado de transformar entidades Report en sus DTOs asociados.
 *
 * Este mapper no utiliza MapStruct porque requiere lógica adicional, como la
 * verificación de la existencia del post en la base de datos
 * y la construcción de un resumen del mismo.
 *
 * Sus funcionalidades principales incluyen la conversión de una entidad Report
 * en un ReportResponse,
 * la transformación de un ReportRequest en una entidad Report lista para
 * persistir,
 * y la generación de un PostSummaryResponse con información básica del post
 * asociado.
 */

@Component
public class ReportMapper {

    private final UserMapper userMapper;
    private final PostRepository postRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param userMapper     mapper de usuarios para convertir autores y reporteros
     * @param postRepository repositorio de posts para verificar existencia de
     *                       publicaciones
     */
    public ReportMapper(UserMapper userMapper, PostRepository postRepository) {
        this.userMapper = userMapper;
        this.postRepository = postRepository;
    }

    /**
     * Convierte una entidad Report en un DTO ReportResponse.
     *
     * @param report entidad Report a convertir
     * @return DTO con los datos del reporte y un resumen del post asociado
     */
    public ReportResponse toResponse(Report report) {
        if (report == null) {
            return null;
        }

        PostSummaryResponse postSummary = createPostSummary(report);

        return new ReportResponse(
                report.getId(),
                report.getReason(),
                report.getDescription(),
                report.getStatus(),
                report.getReportDate(),
                report.getReviewDate(),
                userMapper.toResponse(report.getReporter()),
                postSummary);
    }

    /**
     * Genera un PostSummaryResponse a partir del post asociado a un reporte.
     *
     * Si el post fue eliminado, devuelve un resumen con valores por defecto.
     * Si el post existe, se incluye información básica y el autor.
     *
     * @param report reporte que contiene el post a resumir
     * @return DTO con información resumida del post
     */
    private PostSummaryResponse createPostSummary(Report report) {
        Post reportedPost = report.getPost();
        if (reportedPost == null) {

            return new PostSummaryResponse(
                    0L,
                    "Post eliminado",
                    null,
                    false);
        }

        // Verificación de null safety para el ID del post
        Long postId = reportedPost.getId();
        if (postId == null) {
            return new PostSummaryResponse(
                    0L,
                    "Post con ID inválido",
                    null,
                    false);
        }

        boolean postExists = postRepository.existsById(postId);

        return new PostSummaryResponse(
                reportedPost.getId(),
                reportedPost.getContent(),
                userMapper.toResponse(reportedPost.getAuthor()),
                postExists);
    }

    /**
     * Convierte un ReportRequest en una entidad Report.
     *
     * Se asigna la fecha actual como fecha de creación del reporte.
     *
     * @param request DTO con los datos del reporte
     * @return entidad Report lista para persistir
     */
    public Report toEntity(ReportRequest request) {
        if (request == null) {
            return null;
        }

        return Report.builder()
                .reason(request.reason())
                .description(request.description())
                .reportDate(LocalDateTime.now())
                .build();
    }
}
