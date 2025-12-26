package com.mindhub.api.controller.report;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindhub.api.dto.report.ReportRequest;
import com.mindhub.api.dto.report.ReportResponse;
import com.mindhub.api.model.enums.ReportStatus;
import com.mindhub.api.service.report.ReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para operaciones de reportes en la plataforma.
 *
 * Proporciona endpoints para la gestión de reportes, incluyendo:
 * - Creación de reportes por usuarios
 * - Consulta de reportes por administradores
 * - Gestión de reportes pendientes
 * - Resolución y rechazo de reportes
 * - Consulta de reportes por publicación
 *
 * Los usuarios pueden crear reportes, mientras que los administradores
 * pueden gestionarlos y resolverlos.
 */

@Slf4j
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Reportes", description = "Endpoints para gestión de reportes de contenido")
@SecurityRequirement(name = "bearerAuth")
public class ReportController {

        private final ReportService reportService;

        /**
         * Crea un nuevo reporte en la plataforma.
         * 
         * Este endpoint permite a los usuarios autenticados reportar contenido
         * inapropiado o que viole las políticas de la plataforma. Los reportes
         * se crean con estado PENDING y son revisados por administradores.
         * 
         * @param request Datos del reporte a crear (postId, motivo, descripción)
         * @return ResponseEntity con el reporte creado y código 201 (CREATED)
         */
        @PostMapping
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Crear reporte", description = "Crea un nuevo reporte de contenido inapropiado")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Reporte creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReportResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Datos del reporte inválidos"),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "404", description = "Post no encontrado")
        })
        public ResponseEntity<ReportResponse> createReport(@Valid @RequestBody ReportRequest request) {
                log.debug("Creando reporte para publicación ID: {}", request.postId());

                ReportResponse response = reportService.createReport(request);

                log.debug("Reporte creado exitosamente con ID: {}", response.id());
                return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        /**
         * Obtiene un reporte específico por su ID (solo administradores).
         * 
         * Este endpoint permite a los administradores obtener información
         * detallada de un reporte específico usando su ID único.
         * 
         * @param id ID del reporte a consultar
         * @return ResponseEntity con el reporte encontrado
         */
        @GetMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(summary = "Obtener reporte por ID", description = "Recupera un reporte específico usando su ID (solo administradores)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Reporte obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReportResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol ADMIN"),
                        @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
        })
        public ResponseEntity<ReportResponse> getReportById(
                        @Parameter(description = "ID del reporte", example = "1") @PathVariable Long id) {
                log.debug("Obteniendo reporte con ID: {}", id);

                ReportResponse response = reportService.findByIdAsDto(id);

                log.debug("Reporte obtenido exitosamente: {}", id);
                return ResponseEntity.ok(response);
        }

        /**
         * Obtiene todos los reportes pendientes de revisión (solo administradores).
         * 
         * Este endpoint permite a los administradores obtener una lista de todos
         * los reportes que están pendientes de revisión con soporte para paginación.
         * Los reportes se ordenan por fecha de creación (más recientes primero).
         * 
         * @param pageable Información de paginación (número de página, tamaño,
         *                 ordenamiento)
         * @return ResponseEntity con la página de reportes pendientes
         */
        @GetMapping("/pending")
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(summary = "Obtener reportes pendientes", description = "Recupera todos los reportes pendientes de revisión (solo administradores)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Reportes pendientes obtenidos exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReportResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol ADMIN")
        })
        public ResponseEntity<Page<ReportResponse>> getPendingReports(
                        @Parameter(description = "Parámetros de paginación", example = "page=0&size=10&sort=creationDate,desc") @PageableDefault(size = 10) Pageable pageable) {
                log.debug("Obteniendo reportes pendientes, página: {}", pageable.getPageNumber());

                Page<ReportResponse> reports = reportService.getPendingReports(pageable);

                log.debug("Reportes pendientes obtenidos: {} reportes", reports.getContent().size());
                return ResponseEntity.ok(reports);
        }

        /**
         * Obtiene todos los reportes de una publicación específica (solo
         * administradores).
         * 
         * Este endpoint permite a los administradores obtener todos los reportes
         * que han sido creados para una publicación específica con soporte para
         * paginación.
         * Es útil para evaluar la gravedad de los problemas en una publicación.
         * 
         * @param postId   ID de la publicación cuyos reportes se consultan
         * @param pageable Información de paginación (número de página, tamaño,
         *                 ordenamiento)
         * @return ResponseEntity con la página de reportes de la publicación
         */
        @GetMapping("/post/{postId}")
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(summary = "Obtener reportes de una publicación", description = "Recupera todos los reportes de una publicación específica (solo administradores)")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Reportes de la publicación obtenidos exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReportResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol ADMIN"),
                        @ApiResponse(responseCode = "404", description = "Publicación no encontrada")
        })
        public ResponseEntity<Page<ReportResponse>> getReportsByPost(
                        @Parameter(description = "ID de la publicación", example = "1") @PathVariable Long postId,
                        @Parameter(description = "Parámetros de paginación", example = "page=0&size=10&sort=creationDate,desc") @PageableDefault(size = 10) Pageable pageable) {
                log.debug("Obteniendo reportes para publicación ID: {}, página: {}", postId, pageable.getPageNumber());

                Page<ReportResponse> reports = reportService.getReportsByPost(postId, pageable);

                log.debug("Reportes obtenidos: {} para publicación ID: {}", reports.getContent().size(), postId);
                return ResponseEntity.ok(reports);
        }

        /**
         * Resuelve un reporte pendiente (solo administradores).
         * 
         * Este endpoint permite a los administradores resolver un reporte pendiente,
         * marcándolo como RESOLVED y aplicando las acciones correspondientes
         * (como eliminar la publicación reportada).
         * 
         * @param id ID del reporte a resolver
         * @return ResponseEntity con mensaje de confirmación
         */
        @PutMapping("/{id}/resolve")
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(summary = "Resolver reporte", description = "Resuelve un reporte pendiente y aplica acciones correspondientes")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Reporte resuelto exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                                        {
                                          "message": "Reporte resuelto y publicación eliminada"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol ADMIN"),
                        @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
        })
        public ResponseEntity<Map<String, String>> resolveReport(
                        @Parameter(description = "ID del reporte", example = "1") @PathVariable Long id) {
                log.debug("Resolviendo reporte con ID: {}", id);

                reportService.reviewReport(id, ReportStatus.RESOLVED, "Reporte resuelto por administrador");

                log.debug("Reporte resuelto exitosamente: {}", id);
                return ResponseEntity.ok(Map.of("message", "Reporte resuelto y publicación eliminada"));
        }

        /**
         * Rechaza un reporte pendiente (solo administradores).
         * 
         * Este endpoint permite a los administradores rechazar un reporte pendiente,
         * marcándolo como REJECTED cuando se considera que el contenido reportado
         * no viola las políticas de la plataforma.
         * 
         * @param id ID del reporte a rechazar
         * @return ResponseEntity con mensaje de confirmación
         */
        @PutMapping("/{id}/reject")
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(summary = "Rechazar reporte", description = "Rechaza un reporte pendiente cuando el contenido no viola políticas")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Reporte rechazado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                                        {
                                          "message": "Reporte rechazado"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol ADMIN"),
                        @ApiResponse(responseCode = "404", description = "Reporte no encontrado")
        })
        public ResponseEntity<Map<String, String>> rejectReport(
                        @Parameter(description = "ID del reporte", example = "1") @PathVariable Long id) {
                log.debug("Rechazando reporte con ID: {}", id);

                reportService.reviewReport(id, ReportStatus.REJECTED, "Reporte rechazado por administrador");

                log.debug("Reporte rechazado exitosamente: {}", id);
                return ResponseEntity.ok(Map.of("message", "Reporte rechazado"));
        }

}
