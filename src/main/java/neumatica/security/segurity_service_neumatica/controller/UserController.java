package neumatica.security.segurity_service_neumatica.controller;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import neumatica.security.segurity_service_neumatica.dto.UserResponse;
import neumatica.security.segurity_service_neumatica.entity.User;
import neumatica.security.segurity_service_neumatica.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	@Autowired
	private UserRepository userRepository;

    @GetMapping("/me")
    public UserResponse me(Authentication authentication) {

        String email = authentication.name();

        User user = this.userRepository
                .findByEmail(email)
                .orElseThrow();

        return UserResponse.fromEntity(user);
    }
}
