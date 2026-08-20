package neumatica.security.segurity_service_neumatica.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import neumatica.security.segurity_service_neumatica.entity.RefreshToken;
import neumatica.security.segurity_service_neumatica.entity.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID>{

	Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);

    void deleteByToken(String token);
}
