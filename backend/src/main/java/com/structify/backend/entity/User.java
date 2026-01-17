package com.structify.backend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String role; // ROLE_USER, ROLE_ADMIN, ROLE_BUILDER

    @Column(name = "is_builder")
    private boolean builder;

    @Column(nullable = false)
    private boolean approved = false;

    @OneToOne(mappedBy = "builder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BuilderCompany builderCompany;

    // Additional fields for AI recommendation (optional, you can fill later)
    private String skills;   // Example: "Masonry, Eco-construction"
    private int experience;  // in years
    private String location; // City/Area
    

}
