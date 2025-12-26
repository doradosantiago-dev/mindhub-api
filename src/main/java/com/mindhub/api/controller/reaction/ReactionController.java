package com.mindhub.api.controller.reaction;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mindhub.api.dto.reaction.ReactionRequest;
import com.mindhub.api.dto.reaction.ReactionResponse;
import com.mindhub.api.model.enums.ReactionType;
import com.mindhub.api.service.reaction.ReactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para operaciones de reacciones en la plataforma MindHub.
 *
 * Proporciona endpoints para crear y actualizar reacciones en publicaciones,
 * eliminar reacciones existentes,
 * obtener las reacciones de una publicación específica, consultar la reacción
 * propia en una publicación,
 * y obtener un resumen de reacciones agrupadas por tipo.
 *
 * Todos los endpoints requieren autenticación de usuario y están diseñados para
 * permitir la interacción social
 * mediante reacciones en las publicaciones. Los usuarios solo pueden gestionar
 * sus
 * propias reacciones,
 * lo que garantiza la integridad del sistema de interacciones.
 */

@Slf4j
@RestController
@RequestMapping("/api/reactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Reacciones", description = "Endpoints para gestión de reacciones en posts")
@SecurityRequirement(name = "bearerAuth")
public class ReactionController {

        private final ReactionService reactionService;

