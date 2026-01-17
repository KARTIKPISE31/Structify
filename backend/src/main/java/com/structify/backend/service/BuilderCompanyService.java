package com.structify.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.structify.backend.entity.BuilderCompany;
import com.structify.backend.entity.User;
import com.structify.backend.repository.BuilderCompanyRepository;

@Service
public class BuilderCompanyService {

    private final BuilderCompanyRepository builderCompanyRepository;

    public BuilderCompanyService(BuilderCompanyRepository builderCompanyRepository) {
        this.builderCompanyRepository = builderCompanyRepository;
    }

    /**
     * Check if company already exists for builder
     */
    public boolean companyExistsForBuilder(User builder) {
        return builderCompanyRepository.findByBuilder(builder).isPresent();
    }

    /**
     * Save company details (ONLY ONCE)
     */
    @Transactional
    public void saveCompany(BuilderCompany company, User builder) {

        Optional<BuilderCompany> existing =
                builderCompanyRepository.findByBuilder(builder);

        if (existing.isPresent()) {
            throw new IllegalStateException(
                "Company details already submitted. You cannot submit again."
            );
        }

        company.setBuilder(builder);
        builderCompanyRepository.save(company);
    }

    /**
     * Get company by builder
     */
    public Optional<BuilderCompany> getCompanyByBuilder(User builder) {
        return builderCompanyRepository.findByBuilder(builder);
    }

    /**
     * 🔥 NEW (SAFE)
     * Get all builder companies (for USER view)
     */
    public List<BuilderCompany> getAllApprovedBuilders() {
        return builderCompanyRepository.findAll();
    }
}
