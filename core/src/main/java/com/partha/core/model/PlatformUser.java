package com.partha.core.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "platform_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "is_premium")
    private boolean isPremium;
}