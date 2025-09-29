package de.csc.hpforum.user.repository;

import de.csc.hpforum.user.model.entity.Role;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, UUID> {
}
