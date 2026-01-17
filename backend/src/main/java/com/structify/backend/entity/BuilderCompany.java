package com.structify.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "builder_companies")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BuilderCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 One Builder = One Company
    // ❌ Prevent infinite JSON recursion
    @OneToOne
    @JoinColumn(name = "builder_id", nullable = false, unique = true)
    @JsonIgnore
    private User builder;

    @Column(nullable = false)
    private String companyName;

    private int experienceYears;

    private int numberOfProjects;

    private double costPerSqFt;

    private String location;

    private boolean sustainabilityFocused;
}