        /**
         * Crea o actualiza una reacción en una publicación específica.
         * 
         * Este endpoint permite a los usuarios autenticados reaccionar a publicaciones
         * con
         * diferentes tipos de reacciones (LIKE, LOVE, etc.). Si el usuario ya tiene
         * una reacción en la publicación, esta se actualiza con el nuevo tipo.
         * 
         * Si el usuario reacciona con el mismo tipo que ya tenía, la reacción se
         * elimina (toggle behavior).
         * 
         * @param postId ID de la publicación a la que reaccionar
         * @param type   Tipo de reacción a aplicar
         * @return ResponseEntity con la reacción creada/actualizada o mensaje de
         *         eliminación
         */
        @PostMapping("/posts/{postId}/react")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Reaccionar a una publicación", description = "Crea o actualiza una reacción en una publicación específica")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Reacción creada/actualizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReactionResponse.class))),
                        @ApiResponse(responseCode = "200", description = "Reacción eliminada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                                        {
                                          "message": "Reacción eliminada exitosamente"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "404", description = "Publicación no encontrada")
        })
        public ResponseEntity<?> reactToPost(
                        @Parameter(description = "ID de la publicación", example = "1") @PathVariable Long postId,
                        @Parameter(description = "Tipo de reacción", example = "LIKE") @RequestParam ReactionType type) {
                log.debug("Usuario reaccionando a la publicación ID: {} con tipo: {}", postId, type);

                ReactionRequest request = new ReactionRequest(postId, null, type);

                ReactionResponse response = reactionService.createOrUpdateReaction(request);

                if (response == null) {
                        log.debug("Reacción eliminada exitosamente de la publicación ID: {}", postId);

                        return ResponseEntity.ok(Map.of("message", "Reacción eliminada exitosamente"));
                }

                log.debug("Reacción creada/actualizada exitosamente en la publicación ID: {}", postId);

                return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        /**
         * Elimina la reacción del usuario en una publicación específica.
         * 
         * Este endpoint permite a los usuarios eliminar su reacción existente en
         * una publicación específica. Solo el usuario que creó la reacción puede
         * eliminarla.
         * 
         * @param postId ID de la publicación del cual eliminar la reacción
         * @return ResponseEntity con mensaje de confirmación
         */
        @DeleteMapping("/posts/{postId}/react")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Eliminar reacción", description = "Elimina la reacción del usuario en una publicación específica")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Reacción eliminada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                                        {
                                          "message": "Reacción eliminada exitosamente"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "404", description = "Post o reacción no encontrada")
        })
        public ResponseEntity<Map<String, String>> removeReaction(
                        @Parameter(description = "ID de la publicación", example = "1") @PathVariable Long postId) {
                log.debug("Eliminando reacción de la publicación ID: {}", postId);

                reactionService.removeReaction(postId);

                log.debug("Reacción eliminada exitosamente de la publicación ID: {}", postId);

                return ResponseEntity.ok(Map.of("message", "Reacción eliminada exitosamente"));
        }

        /**
         * Obtiene todas las reacciones de una publicación específica.
         * 
         * Este endpoint permite obtener todas las reacciones que han recibido
         * una publicación específica con soporte para paginación. Las reacciones se
         * ordenan por fecha de creación (más recientes primero).
         * 
         * @param postId   ID de la publicación cuyas reacciones se consultan
         * @param pageable Información de paginación (número de página, tamaño,
         *                 ordenamiento)
         * @return ResponseEntity con la página de reacciones de la publicación
         */
        @GetMapping("/posts/{postId}")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Obtener reacciones de una publicación", description = "Recupera todas las reacciones de una publicación específica con paginación")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Reacciones obtenidas exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReactionResponse.class))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "404", description = "Publicación no encontrada")
        })
        public ResponseEntity<Page<ReactionResponse>> getPostReactions(
                        @Parameter(description = "ID de la publicación", example = "1") @PathVariable Long postId,
                        @Parameter(description = "Parámetros de paginación", example = "page=0&size=10&sort=creationDate,desc") @PageableDefault(size = 10) Pageable pageable) {
                log.debug("Obteniendo reacciones de la publicación ID: {}, página: {}", postId,
                                pageable.getPageNumber());

                Page<ReactionResponse> reactions = reactionService.getPostReactions(postId, pageable);

                log.debug("Reacciones obtenidas: {} para publicación ID: {}", reactions.getContent().size(), postId);
                return ResponseEntity.ok(reactions);
        }

        /**
         * Obtiene la reacción del usuario actual en una publicación específica.
         * 
         * Este endpoint permite al usuario autenticado consultar si ha reaccionado
         * a una publicación específica y qué tipo de reacción ha aplicado.
         * 
         * @param postId ID de la publicación para consultar la reacción
         * @return ResponseEntity con la reacción del usuario o 204 si no hay reacción
         */
        @GetMapping("/posts/{postId}/my-reaction")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Obtener mi reacción en una publicación", description = "Recupera la reacción del usuario actual en una publicación específica")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Reacción del usuario obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReactionResponse.class))),
                        @ApiResponse(responseCode = "204", description = "El usuario no tiene reacción en esta publicación"),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "404", description = "Publicación no encontrada")
        })
        public ResponseEntity<ReactionResponse> getMyReaction(
                        @Parameter(description = "ID de la publicación", example = "1") @PathVariable Long postId) {
                log.debug("Obteniendo reacción del usuario en publicación ID: {}", postId);
                ReactionResponse response = reactionService.getUserReactionForPost(postId);

                if (response == null) {
                        log.debug("Usuario no tiene reacción en publicación ID: {}", postId);
                        return ResponseEntity.noContent().build();
                }

                log.debug("Reacción del usuario obtenida exitosamente en publicación ID: {}", postId);
                return ResponseEntity.ok(response);
        }

        /**
         * Obtiene un resumen de reacciones por tipo para una publicación específica.
         * 
         * Este endpoint proporciona un resumen estadístico de todas las reacciones
         * de una publicación, agrupadas por tipo de reacción. Es útil para mostrar
         * contadores de reacciones en la interfaz de usuario.
         * 
         * @param postId ID de la publicación para obtener el resumen de reacciones
         * @return ResponseEntity con el mapa de tipos de reacción y sus conteos
         */
        @GetMapping("/posts/{postId}/reactions-summary")
        @PreAuthorize("isAuthenticated()")
        @Operation(summary = "Obtener resumen de reacciones", description = "Recupera un resumen estadístico de reacciones por tipo para una publicación")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Resumen de reacciones obtenido exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(example = """
                                        {
                                          "LIKE": 15
                                        }
                                        """))),
                        @ApiResponse(responseCode = "401", description = "No autorizado - Se requiere autenticación"),
                        @ApiResponse(responseCode = "404", description = "Publicación no encontrada")
        })
        public ResponseEntity<Map<ReactionType, Long>> getReactionsSummary(
                        @Parameter(description = "ID de la publicación", example = "1") @PathVariable Long postId) {
                log.debug("Obteniendo resumen de reacciones para publicación ID: {}", postId);

                Map<ReactionType, Long> summary = reactionService.getReactionsSummaryByPost(postId);

                log.debug("Resumen de reacciones obtenido exitosamente para publicación ID: {}", postId);

                return ResponseEntity.ok(summary);
        }

}
