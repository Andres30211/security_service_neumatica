package neumatica.security.segurity_service_neumatica.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import neumatica.security.segurity_service_neumatica.entity.User;

@Service
@RequiredArgsConstructor
public class JwtService {

	@Value("${app.jwt.secret}")
	private String secret;
	
	@Value("${app.jwt.issuer}")
	private String issuer;
	
	@Value("${app.jwt.access-token-expiration}")
	private long accessTokenExpiration;
	
	private SecretKey getSecretKey() {
	    return new SecretKeySpec(
	            secret.getBytes(StandardCharsets.UTF_8),
	            "HmacSHA256"
	    );
	}
	
	public String generateAccessToken(User user) {
	
	    Instant now = Instant.now();
	
	    var roles = user.getRoles()
	            .stream()
	            .map(role -> role.getName().name())
	            .collect(Collectors.toSet());
	
	    JwtClaimsSet claims = JwtClaimsSet.builder()
	            .issuer(issuer)
	            .issuedAt(now)
	            .expiresAt(now.plusMillis(accessTokenExpiration))
	            .subject(user.getId().toString())
	            .claim("email", user.getEmail())
	            .claim("name", user.getName())
	            .claim("roles", roles)
	            .build();
	
	    JwtEncoder encoder = NimbusJwtEncoder
	            .withSecretKey(getSecretKey())
	            .algorithm(MacAlgorithm.HS256)
	            .build();
	
	    return encoder
	            .encode(JwtEncoderParameters.from(claims))
	            .getTokenValue();
	}
	
	public long getAccessTokenExpiration() {
	    return accessTokenExpiration;
	}


}
