package com.mindhub.api.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Servicio genérico base para operaciones CRUD.
 *
 * Define las operaciones básicas que todos los servicios
 * de entidades deben implementar.
 *
 * @param <T>  tipo de entidad
 * @param <ID> tipo del identificador de la entidad
 */

public interface GenericService<T, ID> {

    /**
     * Guarda una entidad en la base de datos.
     * 
     * @param entity Entidad a guardar
     * @return Entidad guardada
     */
    T save(T entity);

    /**
     * Busca una entidad por su ID.
     * 
     * @param id ID de la entidad
     * @return Optional con la entidad si existe
     */
    Optional<T> findById(ID id);

    /**
     * Busca una entidad por su ID o lanza excepción si no existe.
     * 
     * @param id ID de la entidad
     * @return Entidad encontrada
     * @throws RuntimeException si la entidad no existe
     */
    T findByIdOrThrow(ID id);

    /**
     * Obtiene todas las entidades.
     * 
     * @return Lista de todas las entidades
     */
    List<T> findAll();

    /**
     * Obtiene todas las entidades paginadas.
     * 
     * @param pageable Configuración de paginación
     * @return Página de entidades
     */
    Page<T> findAll(Pageable pageable);

    /**
     * Elimina una entidad por su ID.
     * 
     * @param id ID de la entidad a eliminar
     */
    void deleteById(ID id);

    /**
     * Elimina una entidad.
     * 
     * @param entity Entidad a eliminar
     */
    void delete(T entity);

    /**
     * Verifica si existe una entidad con el ID especificado.
     * 
     * @param id ID de la entidad
     * @return true si existe, false en caso contrario
     */
    boolean existsById(ID id);

    /**
     * Cuenta el número total de entidades.
     * 
     * @return Número total de entidades
     */
    long count();

    /**
     * Obtiene el nombre de la entidad para mensajes de log.
     * 
     * @return Nombre de la entidad
     */
    String getEntityName();
}
