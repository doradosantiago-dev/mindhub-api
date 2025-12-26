package com.mindhub.api.service.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mindhub.api.dto.post.PostCreateRequest;
import com.mindhub.api.dto.post.PostResponse;
import com.mindhub.api.model.enums.PrivacyType;
import com.mindhub.api.model.post.Post;
import com.mindhub.api.model.user.User;
import com.mindhub.api.service.base.GenericService;

/**
 * Servicio para la gestión de publicaciones.
 *
 * Define operaciones para crear, actualizar, eliminar y consultar posts,
 * incluyendo feeds personales y públicos con validaciones de privacidad.
 */

public interface PostService extends GenericService<Post, Long> {

    /**
     * Crea un nuevo post.
     * 
     * @param request Datos del post a crear
     * @return Post creado con contadores
     */
    PostResponse createPost(PostCreateRequest request);

    /**
     * Obtiene los posts de un usuario específico paginados.
     * 
     * @param userId   ID del usuario
     * @param pageable Configuración de paginación
     * @return Página de posts del usuario
     */
    Page<PostResponse> getUserPosts(Long userId, Pageable pageable);

    /**
     * Obtiene todos los posts públicos paginados.
     * 
     * @param pageable Configuración de paginación
     * @return Página de posts públicos
     */
    Page<PostResponse> getPublicPosts(Pageable pageable);

    /**
     * Obtiene los posts del usuario actual paginados.
     * 
     * @param pageable Configuración de paginación
     * @return Página de posts del usuario actual
     */
    Page<PostResponse> getMyPosts(Pageable pageable);

    /**
     * Obtiene el feed personal del usuario actual.
     * 
     * @param currentUser Usuario actual
     * @param pageable    Configuración de paginación
     * @return Página de posts del feed personal
     */
    Page<PostResponse> getPersonalFeed(User currentUser, Pageable pageable);

    /**
     * Obtiene los posts reportados (solo para administradores).
     * 
     * @param pageable Configuración de paginación
     * @return Página de posts reportados
     */
    Page<PostResponse> getReportedPosts(Pageable pageable);

    /**
     * Actualiza un post existente.
     * 
     * @param id      ID del post a actualizar
     * @param request Nuevos datos del post
     * @return Post actualizado
     */
    PostResponse updatePost(Long id, PostCreateRequest request);

    /**
     * Obtiene un post por su ID.
     * 
     * @param id ID del post
     * @return Post encontrado con contadores
     */
    PostResponse getPostById(Long id);

    /**
     * Cambia la privacidad de un post.
     * 
     * @param id          ID del post
     * @param privacyType Nuevo tipo de privacidad
     */
    void changePostPrivacy(Long id, PrivacyType privacyType);

    /**
     * Cuenta el número total de posts en el sistema.
     * 
     * @return Número total de posts
     */
    long countTotalPosts();
}
