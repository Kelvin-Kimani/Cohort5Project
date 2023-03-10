package com.tracom.cohort5project.Repositories;

import com.tracom.cohort5project.Entities.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Integer> {

    Organization findByOrganizationId(int organizationId);
}
