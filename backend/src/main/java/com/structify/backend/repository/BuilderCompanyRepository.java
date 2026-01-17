package com.structify.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.structify.backend.entity.BuilderCompany;
import com.structify.backend.entity.User;

public interface BuilderCompanyRepository
        extends JpaRepository<BuilderCompany, Long> {

    // EXISTING (DO NOT REMOVE)
    Optional<BuilderCompany> findByBuilder(User builder);

    List<BuilderCompany> findByLocationIgnoreCase(String location);

    // ✅ NEW — SAFE FILTER QUERY
    @Query("""
        SELECT b FROM BuilderCompany b
        WHERE (:maxCost IS NULL OR b.costPerSqFt <= :maxCost)
          AND (:location IS NULL OR LOWER(b.location) LIKE LOWER(CONCAT('%', :location, '%')))
          AND (:sustainable = false OR b.sustainabilityFocused = true)
    """)
    List<BuilderCompany> filterBuilders(
            @Param("maxCost") Double maxCost,
            @Param("location") String location,
            @Param("sustainable") boolean sustainable
    );
}
