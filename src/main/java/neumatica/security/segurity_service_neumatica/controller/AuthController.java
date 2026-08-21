package neumatica.security.segurity_service_neumatica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import neumatica.security.segurity_service_neumatica.dto.AuthResponse;
import neumatica.security.segurity_service_neumatica.dto.LoginRequest;
import neumatica.security.segurity_service_neumatica.dto.RefreshTokenRequest;
import neumatica.security.segurity_service_neumatica.dto.RegisterRequest;
import neumatica.security.segurity_service_neumatica.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	@Autowired
	private AuthService authService;
	
	@GetMapping("/despertar")
    public ResponseEntity<String> despertar() {
        return ResponseEntity.ok("Servidor disponible...");
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
    	System.out.println(request.email());
        return ResponseEntity.ok(
        		this.authService.login(request)
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @Valid @RequestBody RefreshTokenRequest request) {

        return ResponseEntity.ok(
        		this.authService.refresh(request)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Valid @RequestBody RefreshTokenRequest request) {

    	this.authService.logout(request.refreshToken());

        return ResponseEntity.noContent().build();
    }
}
