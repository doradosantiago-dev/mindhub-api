package com.mindhub.api.service.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mindhub.api.dto.comment.CommentRequest;
import com.mindhub.api.dto.comment.CommentResponse;
import com.mindhub.api.exception.CommentNotAllowedException;
import com.mindhub.api.exception.CommentNotFoundException;
import com.mindhub.api.exception.CommentViewNotAllowedException;
import com.mindhub.api.exception.PostNotFoundException;
import com.mindhub.api.mapper.comment.CommentMapper;
import com.mindhub.api.model.comment.Comment;
import com.mindhub.api.model.enums.NotificationType;
import com.mindhub.api.model.enums.PrivacyType;
import com.mindhub.api.model.post.Post;
import com.mindhub.api.model.role.Role;
import com.mindhub.api.model.user.User;
import com.mindhub.api.repository.comment.CommentRepository;
import com.mindhub.api.repository.post.PostRepository;
import com.mindhub.api.service.base.GenericServiceImpl;
import com.mindhub.api.service.notification.NotificationService;
import com.mindhub.api.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio de comentarios.
 *
 * Gestiona las operaciones CRUD de comentarios, incluyendo validaciones
 * de privacidad y permisos de usuario.
 */

@Slf4j
@Service
@Transactional
public class CommentServiceImpl extends GenericServiceImpl<Comment, Long> implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;
    private final UserService userService;
    private final NotificationService notificationService;
    private final com.mindhub.api.service.follow.FollowService followService;

    public CommentServiceImpl(CommentRepository commentRepository,
            PostRepository postRepository,
            CommentMapper commentMapper,
            UserService userService,
            NotificationService notificationService,
            com.mindhub.api.service.follow.FollowService followService) {
        super(commentRepository);
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.commentMapper = commentMapper;
        this.userService = userService;
        this.notificationService = notificationService;
        this.followService = followService;
    }

    /**
     * Crea un nuevo comentario en un post.
     * 
     * @param request Datos del comentario a crear
     * @return Comentario creado
     */
    @Override
    public CommentResponse createComment(CommentRequest request) {
        User currentUser = userService.getCurrentUser();

        log.debug("Usuario {} intenta comentar en la publicación {}", currentUser.getId(), request.postId());

        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new PostNotFoundException("Publicación no encontrada"));

        if (!canCommentOnPost(post, currentUser)) {
            throw new CommentNotAllowedException(
                    "No puedes comentar en esta publicación. La publicación o el autor tienen privacidad privada.");
        }

        Comment comment = commentMapper.toEntity(request);
        comment.setAuthor(currentUser);
        comment.setPost(post);

        Comment savedComment = save(comment);
        userService.updateLastActivity(currentUser.getId());

        notificationService.createNotificationWithReference(
                post.getAuthor(),
                "Nuevo comentario",
                currentUser.getFirstName() + " " + currentUser.getLastName() +
                        " ha comentado en tu publicación",
                NotificationType.COMMENT,
                post.getId(),
                "posts");

        log.info("Comentario {} creado en publicación {} por usuario {}", savedComment.getId(), post.getId(),
                currentUser.getId());

        return commentMapper.toResponse(savedComment);
    }

    /**
     * Obtiene un comentario por su ID.
     * 
     * @param id ID del comentario
     * @return Comentario encontrado
     */
    @Override
    @Transactional(readOnly = true)
    public CommentResponse getCommentById(Long id) {
        log.debug("Consultando comentario con ID {}", id);

        Comment comment = findByIdOrThrow(id);

        return commentMapper.toResponse(comment);
    }

    /**
     * Obtiene los comentarios de un post paginados.
     * 
     * @param postId   ID del post
     * @param pageable Configuración de paginación
     * @return Página de comentarios del post
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponse> getPostComments(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Publicación no encontrada"));

        User currentUser = userService.getCurrentUser();

        if (!canViewPost(post, currentUser)) {
            log.warn("Usuario {} no tiene permisos para ver comentarios de la publicación {}", currentUser.getId(),
                    postId);

            throw new CommentViewNotAllowedException(
                    "No tienes permisos para ver los comentarios de esta publicación. La publicación o el autor tienen privacidad privada.");
        }

        return commentRepository.findByPostOrderByCreationDateAsc(post, pageable)
                .map(commentMapper::toResponse);
    }

    /**
     * Actualiza un comentario existente.
     * 
     * @param id      ID del comentario a actualizar
     * @param request Nuevos datos del comentario
     * @return Comentario actualizado
     */
    @Override
    public CommentResponse updateComment(Long id, CommentRequest request) {
        Comment comment = findByIdOrThrow(id);

        User currentUser = userService.getCurrentUser();

        log.debug("Usuario {} intenta actualizar comentario {}", currentUser.getId(), id);

        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            log.warn("Usuario {} intentó actualizar comentario {} sin ser autor", currentUser.getId(), id);

            throw new CommentNotAllowedException("Solo el autor puede editar el comentario");
        }

        comment.setContent(request.content());
        Comment savedComment = save(comment);

        log.info("Comentario {} actualizado por usuario {}", savedComment.getId(), currentUser.getId());

        return commentMapper.toResponse(savedComment);
    }

    /**
     * Elimina un comentario por su ID.
     * 
     * @param id ID del comentario a eliminar
     */
    @Override
    public void deleteById(Long id) {
        Comment comment = findByIdOrThrow(id);

        User currentUser = userService.getCurrentUser();

        log.debug("Usuario {} intenta eliminar comentario {}", currentUser.getId(), id);

        if (!comment.getAuthor().getId().equals(currentUser.getId()) &&
                !isAdmin(currentUser)) {
            log.warn("Usuario {} no tiene permisos para eliminar comentario {}", currentUser.getId(), id);

            throw new CommentNotAllowedException("No tienes permisos para eliminar este comentario");
        }

        log.info("Comentario {} eliminado por usuario {}", id, currentUser.getId());

        commentRepository.delete(comment);
    }

    /**
     * Verifica si un usuario puede comentar en un post.
     * 
     * @param post Post en el cual se quiere comentar
     * @param user Usuario que quiere comentar
     * @return true si puede comentar, false en caso contrario
     */
    private boolean canCommentOnPost(Post post, User user) {
        // El autor siempre puede comentar en su propia publicación
        if (post.getAuthor().getId().equals(user.getId())) {
            return true;
        }

        // Los admins pueden comentar en cualquier publicación
        if (isAdmin(user)) {
            return true;
        }

        // Si la publicación es pública, cualquier usuario autenticado puede comentar
        return post.getPrivacyType() == PrivacyType.PUBLIC;
    }

    /**
     * Verifica si un usuario puede ver un post.
     * 
     * @param post Post a verificar
     * @param user Usuario que quiere ver el post
     * @return true si puede ver el post, false en caso contrario
     */
    private boolean canViewPost(Post post, User user) {
        // El autor siempre puede ver sus propios posts
        if (post.getAuthor().getId().equals(user.getId())) {
            return true;
        }

        // Los admins pueden ver todos los posts
        if (isAdmin(user)) {
            return true;
        }

        // Si el post es público, cualquiera puede verlo (independiente de si el autor
        // es público o privado)
        if (post.getPrivacyType() == PrivacyType.PUBLIC) {
            return true;
        }

        // Si el post es privado, solo puede verlo si sigue al autor
        boolean isFollowing = followService.follows(user, post.getAuthor());
        log.debug("Post privado: Usuario {} sigue a autor {}: {}", user.getId(), post.getAuthor().getId(), isFollowing);
        return isFollowing;
    }

    @Override
    protected Long getEntityId(Comment entity) {
        return entity.getId();
    }

    @Override
    protected RuntimeException createNotFoundException(String message) {
        return new CommentNotFoundException(message);
    }

    @Override
    public String getEntityName() {
        return "Comment";
    }

    /**
     * Verifica si un usuario es administrador.
     * 
     * @param user Usuario a verificar
     * @return true si es administrador, false en caso contrario
     */
    private boolean isAdmin(User user) {
        return user != null && user.getRole() != null && Role.ADMIN.equals(user.getRole().getName());
    }
}
