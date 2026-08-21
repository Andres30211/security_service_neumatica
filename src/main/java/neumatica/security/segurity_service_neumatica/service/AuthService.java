package neumatica.security.segurity_service_neumatica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import neumatica.security.segurity_service_neumatica.dto.AuthResponse;
import neumatica.security.segurity_service_neumatica.dto.LoginRequest;
import neumatica.security.segurity_service_neumatica.dto.RefreshTokenRequest;
import neumatica.security.segurity_service_neumatica.dto.RegisterRequest;
import neumatica.security.segurity_service_neumatica.entity.RefreshToken;
import neumatica.security.segurity_service_neumatica.entity.Role;
import neumatica.security.segurity_service_neumatica.entity.User;
import neumatica.security.segurity_service_neumatica.enums.RoleName;
import neumatica.security.segurity_service_neumatica.repository.RoleRepository;
import neumatica.security.segurity_service_neumatica.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	@Autowired
	private UserRepository userRepository;
    
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;

	@Autowired
    private AuthenticationManager authenticationManager;

	@Autowired
    private JwtService jwtService;

	@Autowired
    private RefreshTokenService refreshTokenService;
	

	public AuthResponse register(RegisterRequest request) {

	    if (this.userRepository.existsByEmail(request.email())) {
	        throw new RuntimeException("El usuario ya existe");
	    }

	    RoleName roleName;

	    // El primer usuario será ADMIN
	    if (this.userRepository.count() == 0) {
	        roleName = RoleName.ROLE_ADMIN;
	    } else {
	        roleName = RoleName.ROLE_USER;
	    }

	    Role userRole = this.roleRepository
	            .findByName(roleName)
	            .orElseThrow(() ->
	                    new RuntimeException(
	                            roleName + " no existe"
	                    )
	            );

	    User user = User.builder()
	            .name(request.name())
	            .email(request.email())
	            .password(this.passwordEncoder.encode(request.password()))
	            .enabled(true)
	            .accountNonLocked(true)
	            .build();

	    user.getRoles().add(userRole);

	    user = this.userRepository.save(user);

	    return this.generateTokens(user);
	}

    public AuthResponse login(LoginRequest request) {

    	this.authenticationManager
    		.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        User user = this.userRepository
                .findByEmail(request.email())
                .orElseThrow();

        return this.generateTokens(user);
    }

    public AuthResponse refresh(RefreshTokenRequest request) {

        RefreshToken refreshToken = this.refreshTokenService.validate(request.refreshToken());

        User user = refreshToken.getUser();

        String accessToken = this.jwtService.generateAccessToken(user);

        return new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer",
                this.jwtService.getAccessTokenExpiration()
        );
    }

    public void logout(String refreshToken) {

    	this.refreshTokenService.delete(refreshToken);
    }

    private AuthResponse generateTokens(User user) {

        String accessToken = this.jwtService.generateAccessToken(user);

        RefreshToken refreshToken = this.refreshTokenService.create(user);

        return new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer",
                this.jwtService.getAccessTokenExpiration()
        );
    }
}
