package com.tracom.cohort5project.Services;

import com.tracom.cohort5project.Entities.Organization;
import com.tracom.cohort5project.Repositories.OrganizationRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    /*CREATE*/
    public void createOrganization(Organization organization) {
        organizationRepository.save(organization);
    }

    /*READ*/
    public List<Organization> getOrganizations() {
        return organizationRepository.findAll();
    }
}
