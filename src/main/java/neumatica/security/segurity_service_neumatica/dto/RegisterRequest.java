package neumatica.security.segurity_service_neumatica.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
		
		@NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100)
        String name,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no es válido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 100)
        String password
		
		) {

}
