package neumatica.security.segurity_service_neumatica.dto;

public record AuthResponse(
		
		String accessToken,

        String refreshToken,

        String tokenType,

        long expiresIn
		
		) {

}
