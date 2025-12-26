package com.mindhub.api.service.reaction;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mindhub.api.dto.reaction.ReactionRequest;
import com.mindhub.api.dto.reaction.ReactionResponse;
import com.mindhub.api.exception.PostNotFoundException;
import com.mindhub.api.exception.ReactionNotFoundException;
import com.mindhub.api.mapper.reaction.ReactionMapper;
import com.mindhub.api.model.enums.NotificationType;
import com.mindhub.api.model.enums.PrivacyType;
import com.mindhub.api.model.enums.ReactionType;
import com.mindhub.api.model.post.Post;
import com.mindhub.api.model.reaction.Reaction;
import com.mindhub.api.model.role.Role;
import com.mindhub.api.model.user.User;
import com.mindhub.api.repository.post.PostRepository;
import com.mindhub.api.repository.reaction.ReactionRepository;
import com.mindhub.api.service.base.GenericServiceImpl;
import com.mindhub.api.service.notification.NotificationService;
import com.mindhub.api.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio de reacciones.
 *
 * Gestiona las operaciones CRUD de reacciones, incluyendo validaciones
 * de privacidad y permisos para reaccionar a publicaciones.
 */

@Slf4j
@Service
@Transactional
public class ReactionServiceImpl extends GenericServiceImpl<Reaction, Long> implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final PostRepository postRepository;
    private final ReactionMapper reactionMapper;
    private final UserService userService;
    private final NotificationService notificationService;
    private final com.mindhub.api.service.follow.FollowService followService;

    public ReactionServiceImpl(ReactionRepository reactionRepository,
            PostRepository postRepository,
            ReactionMapper reactionMapper,
            UserService userService,
            NotificationService notificationService,
            com.mindhub.api.service.follow.FollowService followService) {
        super(reactionRepository);
        this.reactionRepository = reactionRepository;
        this.postRepository = postRepository;
        this.reactionMapper = reactionMapper;
        this.userService = userService;
        this.notificationService = notificationService;
        this.followService = followService;
    }

    /**
     * Crea o actualiza una reacción a un publicaciones.
     * 
     * @param request Datos de la reacción
     * @return Reacción creada o actualizada
     */
    @Override
    public ReactionResponse createOrUpdateReaction(ReactionRequest request) {

        User currentUser = userService.getCurrentUser();

        log.debug("Usuario actual: {} intentando reaccionar a la publicación {}", currentUser.getId(),
                request.postId());

        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new PostNotFoundException("Publicación no encontrada"));

        if (!canReactToPost(post, currentUser)) {
            log.warn("Usuario {} no tiene permiso para reaccionar a la publicación {}", currentUser.getId(),
                    post.getId());

            throw new IllegalStateException("No tienes permiso para reaccionar a esta publicación");
        }

        Reaction existingReaction = reactionRepository.findByUserAndPost(currentUser, post)
                .orElse(null);

        Reaction reaction;
        boolean isNewReaction = false;

        if (existingReaction != null) {
            if (existingReaction.getType().equals(request.reactionType())) {
                log.info("Usuario {} eliminó su reacción '{}' en la publicación {}", currentUser.getId(),
                        existingReaction.getType(), post.getId());

                delete(existingReaction);

                return null;
            } else {
                log.info("Usuario {} cambió su reacción de '{}' a '{}' en la publicación {}",
                        currentUser.getId(), existingReaction.getType(), request.reactionType(), post.getId());

                existingReaction.setType(request.reactionType());

                reaction = save(existingReaction);
            }
        } else {
            reaction = reactionMapper.toEntity(request);

            reaction.setUser(currentUser);
            reaction.setPost(post);

            reaction = save(reaction);

            isNewReaction = true;

            log.info("Usuario {} creó una nueva reacción '{}' en la publicación {}", currentUser.getId(),
                    reaction.getType(),
                    post.getId());
        }

        userService.updateLastActivity(currentUser.getId());

        if (isNewReaction) {
            notificationService.createNotificationWithReference(
                    post.getAuthor(),
                    "Nueva reacción",
                    currentUser.getFirstName() + " " + currentUser.getLastName() +
                            " ha reaccionado a tu publicación",
                    NotificationType.REACTION,
                    post.getId(),
                    "posts");

            log.debug("Notificación enviada al autor {} por nueva reacción en la publicación {}",
                    post.getAuthor().getId(),
                    post.getId());
        }

        return reactionMapper.toResponse(reaction);
    }

    /**
     * Elimina una reacción de un post.
     * 
     * @param postId ID del post
     */
    @Override
    public void removeReaction(Long postId) {
        User currentUser = userService.getCurrentUser();

        log.debug("Usuario {} intenta eliminar su reacción en la publicación {}", currentUser.getId(), postId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Publicación no encontrada"));

        Reaction reaction = reactionRepository.findByUserAndPost(currentUser, post)
                .orElseThrow(() -> new ReactionNotFoundException("Reacción no encontrada"));

        delete(reaction);

        log.info("Usuario {} eliminó su reacción '{}' en la publicación {}", currentUser.getId(), reaction.getType(),
                postId);
    }

    /**
     * Obtiene las reacciones de un post paginadas.
     * 
     * @param postId   ID del post
     * @param pageable Configuración de paginación
     * @return Página de reacciones
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReactionResponse> getPostReactions(Long postId, Pageable pageable) {
        log.debug("Usuario solicita ver reacciones de la publicación {} con paginación {}", postId, pageable);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Publicación no encontrada"));

        User currentUser = userService.getCurrentUser();

        log.debug("Usuario actual {} intenta acceder a las reacciones de la publicación {}", currentUser.getId(),
                postId);
        if (!canViewPost(post, currentUser)) {
            log.warn("Acceso denegado: el usuario {} no tiene permiso para ver las reacciones de la publicación {}",
                    currentUser.getId(), postId);

            throw new IllegalStateException("No tienes permiso para ver las reacciones de esta publicación");
        }

        return reactionRepository.findByPostOrderByCreationDateDesc(post, pageable)
                .map(reactionMapper::toResponse);

    }

    /**
     * Cuenta el número total de reacciones de una publicación.
     * 
     * @param post Publicación a contar reacciones
     * @return Número total de reacciones
     */
    @Override
    @Transactional(readOnly = true)
    public long countReactionsByPost(Post post) {
        log.debug("Contando todas las reacciones de la publicación {}", post.getId());

        return reactionRepository.countByPost(post);
    }

    /**
     * Cuenta el número de reacciones de un tipo específico en una publicación.
     * 
     * @param post Publicación a contar reacciones
     * @param type Tipo de reacción
     * @return Número de reacciones del tipo especificado
     */
    @Override
    @Transactional(readOnly = true)
    public long countReactionsByPostAndType(Post post, ReactionType type) {
        log.debug("Contando reacciones de tipo {} en la publicación {}", type, post.getId());

        return reactionRepository.countByPostAndType(post, type);
    }

    /**
     * Verifica si un usuario ha reaccionado a una publicación.
     * 
     * @param userId ID del usuario
     * @param postId ID de la publicación
     * @return true si el usuario ha reaccionado, false en caso contrario
     */
    @Override
    @Transactional(readOnly = true)
    public boolean hasUserReactedToPost(Long userId, Long postId) {
        log.debug("Verificando si el usuario {} ha reaccionado a la publicación {}", userId, postId);

        User user = userService.findByIdOrThrow(userId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Publicación no encontrada"));

        log.debug("Consultando existencia de reacción del usuario {} en la publicación {}", userId, postId);

        return reactionRepository.existsByUserAndPost(user, post);
    }

    /**
     * Obtiene la reacción del usuario actual para una publicación.
     * 
     * @param postId ID de la publicación
     * @return Reacción del usuario o null si no existe
     */
    @Override
    @Transactional(readOnly = true)
    public ReactionResponse getUserReactionForPost(Long postId) {
        log.debug("Obteniendo reacción del usuario actual para la publicación {}", postId);

        User currentUser = userService.getCurrentUser();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Publicación no encontrada"));

        Reaction reaction = reactionRepository.findByUserAndPost(currentUser, post)
                .orElse(null);

        if (reaction == null) {
            log.info("El usuario {} no tiene reacción en la publicación {}", currentUser.getId(), postId);

            return null;
        }

        log.info("El usuario {} tiene una reacción '{}' en la publicación {}", currentUser.getId(), reaction.getType(),
                postId);

        return reactionMapper.toResponse(reaction);
    }

    /**
     * Obtiene el resumen de reacciones por tipo para una publicación.
     * 
     * @param postId ID de la publicación
     * @return Mapa con el conteo de reacciones por tipo
     */
    @Override
    @Transactional(readOnly = true)
    public Map<ReactionType, Long> getReactionsSummaryByPost(Long postId) {
        log.debug("Obteniendo resumen de reacciones por tipo para la publicación {}", postId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Publicación no encontrada"));

        User currentUser = userService.getCurrentUser();

        log.debug("Usuario {} solicita resumen de reacciones de la publicación {}", currentUser.getId(), postId);
        if (!canViewPost(post, currentUser)) {
            log.warn(
                    "Acceso denegado: el usuario {} no tiene permiso para ver el resumen de reacciones de la publicación {}",
                    currentUser.getId(), postId);

            throw new IllegalStateException("No tienes permiso para ver las reacciones de esta publicación");
        }

        return reactionRepository.findByPostOrderByCreationDateDesc(post, Pageable.unpaged())
                .getContent()
                .stream()
                .collect(Collectors.groupingBy(
                        Reaction::getType,
                        Collectors.counting()));
    }

    /**
     * Verifica si un usuario puede reaccionar a una publicación.
     * 
     * @param post Publicación a verificar
     * @param user Usuario que intenta reaccionar
     * @return true si puede reaccionar, false en caso contrario
     */
    private boolean canReactToPost(Post post, User user) {
        log.debug("Verificando si el usuario {} puede reaccionar a la publicación {}", user.getId(), post.getId());

        // El autor siempre puede reaccionar a sus propios posts
        if (post.getAuthor().getId().equals(user.getId())) {
            log.info("El usuario {} es el autor de la publicación {} y puede reaccionar", user.getId(), post.getId());
            return true;
        }

        // Los admins pueden reaccionar a cualquier publicación
        if (isAdmin(user)) {
            log.info("El usuario {} es administrador y puede reaccionar", user.getId());
            return true;
        }

        // Si el post es público, cualquiera puede reaccionar (independiente de si el
        // autor es público o privado)
        if (post.getPrivacyType() == PrivacyType.PUBLIC) {
            log.debug("Post público - permitir reacción");
            return true;
        }

        // Si el post es privado, solo puede reaccionar si sigue al autor
        boolean isFollowing = followService.follows(user, post.getAuthor());
        log.debug("Post privado: Usuario {} sigue a autor {}: {}", user.getId(), post.getAuthor().getId(), isFollowing);
        return isFollowing;
    }

    /**
     * Verifica si un usuario puede ver una publicación.
     * 
     * @param post Publicación a verificar
     * @param user Usuario que intenta ver la publicación
     * @return true si puede ver la publicación, false en caso contrario
     */
    private boolean canViewPost(Post post, User user) {
        log.debug("Verificando si el usuario {} puede ver la publicación {}", user.getId(), post.getId());

        if (post.getAuthor().getId().equals(user.getId())) {
            log.info("El usuario {} es el autor de la publicación {} y puede verla", user.getId(), post.getId());

            return true;
        }

        if (isAdmin(user)) {
            log.info("El usuario {} es administrador y puede ver la publicación {}", user.getId(), post.getId());

            return true;
        }

        return post.getPrivacyType() == PrivacyType.PUBLIC &&
                post.getAuthor().getPrivacyType() == PrivacyType.PUBLIC;
    }

    /**
     * Verifica si un usuario es administrador.
     * 
     * @param user Usuario a verificar
     * @return true si es administrador, false en caso contrario
     */
    private boolean isAdmin(User user) {
        log.debug("Verificando si el usuario {} es administrador", user != null ? user.getId() : null);

        return user != null && user.getRole() != null && Role.ADMIN.equals(user.getRole().getName());
    }

    /**
     * Obtiene el identificador único de la entidad Reaction.
     *
     * @param entity entidad Reaction
     * @return identificador de la entidad
     */
    @Override
    protected Long getEntityId(Reaction entity) {
        log.debug("Obteniendo ID de la entidad Reaction");

        return entity.getId();
    }

    /**
     * Crea una excepción específica cuando no se encuentra una reacción.
     *
     * @param message mensaje descriptivo del error
     * @return excepción de tipo ReactionNotFoundException
     */
    @Override
    protected RuntimeException createNotFoundException(String message) {
        log.warn("Creando excepción ReactionNotFoundException con mensaje: {}", message);

        return new ReactionNotFoundException(message);
    }

    /**
     * Devuelve el nombre de la entidad gestionada por este servicio.
     *
     * @return nombre de la entidad ("Reaction")
     */
    @Override
    public String getEntityName() {
        log.debug("Obteniendo nombre de la entidad gestionada: Reaction");

        return "Reaction";
    }
}
