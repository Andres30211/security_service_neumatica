package neumatica.security.segurity_service_neumatica.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import neumatica.security.segurity_service_neumatica.entity.RefreshToken;
import neumatica.security.segurity_service_neumatica.entity.User;
import neumatica.security.segurity_service_neumatica.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
	
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
	@Value("${app.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

	@Transactional
    public RefreshToken create(User user) {

        this.refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(
                        LocalDateTime.now()
                                .plusNanos(
                                        refreshTokenExpiration * 1_000_000
                                )
                )
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken validate(String token) {

        RefreshToken refreshToken =
                refreshTokenRepository
                        .findByToken(token)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Refresh token inválido"
                                )
                        );

        if (refreshToken
                .getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            refreshTokenRepository.delete(refreshToken);

            throw new RuntimeException(
                    "Refresh token expirado"
            );
        }

        return refreshToken;
    }

    public void delete(String token) {

        refreshTokenRepository.deleteByToken(token);
    }
}
