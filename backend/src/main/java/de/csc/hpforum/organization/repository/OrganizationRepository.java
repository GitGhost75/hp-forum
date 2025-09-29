package de.csc.hpforum.organization.repository;

import de.csc.hpforum.organization.model.entity.Organization;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
}
