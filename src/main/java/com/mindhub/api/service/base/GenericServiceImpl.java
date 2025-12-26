package com.mindhub.api.service.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación genérica base para operaciones CRUD.
 *
 * Proporciona implementaciones comunes para todas las operaciones
 * básicas de servicios de entidades.
 *
 * @param <T>  tipo de entidad
 * @param <ID> tipo del identificador de la entidad
 */

@Slf4j
@Transactional
public abstract class GenericServiceImpl<T, ID> implements GenericService<T, ID> {

    protected final JpaRepository<T, ID> repository;

    protected GenericServiceImpl(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    /**
     * Guarda una entidad en la base de datos.
     * 
     * @param entity Entidad a guardar
     * @return Entidad guardada con ID generado
     */
    @Override
    public T save(T entity) {
        T savedEntity = repository.save(entity);

        log.debug("Entidad {} guardada con ID: {}", getEntityName(), getEntityId(savedEntity));

        return savedEntity;
    }

    /**
     * Busca una entidad por su ID.
     * 
     * @param id ID de la entidad a buscar
     * @return Optional que contiene la entidad si existe
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    /**
     * Busca una entidad por su ID y lanza excepción si no existe.
     * 
     * @param id ID de la entidad a buscar
     * @return Entidad encontrada
     * @throws RuntimeException si la entidad no existe
     */
    @Override
    @Transactional(readOnly = true)
    public T findByIdOrThrow(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    String message = String.format("%s no encontrado con ID: %s", getEntityName(), id);

                    log.error(message);

                    return createNotFoundException(message);
                });
    }

    /**
     * Obtiene todas las entidades.
     * 
     * @return Lista de todas las entidades
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return repository.findAll();
    }

    /**
     * Obtiene todas las entidades con paginación.
     * 
     * @param pageable Configuración de paginación
     * @return Página de entidades
     */
    @Override
    @Transactional(readOnly = true)
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Elimina una entidad por su ID.
     * 
     * @param id ID de la entidad a eliminar
     * @throws RuntimeException si la entidad no existe
     */
    @Override
    public void deleteById(ID id) {
        if (!repository.existsById(id)) {
            String message = String.format("%s no encontrado para eliminar con ID: %s", getEntityName(), id);

            log.error(message);

            throw createNotFoundException(message);
        }

        repository.deleteById(id);

        log.debug("{} eliminado con ID: {}", getEntityName(), id);
    }

    /**
     * Elimina una entidad.
     * 
     * @param entity Entidad a eliminar
     */
    @Override
    public void delete(T entity) {
        ID id = getEntityId(entity);

        repository.delete(entity);

        log.debug("{} eliminado con ID: {}", getEntityName(), id);
    }

    /**
     * Verifica si existe una entidad con el ID especificado.
     * 
     * @param id ID de la entidad a verificar
     * @return true si existe, false en caso contrario
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    /**
     * Cuenta el número total de entidades.
     * 
     * @return Número total de entidades
     */
    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    /**
     * Método abstracto para obtener el ID de la entidad.
     * 
     * <p>
     * Cada implementación debe definir cómo extraer el ID
     * de la entidad específica.
     * </p>
     * 
     * @param entity Entidad de la cual extraer el ID
     * @return ID de la entidad
     */
    protected abstract ID getEntityId(T entity);

    /**
     * Método abstracto para crear la excepción apropiada cuando no se encuentra la
     * entidad.
     * 
     * <p>
     * Cada implementación puede usar su propia excepción específica
     * para proporcionar mensajes de error más descriptivos.
     * </p>
     * 
     * @param message Mensaje de error
     * @return Excepción apropiada para la entidad
     */
    protected abstract RuntimeException createNotFoundException(String message);
}
