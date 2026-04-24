package com.partha.core.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bots")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "persona_description", columnDefinition = "TEXT")
    private String personaDescription;
}