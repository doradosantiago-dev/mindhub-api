package com.mindhub.api.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mindhub.api.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio personalizado de Spring Security para la carga de usuarios.
 *
 * Implementa la interfaz UserDetailsService, utilizada por Spring Security
 * durante el proceso de autenticación. Se encarga de recuperar un usuario
 * desde la base de datos a partir de su nombre de usuario.
 *
 * Si el usuario no existe, se lanza una excepción UsernameNotFoundException,
 * lo que impide la autenticación.
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    /** Repositorio de usuarios para acceder a la base de datos. */
    private final UserRepository userRepository;

    /**
     * Carga un usuario por su nombre de usuario.
     *
     * Busca en la base de datos un usuario con el nombre proporcionado.
     * Si no se encuentra, lanza una excepción {@link UsernameNotFoundException}.
     * 
     *
     * @param username nombre de usuario a buscar
     * @return detalles del usuario que implementan {@link UserDetails}
     * @throws UsernameNotFoundException si no se encuentra el usuario
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Buscando usuario con username: {}", username);

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }
}
