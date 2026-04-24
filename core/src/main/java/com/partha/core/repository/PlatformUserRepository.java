package com.partha.core.repository;

import com.partha.core.model.PlatformUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlatformUserRepository extends JpaRepository<PlatformUser, Long> {

    // Find by username (login / lookup)
    Optional<PlatformUser> findByUsername(String username);

    //  Check if username exists
    boolean existsByUsername(String username);
}