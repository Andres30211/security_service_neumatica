package neumatica.security.segurity_service_neumatica.dto;

import jakarta.validation.constraints.NotNull;
import neumatica.security.segurity_service_neumatica.enums.RoleName;


public record ChangeRoleRequest(
		
		@NotNull
        RoleName role
		
		) {

}
