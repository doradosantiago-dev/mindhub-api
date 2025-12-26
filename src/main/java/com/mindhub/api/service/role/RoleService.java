package com.mindhub.api.service.role;

import java.util.Optional;

import com.mindhub.api.model.role.Role;
import com.mindhub.api.service.base.GenericService;

/**
 * Servicio para la gestión de roles del sistema.
 *
 * Proporciona funcionalidades para gestionar roles de usuario,
 * incluyendo la creación de roles por defecto y validaciones.
 */

public interface RoleService extends GenericService<Role, Long> {

    /**
     * Busca un rol por su nombre.
     * 
     * @param name Nombre del rol
     * @return Rol encontrado o empty si no existe
     */
    Optional<Role> findByName(String name);

    /**
     * Busca un rol por su nombre o lanza excepción si no existe.
     * 
     * @param name Nombre del rol
     * @return Rol encontrado
     * @throws RoleNotFoundException si el rol no existe
     */
    Role findByNameOrThrow(String name);

    /**
     * Obtiene el rol por defecto del sistema.
     * 
     * @return Rol por defecto
     */
    Role getDefaultRole();

    /**
     * Verifica si existe un rol con el nombre especificado.
     * 
     * @param name Nombre del rol
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);

    /**
     * Crea los roles por defecto si no existen.
     */
    void createDefaultRolesIfNotExist();
}
