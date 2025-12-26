package com.mindhub.api.repository.userProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mindhub.api.model.user.User;
import com.mindhub.api.model.userProfile.UserProfile;

import java.util.Optional;

/**
 * Repositorio para la gestión de perfiles de usuario.
 *
 * Proporciona métodos para consultar y administrar los perfiles extendidos,
 * incluyendo información personal y profesional asociada a cada usuario.
 */

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    /**
     * Busca el perfil de un usuario específico.
     * 
     * Retorna el perfil extendido de un usuario, incluyendo
     * información personal y profesional.
     * 
     * 
     * @param user Usuario del cual obtener el perfil
     * @return Optional con el perfil del usuario, o vacío si no existe
     * 
     * @see UserProfile
     * @see User
     */
    @Query("SELECT p FROM UserProfile p WHERE p.user = :user")
    Optional<UserProfile> findByUser(@Param("user") User user);
}
