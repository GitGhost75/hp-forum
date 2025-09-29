package de.csc.hpforum.organization.repository;

import de.csc.hpforum.organization.model.entity.OrganizationCategory;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationCategoryRepository extends JpaRepository<OrganizationCategory, UUID> {
}
