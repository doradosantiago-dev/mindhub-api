package com.mindhub.api.repository.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mindhub.api.model.enums.PrivacyType;
import com.mindhub.api.model.post.Post;
import com.mindhub.api.model.user.User;

import java.util.List;

/**
 * Repositorio para la gestión de publicaciones del sistema.
 *
 * Proporciona métodos para consultar, contar y administrar los posts
 * de los usuarios, incluyendo filtrado por privacidad, conteo de
 * interacciones y consultas optimizadas para múltiples publicaciones.
 */

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

       /**
        * Busca posts de un autor con paginación.
        * 
        * Retorna todos los posts de un autor específico ordenados
        * por fecha de creación descendente (más recientes primero).
        * 
        * 
        * @param author   Autor de los posts
        * @param pageable Configuración de paginación
        * @return Página de posts ordenados por fecha
        * 
        * @see Post
        * @see User
        */
       @Query("SELECT p FROM Post p WHERE p.author = :author")
       Page<Post> findByAuthorOrderByCreationDateDesc(@Param("author") User author, Pageable pageable);

       /**
        * Busca posts públicos de un autor con paginación.
        * 
        * Retorna solo los posts públicos de un autor específico
        * ordenados por fecha de creación descendente.
        * 
        * 
        * @param author   Autor de los posts
        * @param pageable Configuración de paginación
        * @return Página de posts públicos ordenados por fecha
        * 
        * @see Post
        * @see User
        * @see PrivacyType
        */
       @Query("SELECT p FROM Post p WHERE p.author = :author AND p.privacyType = 'PUBLIC'")
       Page<Post> findPublicPostsByAuthorOrderByCreationDateDesc(@Param("author") User author, Pageable pageable);

       /**
        * Busca todos los posts públicos del sistema.
        * 
        * Retorna posts donde tanto el autor como el post tienen
        * configuración de privacidad pública.
        * 
        * 
        * @param userPrivacy Tipo de privacidad del usuario
        * @param postPrivacy Tipo de privacidad del post
        * @param pageable    Configuración de paginación
        * @return Página de posts públicos ordenados por fecha
        * 
        * @see Post
        * @see PrivacyType
        */
       @Query("SELECT p FROM Post p WHERE p.author.privacyType = :userPrivacy AND p.privacyType = :postPrivacy " +
                     "ORDER BY p.creationDate DESC")
       Page<Post> findAllPublicPosts(@Param("userPrivacy") PrivacyType userPrivacy,
                     @Param("postPrivacy") PrivacyType postPrivacy,
                     Pageable pageable);

       /**
        * Busca posts con reportes pendientes.
        *
        * Retorna posts que tienen reportes con estado pendiente,
        * ordenados por fecha de reporte descendente.
        * 
        * 
        * @param pageable Configuración de paginación
        * @return Página de posts con reportes pendientes
        * 
        * @see Post
        * @see Report
        */
       @Query("SELECT p FROM Post p JOIN p.reports r WHERE r.status = 'PENDING' " +
                     "ORDER BY r.reportDate DESC")
       Page<Post> findPostsWithPendingReports(Pageable pageable);

       /**
        * Cuenta los comentarios de un post específico.
        * 
        * Método optimizado para obtener el conteo de comentarios
        * sin cargar todos los datos de los comentarios.
        * 
        * 
        * @param postId ID del post
        * @return Número total de comentarios del post
        * 
        * @see Comment
        */
       @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
       long countCommentsByPostId(@Param("postId") Long postId);

       /**
        * Cuenta las reacciones de un post específico.
        * 
        * Método optimizado para obtener el conteo de reacciones
        * sin cargar todos los datos de las reacciones.
        * 
        * 
        * @param postId ID del post
        * @return Número total de reacciones del post
        * 
        * @see Reaction
        */
       @Query("SELECT COUNT(r) FROM Reaction r WHERE r.post.id = :postId")
       long countReactionsByPostId(@Param("postId") Long postId);

       /**
        * Cuenta comentarios para múltiples posts en una sola consulta.
        * 
        * Consulta optimizada para obtener el conteo de comentarios
        * de múltiples posts sin realizar consultas individuales.
        * 
        * 
        * @param postIds Lista de IDs de posts
        * @return Lista de arrays [postId, count] con el conteo por post
        * 
        * @see Comment
        */
       @Query("SELECT c.post.id, COUNT(c) FROM Comment c WHERE c.post.id IN :postIds GROUP BY c.post.id")
       List<Object[]> countCommentsByPostIds(@Param("postIds") List<Long> postIds);

       /**
        * Cuenta reacciones para múltiples posts en una sola consulta.
        * 
        * Consulta optimizada para obtener el conteo de reacciones
        * de múltiples posts sin realizar consultas individuales.
        * 
        * 
        * @param postIds Lista de IDs de posts
        * @return Lista de arrays [postId, count] con el conteo por post
        * 
        * @see Reaction
        */
       @Query("SELECT r.post.id, COUNT(r) FROM Reaction r WHERE r.post.id IN :postIds GROUP BY r.post.id")
       List<Object[]> countReactionsByPostIds(@Param("postIds") List<Long> postIds);

       /**
        * Busca posts con contadores de comentarios y reacciones.
        * 
        * Consulta optimizada que retorna posts junto con sus contadores
        * de comentarios y reacciones en una sola consulta.
        * 
        * 
        * @param authorIds Lista de IDs de autores
        * @param privacy   Tipo de privacidad del post
        * @param pageable  Configuración de paginación
        * @return Página de arrays [post, commentCount, reactionCount]
        * 
        * @see Post
        * @see Comment
        * @see Reaction
        * @see PrivacyType
        */
       @Query("SELECT p, " +
                     "(SELECT COUNT(c) FROM Comment c WHERE c.post = p) as commentCount, " +
                     "(SELECT COUNT(r) FROM Reaction r WHERE r.post = p) as reactionCount " +
                     "FROM Post p WHERE p.author.id IN :authorIds AND p.privacyType = :privacy " +
                     "ORDER BY p.creationDate DESC")
       Page<Object[]> findPostsWithCountsByAuthorIds(@Param("authorIds") List<Long> authorIds,
                     @Param("privacy") PrivacyType privacy,
                     Pageable pageable);

       /**
        * Busca posts para el feed personal: incluye posts públicos de los autores
        * seguidos y todos los posts (públicos y privados) del propio usuario.
        *
        * Esto permite que el feed personal contenga las publicaciones privadas
        * del usuario autenticado además de las publicaciones públicas de los
        * que sigue.
        */
       @Query("SELECT p FROM Post p WHERE (p.author.id IN :authorIds AND p.privacyType = :postPrivacy) " +
                     "OR p.author.id = :currentUserId " +
                     "ORDER BY p.creationDate DESC, p.id DESC")
       Page<Post> findPostsForFeed(@Param("authorIds") List<Long> authorIds,
                     @Param("postPrivacy") PrivacyType postPrivacy,
                     @Param("currentUserId") Long currentUserId,
                     Pageable pageable);
}
