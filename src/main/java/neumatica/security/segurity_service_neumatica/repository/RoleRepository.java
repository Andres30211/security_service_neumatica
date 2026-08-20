package neumatica.security.segurity_service_neumatica.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import neumatica.security.segurity_service_neumatica.entity.Role;
import neumatica.security.segurity_service_neumatica.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role, UUID>{

	Optional<Role> findByName(RoleName name);
}
