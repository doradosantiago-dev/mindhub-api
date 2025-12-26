package com.mindhub.api.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mindhub.api.model.role.Role;
import com.mindhub.api.model.user.User;

import java.util.Optional;

/**
 * Repositorio para la gestión de usuarios del sistema.
 *
 * Proporciona métodos para consultar, verificar existencia, contar y filtrar
 * usuarios según distintos criterios como nombre de usuario, correo, estado
 * de la cuenta, rol o nivel de privacidad.
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

        /**
         * Busca un usuario por nombre de usuario.
         * 
         * @param username Nombre de usuario a buscar
         * @return Optional con el usuario, o vacío si no existe
         * 
         * @see User
         */
        Optional<User> findByUsername(String username);

        /**
         * Verifica si existe un usuario con el nombre de usuario especificado.
         * 
         * @param username Nombre de usuario a verificar
         * @return true si existe el usuario, false en caso contrario
         * 
         * @see User
         */
        boolean existsByUsername(String username);

        /**
         * Verifica si existe un usuario con el email especificado.
         * 
         * @param email Email a verificar
         * @return true si existe el usuario, false en caso contrario
         * 
         * @see User
         */
        boolean existsByEmail(String email);

        /**
         * Busca usuarios públicos por nombre o apellido.
         * 
         * Retorna usuarios públicos activos que coincidan con la búsqueda
         * en nombre, apellido o nombre completo.
         * 
         * 
         * @param search   Término de búsqueda
         * @param pageable Configuración de paginación
         * @return Página de usuarios públicos que coincidan con la búsqueda
         * 
         * @see User
         * @see PrivacyType
         */
        @Query("SELECT u FROM User u WHERE u.privacyType = 'PUBLIC' AND u.active = true AND " +
                        "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                        "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                        "LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :search, '%')))")
        Page<User> findPublicUsersByFirstNameOrLastName(@Param("search") String search, Pageable pageable);

        /**
         * Busca usuarios por nombre o apellido sin restricción de privacidad (para
         * admin).
         * 
         * Retorna usuarios que coincidan con la búsqueda en nombre, apellido o nombre
         * completo,
         * sin importar su tipo de privacidad. Usado exclusivamente por administradores.
         * 
         * @param search   Término de búsqueda
         * @param pageable Configuración de paginación
         * @return Página de usuarios que coincidan con la búsqueda
         * 
         * @see User
         */
        @Query("SELECT u FROM User u WHERE " +
                        "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                        "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
                        "LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :search, '%'))")
        Page<User> findUsersByFirstNameOrLastName(@Param("search") String search, Pageable pageable);

        /**
         * Cuenta el número total de usuarios activos.
         * 
         * Método optimizado para obtener el conteo de usuarios
         * activos sin cargar todos los datos.
         * 
         * 
         * @return Número total de usuarios activos
         * 
         * @see User
         */
        @Query("SELECT COUNT(u) FROM User u WHERE u.active = true")
        long countByActiveTrue();

        /**
         * Busca un administrador activo del sistema.
         * 
         * Retorna el primer usuario con rol de administrador
         * que esté activo en el sistema.
         * 
         * 
         * @return Optional con el administrador activo, o vacío si no existe
         * 
         * @see User
         * @see Role
         */
        @Query("SELECT u FROM User u WHERE u.role = :role AND u.active = true")
        Optional<User> findActiveAdmin();

        /**
         * Cuenta el número total de usuarios inactivos.
         * 
         * Método optimizado para obtener el conteo de usuarios
         * inactivos sin cargar todos los datos.
         * 
         * 
         * @return Número total de usuarios inactivos
         * 
         * @see User
         */
        @Query("SELECT COUNT(u) FROM User u WHERE u.active = false")
        long countByActiveFalse();

        /**
         * Cuenta el número de administradores activos en el sistema.
         *
         * @return número de administradores activos
         */
        @Query("SELECT COUNT(u) FROM User u WHERE u.role.name = 'ADMIN' AND u.active = true")
        long countActiveAdmins();

        /**
         * Busca todos los usuarios con rol de administrador que estén activos.
         * 
         * @param role Rol de administrador
         * @return Lista de administradores activos
         * 
         * @see User
         * @see Role
         */
        @Query("SELECT u FROM User u WHERE u.role = :role AND u.active = true")
        java.util.List<User> findByRoleAndActiveTrue(@Param("role") Role role);
}
