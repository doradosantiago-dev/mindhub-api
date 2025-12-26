package com.mindhub.api.repository.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mindhub.api.model.notification.Notification;
import com.mindhub.api.model.user.User;

/**
 * Repositorio para la gestión de notificaciones del sistema.
 *
 * Proporciona métodos para consultar, contar y administrar las notificaciones
 * de los usuarios, incluyendo funcionalidades de lectura y paginación.
 */

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Busca notificaciones de un usuario con paginación.
     * 
     * Retorna todas las notificaciones de un usuario ordenadas
     * por fecha de creación descendente (más recientes primero).
     * 
     * 
     * @param user     Usuario propietario de las notificaciones
     * @param pageable Configuración de paginación
     * @return Página de notificaciones ordenadas por fecha
     * 
     * @see Notification
     * @see User
     */
    Page<Notification> findByUserOrderByCreationDateDesc(User user, Pageable pageable);

    /**
     * Cuenta las notificaciones no leídas de un usuario.
     * 
     * Método optimizado para obtener el conteo de notificaciones
     * pendientes de lectura sin cargar todos los datos.
     * 
     * 
     * @param user Usuario del cual contar las notificaciones no leídas
     * @return Número total de notificaciones no leídas
     * 
     * @see User
     */
    long countByUserAndReadFalse(User user);

    /**
     * Busca notificaciones no leídas de un usuario con paginación.
     * 
     * Retorna solo las notificaciones pendientes de lectura ordenadas
     * por fecha de creación descendente (más recientes primero).
     * 
     * 
     * @param user     Usuario propietario de las notificaciones
     * @param pageable Configuración de paginación
     * @return Página de notificaciones no leídas ordenadas por fecha
     * 
     * @see Notification
     * @see User
     */
    Page<Notification> findByUserAndReadFalseOrderByCreationDateDesc(User user, Pageable pageable);

    /**
     * Marca todas las notificaciones de un usuario como leídas.
     * 
     * Método transaccional que actualiza el estado de lectura
     * y la fecha de lectura de todas las notificaciones pendientes.
     * 
     * 
     * @param userId ID del usuario cuyas notificaciones se marcarán como leídas
     * 
     * @see User
     */
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.read = true, n.readDate = CURRENT_DATE WHERE n.user.id = :userId AND n.read = false")
    void markAllAsReadForUser(@Param("userId") Long userId);

}
