package com.partha.core.repository;

import com.partha.core.model.Bot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BotRepository extends JpaRepository<Bot, Long> {

    // Find by name
    Bot findByName(String name);
}