package com.tracom.cohort5project.Services;

import com.tracom.cohort5project.Entities.Organization;
import com.tracom.cohort5project.Repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrganizationService {

    private OrganizationRepository organizationRepository;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository){
        this.organizationRepository = organizationRepository;
    }

    /*CREATE*/
    public void createOrganization(Organization organization){
        organizationRepository.save(organization);
    }

    /*READ*/
    public List<Organization> getOrganizations() {
        return organizationRepository.findAll();
    }
}
