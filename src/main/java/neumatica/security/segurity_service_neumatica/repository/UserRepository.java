package neumatica.security.segurity_service_neumatica.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import neumatica.security.segurity_service_neumatica.entity.User;

public interface UserRepository extends JpaRepository<User, UUID>{

	Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
