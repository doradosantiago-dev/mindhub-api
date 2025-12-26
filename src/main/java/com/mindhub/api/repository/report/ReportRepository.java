package com.mindhub.api.repository.report;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mindhub.api.model.enums.ReportStatus;
import com.mindhub.api.model.post.Post;
import com.mindhub.api.model.report.Report;
import com.mindhub.api.model.user.User;

/**
 * Repositorio para la gestión de reportes del sistema.
 *
 * Proporciona métodos para consultar, contar y administrar los reportes
 * realizados por usuarios sobre publicaciones, incluyendo filtrado por estado,
 * búsqueda avanzada y conteo optimizado.
 */

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

       /**
        * Busca reportes pendientes con paginación.
        * 
        * Retorna todos los reportes con estado pendiente ordenados
        * por fecha de reporte descendente (más recientes primero).
        * 
        * 
        * @param status   Estado del reporte
        * @param pageable Configuración de paginación
        * @return Página de reportes pendientes ordenados por fecha
        * 
        * @see Report
        * @see ReportStatus
        */
       @Query("SELECT r FROM Report r WHERE r.status = :status ORDER BY r.reportDate DESC")
       Page<Report> findPendingReports(@Param("status") ReportStatus status, Pageable pageable);

       /**
        * Cuenta el número total de reportes pendientes.
        * 
        * Método optimizado para obtener el conteo de reportes
        * pendientes sin cargar todos los datos.
        * 
        * 
        * @return Número total de reportes pendientes
        * 
        * @see ReportStatus
        */
       @Query("SELECT COUNT(r) FROM Report r WHERE r.status = 'PENDING'")
       long countPendingReports();

       /**
        * Busca reportes de un post específico con paginación.
        * 
        * Retorna todos los reportes de un post ordenados
        * por fecha de reporte descendente.
        * 
        * 
        * @param post     Post del cual obtener los reportes
        * @param pageable Configuración de paginación
        * @return Página de reportes del post ordenados por fecha
        * 
        * @see Report
        * @see Post
        */
       @Query("SELECT r FROM Report r WHERE r.post = :post ORDER BY r.reportDate DESC")
       Page<Report> findByPostOrderByReportDateDesc(@Param("post") Post post, Pageable pageable);

       /**
        * Verifica si existe un reporte de un usuario en un post.
        * 
        * @param reporter Usuario que realizó el reporte
        * @param post     Post que fue reportado
        * @return true si existe el reporte, false en caso contrario
        * 
        * @see User
        * @see Post
        */
       boolean existsByReporterAndPost(User reporter, Post post);

       /**
        * Cuenta el número total de reportes rechazados.
        * 
        * Método optimizado para obtener el conteo de reportes
        * rechazados sin cargar todos los datos.
        * 
        * 
        * @return Número total de reportes rechazados
        * 
        * @see ReportStatus
        */
       @Query("SELECT COUNT(r) FROM Report r WHERE r.status = 'REJECTED'")
       long countRejectedReports();

       /**
        * Cuenta el número total de reportes resueltos.
        * 
        * Método optimizado para obtener el conteo de reportes
        * resueltos sin cargar todos los datos.
        * 
        * 
        * @return Número total de reportes resueltos
        * 
        * @see ReportStatus
        */
       @Query("SELECT COUNT(r) FROM Report r WHERE r.status = 'RESOLVED'")
       long countResolvedReports();

       /**
        * Busca reportes con filtros avanzados.
        * 
        * Retorna reportes filtrados por estado y búsqueda en texto,
        * incluyendo búsqueda en razón, descripción y datos del reportero.
        * 
        * 
        * @param status   Estado del reporte (opcional)
        * @param search   Término de búsqueda (opcional)
        * @param pageable Configuración de paginación
        * @return Página de reportes filtrados ordenados por fecha
        * 
        * @see Report
        * @see ReportStatus
        */
       @Query("SELECT r FROM Report r WHERE " +
                     "(:status IS NULL OR r.status = :status) AND " +
                     "(:search IS NULL OR :search = '' OR " +
                     "LOWER(r.reason) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                     "LOWER(r.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                     "LOWER(r.reporter.username) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                     "LOWER(r.reporter.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                     "LOWER(r.reporter.lastName) LIKE LOWER(CONCAT('%', :search, '%'))) " +
                     "ORDER BY r.reportDate DESC")
       Page<Report> findReportsWithFilters(@Param("status") String status,
                     @Param("search") String search,
                     Pageable pageable);
}
