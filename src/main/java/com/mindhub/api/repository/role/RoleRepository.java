package com.mindhub.api.repository.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mindhub.api.model.role.Role;

import java.util.Optional;

/**
 * Repositorio para la gestión de roles del sistema.
 *
 * Proporciona métodos para consultar y administrar los roles de usuario,
 * incluyendo búsqueda por nombre, verificación de existencia y obtención
 * del rol configurado como predeterminado.
 */

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Busca un rol activo por nombre.
     * 
     * Retorna un rol específico que esté activo en el sistema,
     * si existe.
     * 
     * 
     * @param name Nombre del rol a buscar
     * @return Optional con el rol activo, o vacío si no existe
     * 
     * @see Role
     */
    Optional<Role> findByNameAndActiveTrue(String name);

    /**
     * Busca el rol por defecto del sistema.
     * 
     * Retorna el rol configurado como predeterminado
     * que esté activo en el sistema.
     * 
     * 
     * @return Optional con el rol por defecto, o vacío si no existe
     * 
     * @see Role
     */
    @Query("SELECT r FROM Role r WHERE r.defaultRole = true AND r.active = true")
    Optional<Role> findDefaultRole();

    /**
     * Verifica si existe un rol con el nombre especificado.
     * 
     * @param name Nombre del rol a verificar
     * @return true si existe el rol, false en caso contrario
     * 
     * @see Role
     */
    boolean existsByName(String name);
}
