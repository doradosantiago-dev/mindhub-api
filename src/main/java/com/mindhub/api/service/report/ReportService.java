package com.mindhub.api.service.report;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mindhub.api.dto.report.ReportRequest;
import com.mindhub.api.dto.report.ReportResponse;
import com.mindhub.api.model.enums.ReportStatus;
import com.mindhub.api.model.report.Report;
import com.mindhub.api.service.base.GenericService;

/**
 * Servicio para la gestión de reportes del sistema.
 *
 * Proporciona funcionalidades para crear, revisar y gestionar reportes
 * de posts, incluyendo validaciones de permisos y notificaciones.
 */

public interface ReportService extends GenericService<Report, Long> {

    /**
     * Crea un nuevo reporte.
     * 
     * @param request Datos del reporte
     * @return Reporte creado
     */
    ReportResponse createReport(ReportRequest request);

    /**
     * Obtiene un reporte por su ID como DTO.
     * 
     * @param id ID del reporte
     * @return Reporte encontrado
     */
    ReportResponse findByIdAsDto(Long id);

    /**
     * Obtiene los reportes pendientes paginados.
     * 
     * @param pageable Configuración de paginación
     * @return Página de reportes pendientes
     */
    Page<ReportResponse> getPendingReports(Pageable pageable);

    /**
     * Obtiene los reportes de un post específico paginados.
     * 
     * @param postId   ID del post
     * @param pageable Configuración de paginación
     * @return Página de reportes del post
     */
    Page<ReportResponse> getReportsByPost(Long postId, Pageable pageable);

    /**
     * Revisa un reporte y actualiza su estado.
     * 
     * @param id           ID del reporte
     * @param status       Nuevo estado del reporte
     * @param adminComment Comentario del administrador
     */
    void reviewReport(Long id, ReportStatus status, String adminComment);

    /**
     * Cuenta el número de reportes pendientes.
     * 
     * @return Número de reportes pendientes
     */
    long countPendingReports();

    /**
     * Cuenta el número de reportes rechazados.
     * 
     * @return Número de reportes rechazados
     */
    long countRejectedReports();

    /**
     * Cuenta el número de reportes resueltos.
     * 
     * @return Número de reportes resueltos
     */
    long countResolvedReports();

    /**
     * Obtiene todos los reportes con filtros aplicados.
     * 
     * @param status   Filtro por estado
     * @param search   Filtro de búsqueda
     * @param pageable Configuración de paginación
     * @return Página de reportes filtrados
     */
    Page<ReportResponse> getAllReportsWithFilters(String status, String search, Pageable pageable);
}
