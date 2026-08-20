package neumatica.security.segurity_service_neumatica.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
		
		@NotBlank
        String refreshToken

		
		) {

}
