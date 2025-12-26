package com.mindhub.api.service.post;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mindhub.api.dto.post.PostCreateRequest;
import com.mindhub.api.dto.post.PostResponse;
import com.mindhub.api.exception.PostNotFoundException;
import com.mindhub.api.mapper.post.PostMapper;
import com.mindhub.api.model.enums.ActionType;
import com.mindhub.api.model.enums.NotificationType;
import com.mindhub.api.model.enums.PrivacyType;
import com.mindhub.api.model.post.Post;
import com.mindhub.api.model.user.User;
import com.mindhub.api.repository.follow.FollowRepository;
import com.mindhub.api.repository.post.PostRepository;
import com.mindhub.api.service.admin.AdminActionService;
import com.mindhub.api.service.base.GenericServiceImpl;
import com.mindhub.api.service.notification.NotificationService;
import com.mindhub.api.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio de publicaciones.
 *
 * Gestiona las operaciones CRUD de publicaciones, incluyendo la generación
 * de feeds personales, validaciones de privacidad y optimización
 * de consultas.
 */

@Slf4j
@Service
@Transactional
public class PostServiceImpl extends GenericServiceImpl<Post, Long> implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserService userService;
    private final NotificationService notificationService;
    private final AdminActionService adminActionService;
    private final FollowRepository followRepository;

    public PostServiceImpl(PostRepository postRepository,
            PostMapper postMapper,
            UserService userService,
            NotificationService notificationService,
            AdminActionService adminActionService,
            FollowRepository followRepository) {
        super(postRepository);
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.userService = userService;
        this.notificationService = notificationService;
        this.adminActionService = adminActionService;
        this.followRepository = followRepository;
    }

    /**
     * Enriquece un post con contadores de comentarios y reacciones.
     * 
     * @param post Post a enriquecer
     * @return PostResponse con contadores
     */
    private PostResponse enrichPostWithCounts(Post post) {
        log.debug("Enriqueciendo post {} con contadores", post.getId());

        PostResponse response = postMapper.toResponse(post);

        long commentCount = postRepository.countCommentsByPostId(post.getId());
        long reactionCount = postRepository.countReactionsByPostId(post.getId());

        log.info("Post {} enriquecido con {} comentarios y {} reacciones", post.getId(), commentCount, reactionCount);

        return new PostResponse(
                response.id(),
                response.content(),
                response.imageUrl(),
                response.privacyType(),
                response.creationDate(),
                response.updateDate(),
                response.author(),
                (int) commentCount,
                (int) reactionCount);
    }

    /**
     * Enriquece múltiples publicaciones con contadores de comentarios y reacciones.
     * 
     * @param posts Lista de publicaciones a enriquecer
     * @return Lista de PostResponse con contadores
     */
    private List<PostResponse> enrichPostsWithCounts(List<Post> posts) {
        if (posts.isEmpty()) {
            log.debug("No hay posts para enriquecer");

            return List.of();
        }

        List<Long> postIds = posts.stream()
                .map(Post::getId)
                .toList();

        log.debug("Enriqueciendo {} publicaciones con contadores", postIds.size());

        Map<Long, Long> commentCounts = postRepository.countCommentsByPostIds(postIds)
                .stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Long) row[1]));

        Map<Long, Long> reactionCounts = postRepository.countReactionsByPostIds(postIds)
                .stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Long) row[1]));

        return posts.stream()
                .map(post -> {
                    PostResponse response = postMapper.toResponse(post);
                    long commentCount = commentCounts.getOrDefault(post.getId(), 0L);
                    long reactionCount = reactionCounts.getOrDefault(post.getId(), 0L);

                    return new PostResponse(
                            response.id(),
                            response.content(),
                            response.imageUrl(),
                            response.privacyType(),
                            response.creationDate(),
                            response.updateDate(),
                            response.author(),
                            (int) commentCount,
                            (int) reactionCount);
                })
                .toList();
    }

    /**
     * Verifica si un usuario es administrador.
     * 
     * @param user Usuario a verificar
     * @return true si es administrador, false en caso contrario
     */
    private boolean isAdmin(User user) {
        log.debug("Verificando si el usuario {} es administrador", user != null ? user.getId() : null);

        return user != null && user.getRole() != null && user.getRole().isAdmin();
    }

    /**
     * Crea una nueva publicación.
     * 
     * @param request Datos de la publicación a crear
     * @return Publicación creada con contadores
     */
    /**
     * Crea una nueva publicación.
     * 
     * @param request Datos de la publicación a crear
     * @return Publicación creada con contadores
     */
    @Override
    public PostResponse createPost(PostCreateRequest request) {
        log.debug("Creando una nueva publicación");

        User currentUser = userService.getCurrentUser();

        log.debug("Usuario {} crea una nueva publicación", currentUser.getId());
        Post post = postMapper.toEntity(request);

        post.setAuthor(currentUser);

        Post savedPost = save(post);

        userService.updateLastActivity(currentUser.getId());

        log.info("Usuario {} creó la publicación {}", currentUser.getId(), savedPost.getId());

        return enrichPostWithCounts(savedPost);
    }

    /**
     * Obtiene los posts de un usuario específico paginados.
     * 
     * @param userId   ID del usuario
     * @param pageable Configuración de paginación
     * @return Página de posts del usuario
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> getUserPosts(Long userId, Pageable pageable) {
        log.debug("Obteniendo publicaciones del usuario {} con paginación {}", userId, pageable);

        User usuario = userService.findByIdOrThrow(userId);

        User currentUser = userService.getCurrentUser();

        Page<Post> postsPage;
        if (currentUser.getId().equals(userId)) {
            postsPage = postRepository.findByAuthorOrderByCreationDateDesc(usuario, pageable);
        } else {
            postsPage = postRepository.findPublicPostsByAuthorOrderByCreationDateDesc(usuario, pageable);
        }

        List<PostResponse> postsWithCounts = enrichPostsWithCounts(postsPage.getContent());

        log.info("Se obtuvieron {} publicaciones del usuario {}", postsPage.getTotalElements(), userId);

        return new org.springframework.data.domain.PageImpl<>(
                postsWithCounts,
                pageable,
                postsPage.getTotalElements());
    }

    /**
     * Obtiene todos los posts públicos paginados.
     * 
     * @param pageable Configuración de paginación
     * @return Página de posts públicos
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> getPublicPosts(Pageable pageable) {
        log.debug("Obteniendo publicaciones públicas con paginación {}", pageable);

        Page<Post> postsPage = postRepository.findAllPublicPosts(PrivacyType.PUBLIC, PrivacyType.PUBLIC, pageable);

        List<PostResponse> postsWithCounts = enrichPostsWithCounts(postsPage.getContent());

        log.info("Se obtuvieron {} publicaciones públicas", postsPage.getTotalElements());

        return new org.springframework.data.domain.PageImpl<>(
                postsWithCounts,
                pageable,
                postsPage.getTotalElements());
    }

    /**
     * Obtiene los posts del usuario actual paginados.
     * 
     * @param pageable Configuración de paginación
     * @return Página de posts del usuario actual
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> getMyPosts(Pageable pageable) {
        log.debug("Obteniendo publicaciones del usuario actual con paginación {}", pageable);

        User currentUser = userService.getCurrentUser();

        log.debug("Usuario actual: {}", currentUser.getId());

        Page<Post> postsPage = postRepository.findByAuthorOrderByCreationDateDesc(currentUser, pageable);

        List<PostResponse> postsWithCounts = enrichPostsWithCounts(postsPage.getContent());

        log.info("Usuario {} obtuvo {} publicaciones propias", currentUser.getId(), postsPage.getTotalElements());

        return new org.springframework.data.domain.PageImpl<>(
                postsWithCounts,
                pageable,
                postsPage.getTotalElements());
    }

    /**
     * Obtiene el feed personal del usuario actual.
     * 
     * @param currentUser Usuario actual
     * @param pageable    Configuración de paginación
     * @return Página de posts del feed personal
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> getPersonalFeed(User currentUser, Pageable pageable) {
        log.info("Obteniendo feed personal para el usuario {} con paginación: page={}, size={}, sort={}",
                currentUser.getId(), pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        // Los administradores no usan el feed, tienen su panel de administración
        if (currentUser.getRole().getName().equals("ADMIN")) {
            log.info("Usuario {} es administrador, retornando feed vacío", currentUser.getId());
            return Page.empty(pageable);
        }

        List<Long> followingIds = followRepository.findFollowedIdsByUser(currentUser);

        followingIds.add(currentUser.getId());

        // Obtener posts para el feed: posts públicos de los seguidos + TODOS los posts
        // del propio usuario
        // Ordenados por creationDate DESC, id DESC
        Page<Post> postsPage = postRepository.findPostsForFeed(
                followingIds, PrivacyType.PUBLIC, currentUser.getId(), pageable);

        // Enriquecer con contadores de comentarios y reacciones
        List<PostResponse> postsWithCounts = enrichPostsWithCounts(postsPage.getContent());

        log.info("Feed personal del usuario {} contiene {} publicaciones", currentUser.getId(),
                postsPage.getTotalElements());

        return new org.springframework.data.domain.PageImpl<>(
                postsWithCounts,
                pageable,
                postsPage.getTotalElements());
    }

    /**
     * Obtiene las publicaciones reportadas (solo para administradores).
     * 
     * @param pageable Configuración de paginación
     * @return Página de publicaciones reportadas
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> getReportedPosts(Pageable pageable) {
        log.debug("Obteniendo publicaciones reportadas con paginación {}", pageable);

        User currentUser = userService.getCurrentUser();

        if (!isAdmin(currentUser)) {
            log.warn("Usuario {} intentó acceder a publicaciones reportadas sin permisos", currentUser.getId());

            throw new SecurityException("Solo los administradores pueden ver las publicaciones reportadas");
        }

        Page<Post> postsPage = postRepository.findPostsWithPendingReports(pageable);

        List<PostResponse> postsWithCounts = postsPage.getContent().stream()
                .map(this::enrichPostWithCounts)
                .toList();

        log.info("Usuario administrador {} obtuvo {} publicaciones reportadas", currentUser.getId(),
                postsPage.getTotalElements());

        return new org.springframework.data.domain.PageImpl<>(
                postsWithCounts,
                pageable,
                postsPage.getTotalElements());
    }

    /**
     * Actualiza una publicación existente.
     * 
     * @param id      ID de la publicación a actualizar
     * @param request Nuevos datos de la publicación
     * @return Publicación actualizada
     */
    @Override
    public PostResponse updatePost(Long id, PostCreateRequest request) {
        log.debug("Solicitud de actualización de la publicación {}", id);

        Post post = findByIdOrThrow(id);

        User currentUser = userService.getCurrentUser();

        log.debug("Usuario {} intenta actualizar la publicación {}", currentUser.getId(), id);

        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            log.warn("Usuario {} no es autor de la publicación {} y no puede actualizarla", currentUser.getId(), id);

            throw new IllegalStateException("Solo el autor puede actualizar la publicación.");
        }

        post.setContent(request.content());
        post.setImageUrl(request.imageUrl());
        post.setPrivacyType(request.privacyType());

        Post savedPost = save(post);

        log.info("Usuario {} actualizó la publicación {}", currentUser.getId(), id);

        return enrichPostWithCounts(savedPost);
    }

    /**
     * Obtiene un post por su ID.
     * 
     * @param id ID del post
     * @return Post encontrado con contadores
     */
    @Override
    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {
        log.debug("Obteniendo publicación por ID {}", id);

        Post post = findByIdOrThrow(id);

        log.info("Publicación {} obtenida correctamente", id);
        return enrichPostWithCounts(post);
    }

    /**
     * Elimina una publicación por su ID.
     * 
     * @param id ID de la publicación a eliminar
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        log.debug("Solicitud de eliminación de la publicación {}", id);

        Post post = findByIdOrThrow(id);

        User currentUser = userService.getCurrentUser();

        log.debug("Usuario {} intenta eliminar la publicación {}", currentUser.getId(), id);

        if (!post.getAuthor().getId().equals(currentUser.getId()) &&
                !isAdmin(currentUser)) {
            log.warn("Usuario {} no tiene permisos para eliminar la publicación {}", currentUser.getId(), id);
            throw new SecurityException("No tienes permiso para eliminar esta publicación");
        }

        if (isAdmin(currentUser) &&
                !post.getAuthor().getId().equals(currentUser.getId())) {

            log.info("Usuario administrador {} elimina la publicación {} del autor {}", currentUser.getId(), id,
                    post.getAuthor().getId());

            adminActionService.logAction(currentUser, ActionType.DELETE_POST,
                    "Publicación eliminada", "Publicación con contenido: " + post.getContent().substring(0,
                            Math.min(post.getContent().length(), 100)),
                    post.getId(), "posts", post.getAuthor());

            notificationService.createNotification(post.getAuthor(),
                    "Publicación eliminada por administrador",
                    "Tu publicación ha sido eliminada por un administrador",
                    NotificationType.ADMIN_ACTION);
        }

        postRepository.delete(post);

        log.info("Publicación {} eliminada correctamente por el usuario {}", id, currentUser.getId());
    }

    /**
     * Cambia la privacidad de una publicación.
     * 
     * @param id          ID de la publicación
     * @param privacyType Nuevo tipo de privacidad
     */
    @Override
    public void changePostPrivacy(Long id, PrivacyType privacyType) {
        log.debug("Solicitud de cambio de privacidad para la publicación {} a {}", id, privacyType);

        Post post = findByIdOrThrow(id);

        User currentUser = userService.getCurrentUser();

        log.debug("Usuario {} intenta cambiar privacidad de la publicación {}", currentUser.getId(), id);

        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            log.warn("Usuario {} no es autor de la publicación {} y no puede cambiar la privacidad",
                    currentUser.getId(), id);

            throw new IllegalStateException("Solo el autor puede cambiar la privacidad de la publicación");
        }

        post.setPrivacyType(privacyType);

        save(post);

        log.info("Usuario {} cambió la privacidad de la publicación {} a {}", currentUser.getId(), id, privacyType);
    }

    /**
     * Cuenta el número total de publicaciones en el sistema.
     * 
     * @return Número total de publicaciones
     */
    @Override
    @Transactional(readOnly = true)
    public long countTotalPosts() {
        log.debug("Contando el número total de publicaciones en el sistema");

        return postRepository.count();
    }

    /**
     * Obtiene el ID de una entidad.
     * 
     * @param entity Entidad
     * @return ID de la entidad
     */
    @Override
    protected Long getEntityId(Post entity) {
        log.debug("Obteniendo ID de la entidad Publicación");

        return entity.getId();
    }

    /**
     * Crea una excepción de notificación cuando no se encuentra una entidad.
     * 
     * @param message Mensaje de la excepción
     * @return Excepción de notificación
     */
    @Override
    protected RuntimeException createNotFoundException(String message) {
        log.warn("Creando excepción PostNotFoundException con mensaje: {}", message);

        return new PostNotFoundException(message);
    }

    /**
     * Obtiene el nombre de la entidad.
     * 
     * @return Nombre de la entidad
     */
    @Override
    public String getEntityName() {
        log.debug("Obteniendo nombre de la entidad gestionada:  Publicación");

        return "Publicación";
    }
}
