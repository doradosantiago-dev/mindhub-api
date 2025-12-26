package com.mindhub.api.controller.admin;

import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mindhub.api.dto.admin.AdminActionResponse;
import com.mindhub.api.dto.auth.UserResponse;
import com.mindhub.api.dto.report.ReportResponse;
import com.mindhub.api.service.admin.AdminActionService;
import com.mindhub.api.service.post.PostService;
import com.mindhub.api.service.report.ReportService;
import com.mindhub.api.service.user.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para operaciones administrativas del sistema MindHub.
 *
 * Proporciona endpoints para la administración del sistema, incluyendo
 * estadísticas del dashboard administrativo, gestión y listado de usuarios,
 * seguimiento de acciones administrativas y gestión con filtrado de reportes.
 *
 * Todos los endpoints requieren autenticación con rol ADMIN y están protegidos
 * mediante anotaciones de seguridad a nivel de método de Spring Security.
 *
 * El acceso está restringido exclusivamente a usuarios con rol ADMIN para
 * garantizar la seguridad y el control del sistema.
 */

@RestController
@RequestMapping("/api/admin")
@Slf4j
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Administración", description = "Endpoints para gestión administrativa del sistema")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

        private final UserService userService;
        private final PostService postService;
        private final ReportService reportService;
        private final AdminActionService adminActionService;

        /**
         * Construye un AdminController con las dependencias de servicios requeridas.
         * 
         * Utiliza la anotación @Lazy para prevenir problemas de dependencias
         * circulares durante la inicialización de beans de Spring.
         * 
         * @param userService        Servicio para operaciones de gestión de usuarios
         * @param postService        Servicio para operaciones de gestión de
         *                           publicaciones
         * @param reportService      Servicio para operaciones de gestión de reportes
         * @param adminActionService Servicio para seguimiento de acciones
         *                           administrativas
         */
        public AdminController(@Lazy UserService userService,
                        @Lazy PostService postService,
                        @Lazy ReportService reportService,
                        @Lazy AdminActionService adminActionService) {
                this.userService = userService;
                this.postService = postService;
                this.reportService = reportService;
                this.adminActionService = adminActionService;
        }

        /**
         * Obtiene estadísticas completas del dashboard para la vista general
         * administrativa.
         *
         * Este endpoint proporciona estadísticas en tiempo real que incluyen:
         * Conteos totales de usuarios (activos, inactivos y total)
         * Total de publicaciones en el sistema
         * Estadísticas de reportes (pendientes, rechazados y resueltos)
         *
         * Estas métricas son utilizadas por el dashboard de administración para
         * ofrecer una visión general del estado y la actividad del sistema.
         *
         * @return ResponseEntity que contiene un Map con las estadísticas del dashboard
         */
        @GetMapping("/dashboard")
        @Operation(summary = "Obtener estadísticas del dashboard", description = "Retorna estadísticas en tiempo real del sistema incluyendo conteos de usuarios, publicaciones y reportes")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                                        {
                                          "totalUsuarios": 150,
                                          "usuariosActivos": 120,
                                          "usuariosInactivos": 30,
                                          "totalPublicaciones": 450,
                                          "reportesPendientes": 5,
                                          "reportesRechazados": 12,
                                          "reportesResueltos": 28
                                        }
                                        """))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol ADMIN")
        })
        public ResponseEntity<Map<String, Object>> getDashboardStats() {
                log.debug("Obteniendo estadísticas del dashboard para administrador");

                long totalUsuarios = userService.countTotalUsers();
                long usuariosActivos = userService.countActiveUsers();
                long usuariosInactivos = userService.countInactiveUsers();
                long totalPosts = postService.countTotalPosts();
                long reportesPendientes = reportService.countPendingReports();
                long reportesRechazados = reportService.countRejectedReports();
                long reportesResueltos = reportService.countResolvedReports();

                Map<String, Object> stats = Map.of(
                                "totalUsuarios", totalUsuarios,
                                "usuariosActivos", usuariosActivos,
                                "usuariosInactivos", usuariosInactivos,
                                "totalPublicaciones", totalPosts,
                                "reportesPendientes", reportesPendientes,
                                "reportesRechazados", reportesRechazados,
                                "reportesResueltos", reportesResueltos);

                log.debug("Estadísticas del dashboard obtenidas exitosamente: {}", stats);

                return ResponseEntity.ok(stats);
        }

        /**
         * Obtiene una lista paginada de todos los usuarios en el sistema con búsqueda
         * opcional.
         * 
         * Este endpoint soporta paginación y búsqueda por nombre o apellido.
         * Es utilizado por el panel de administración para operaciones de gestión de
         * usuarios.
         * La respuesta incluye detalles de usuarios convertidos a DTOs para consumo del
         * frontend.
         * 
         * Si se proporciona el parámetro 'query', filtra los usuarios por nombre o
         * apellido.
         * Si no se proporciona, retorna todos los usuarios.
         * 
         * El tamaño de página por defecto es de 10 usuarios por página, pero puede ser
         * personalizado a través del parámetro pageable.
         * 
         * @param query    Término de búsqueda opcional para filtrar por nombre o
         *                 apellido
         * @param pageable Información de paginación (número de página, tamaño,
         *                 ordenamiento)
         * @return ResponseEntity que contiene una Page de objetos UserResponse
         */
        @GetMapping("/users")
        @Operation(summary = "Obtener lista de usuarios", description = "Retorna una lista paginada de todos los usuarios del sistema con búsqueda opcional")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol ADMIN")
        })
        public ResponseEntity<Page<UserResponse>> getAllUsers(
                        @Parameter(description = "Término de búsqueda para filtrar por nombre o apellido", example = "ana lopez") @RequestParam(required = false) String query,
                        @Parameter(description = "Parámetros de paginación", example = "page=0&size=10&sort=id,desc") @PageableDefault(size = 10) Pageable pageable) {
                log.debug("Obteniendo página de usuarios: {}, query: '{}'", pageable.getPageNumber(), query);

                Page<UserResponse> users;

                if (query != null && !query.trim().isEmpty()) {
                        log.debug("Buscando usuarios con término: '{}'", query.trim());
                        users = userService.searchAllUsers(query.trim(), pageable);
                } else {
                        log.debug("Obteniendo todos los usuarios sin filtro");
                        users = userService.findAllUsersAsDto(pageable);
                }

                log.debug("Obtenidos {} usuarios para la página {}", users.getContent().size(),
                                pageable.getPageNumber());

                return ResponseEntity.ok(users);
        }

        /**
         * Obtiene una lista paginada de todas las acciones administrativas.
         * 
         * Este endpoint proporciona acceso al historial de auditoría de acciones
         * administrativas realizadas en el sistema. Es utilizado para responsabilidad
         * y propósitos de monitoreo en el dashboard de administración.
         * 
         * El tamaño de página por defecto es de 10 acciones por página, pero puede ser
         * personalizado a través del parámetro pageable.
         * 
         * @param pageable Información de paginación (número de página, tamaño,
         *                 ordenamiento)
         * @return ResponseEntity que contiene una Page de objetos AdminActionResponse
         */
        @GetMapping("/actions")
        @Operation(summary = "Obtener acciones administrativas", description = "Retorna el historial de auditoría de acciones administrativas realizadas en el sistema")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de acciones administrativas obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdminActionResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol ADMIN")
        })
        public ResponseEntity<Page<AdminActionResponse>> getAdminActions(
                        @Parameter(description = "Parámetros de paginación", example = "page=0&size=10&sort=actionDate,desc") @PageableDefault(size = 10) Pageable pageable) {
                log.debug("Obteniendo página de acciones administrativas: {}", pageable.getPageNumber());

                Page<AdminActionResponse> actions = adminActionService.getAllActions(pageable);

                log.debug("Obtenidas {} acciones administrativas para la página {}", actions.getContent().size(),
                                pageable.getPageNumber());

                return ResponseEntity.ok(actions);
        }

        /**
         * Obtiene una lista paginada de reportes con filtros opcionales.
         *
         * Este endpoint permite a los administradores consultar reportes registrados
         * en el sistema. Soporta filtrado por estado (PENDING, RESOLVED, REJECTED) y
         * búsqueda por términos en el contenido del reporte, lo que lo hace adecuado
         * para la gestión de reportes en el panel de administración.
         *
         * El filtrado y la paginación se procesan en la capa de servicio para
         * garantizar un rendimiento óptimo.
         *
         * @param status   Filtro opcional por estado del reporte (PENDING, RESOLVED,
         *                 REJECTED)
         * @param search   Término de búsqueda opcional aplicado al contenido del
         *                 reporte
         * @param pageable Información de paginación (número de página, tamaño,
         *                 ordenamiento)
         * @return ResponseEntity que contiene una página de objetos ReportResponse
         */
        @GetMapping("/reports")
        @Operation(summary = "Obtener reportes con filtros", description = "Retorna una lista paginada de reportes con filtros opcionales por estado y búsqueda")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Lista de reportes obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReportResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "403", description = "Prohibido - Se requiere rol ADMIN")
        })
        public ResponseEntity<Page<ReportResponse>> getAllReports(
                        @Parameter(description = "Filtro por estado del reporte", example = "PENDING") @RequestParam(required = false) String status,
                        @Parameter(description = "Término de búsqueda en el contenido del reporte", example = "spam") @RequestParam(required = false) String search,
                        @Parameter(description = "Parámetros de paginación", example = "page=0&size=10&sort=creationDate,desc") @PageableDefault(size = 10) Pageable pageable) {
                log.debug("Obteniendo reportes con filtros - estado: {}, búsqueda: {}, página: {}",
                                status, search, pageable.getPageNumber());

                Page<ReportResponse> reports = reportService.getAllReportsWithFilters(status, search, pageable);

                log.debug("Obtenidos {} reportes para la página {}", reports.getContent().size(),
                                pageable.getPageNumber());

                return ResponseEntity.ok(reports);
        }

}
