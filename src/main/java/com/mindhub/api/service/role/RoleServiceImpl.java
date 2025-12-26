package com.mindhub.api.service.role;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mindhub.api.exception.RoleNotFoundException;
import com.mindhub.api.model.role.Role;
import com.mindhub.api.repository.role.RoleRepository;
import com.mindhub.api.service.base.GenericServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio de roles.
 *
 * Gestiona las operaciones CRUD de roles, incluyendo la creación
 * de roles por defecto y validaciones de existencia.
 */

@Slf4j
@Service
@Transactional
public class RoleServiceImpl extends GenericServiceImpl<Role, Long> implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        super(roleRepository);
        this.roleRepository = roleRepository;
    }

    /**
     * Busca un rol por su nombre.
     * 
     * @param name Nombre del rol
     * @return Rol encontrado o vacío si no existe
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findByName(String name) {
        log.debug("Buscando rol con nombre {}");

        return roleRepository.findByNameAndActiveTrue(name);
    }

    /**
     * Busca un rol por su nombre o lanza excepción si no existe.
     * 
     * @param name Nombre del rol
     * @return Rol encontrado
     * @throws RoleNotFoundException si el rol no existe
     */
    @Override
    @Transactional(readOnly = true)
    public Role findByNameOrThrow(String name) {
        log.debug("Buscando rol con nombre {}", name);

        return findByName(name)
                .orElseThrow(() -> new RoleNotFoundException("Role no encontrado: " + name));
    }

    /**
     * Obtiene el rol por defecto del sistema.
     * 
     * @return Rol por defecto
     */
    @Override
    @Transactional(readOnly = true)
    public Role getDefaultRole() {
        log.debug("Obteniendo rol por defecto del sistema");

        return roleRepository.findDefaultRole()
                .orElseGet(() -> findByNameOrThrow(Role.USER));
    }

    /**
     * Verifica si existe un rol con el nombre especificado.
     * 
     * @param name Nombre del rol
     * @return true si existe, false en caso contrario
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        log.debug("Verificando existencia del rol {}", name);

        return roleRepository.existsByName(name);
    }

    /**
     * Crea los roles por defecto si no existen.
     */
    @Override
    public void createDefaultRolesIfNotExist() {
        log.debug("Verificando creación de roles por defecto");

        if (!existsByName(Role.ADMIN)) {
            log.info("Creando rol por defecto: ADMIN");

            Role adminRole = Role.builder()
                    .name(Role.ADMIN)
                    .description("Administrador")
                    .icon("fas fa-crown")
                    .color("#dc3545")
                    .active(true)
                    .defaultRole(false)
                    .build();

            save(adminRole);
        }

        if (!existsByName(Role.USER)) {
            log.info("Creando rol por defecto: USER");

            Role userRole = Role.builder()
                    .name(Role.USER)
                    .description("Usuario")
                    .icon("fas fa-user")
                    .color("#007bff")
                    .active(true)
                    .defaultRole(true)
                    .build();

            save(userRole);
        }
    }

    @Override
    protected Long getEntityId(Role entity) {
        log.debug("Obteniendo ID del rol {}", entity.getName());

        return entity.getId();
    }

    @Override
    protected RuntimeException createNotFoundException(String message) {
        log.error("Excepción de rol no encontrado: {}", message);

        return new RoleNotFoundException(message);
    }

    @Override
    public String getEntityName() {
        return "Role";
    }
}
