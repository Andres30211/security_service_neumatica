package neumatica.security.segurity_service_neumatica.dto;

import java.util.Set;
import java.util.UUID;

import neumatica.security.segurity_service_neumatica.entity.User;


public record UserResponse(
        UUID id,
        String name,
        String email,
        Set<String> roles,
        boolean enabled
) {

    public static UserResponse fromEntity(User user) {

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRoles()
                        .stream()
                        .map(role -> role.getName().name())
                        .collect(java.util.stream.Collectors.toSet()),
                user.isEnabled()
        );
    }
}
