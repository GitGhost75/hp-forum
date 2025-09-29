package de.csc.hpforum.user.repository;

import de.csc.hpforum.user.model.entity.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {
}
