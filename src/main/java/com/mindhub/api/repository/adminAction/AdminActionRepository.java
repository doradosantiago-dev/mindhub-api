package com.mindhub.api.repository.adminAction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mindhub.api.model.adminAction.AdminAction;

/**
 * Repositorio para la gestión de acciones administrativas.
 */

@Repository
public interface AdminActionRepository extends JpaRepository<AdminAction, Long> {

}
