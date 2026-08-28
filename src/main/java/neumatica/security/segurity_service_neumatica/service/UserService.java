package neumatica.security.segurity_service_neumatica.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import neumatica.security.segurity_service_neumatica.dto.ChangeRoleRequest;
import neumatica.security.segurity_service_neumatica.dto.UpdateUserRequest;
import neumatica.security.segurity_service_neumatica.dto.UserResponse;
import neumatica.security.segurity_service_neumatica.entity.Role;
import neumatica.security.segurity_service_neumatica.entity.User;
import neumatica.security.segurity_service_neumatica.repository.RefreshTokenRepository;
import neumatica.security.segurity_service_neumatica.repository.RoleRepository;
import neumatica.security.segurity_service_neumatica.repository.UserRepository;


/**
 * Servicio encargado de gestionar los usuarios.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {


    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final RefreshTokenRepository refreshTokenRepository;


    // =========================================
    // LISTAR USUARIOS
    // =========================================

    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
    }


    // =========================================
    // OBTENER USUARIO POR ID
    // =========================================

    /**
     * Este método es especialmente importante
     * para el location-service.
     *
     * El location-service solamente conoce
     * el UUID del vendedor.
     *
     * Entonces realiza:
     *
     * GET /api/users/{id}
     *
     * y este método busca el usuario.
     */
    public UserResponse getUserById(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado"
                        )
                );

        return UserResponse.fromEntity(user);
    }


    // =========================================
    // ACTUALIZAR DATOS BÁSICOS
    // =========================================

    public UserResponse updateUser(
            UUID id,
            UpdateUserRequest request
    ) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado"
                        )
                );

        user.setName(request.name());
        user.setEmail(request.email());

        User updatedUser =
                userRepository.save(user);

        return UserResponse.fromEntity(
                updatedUser
        );
    }


    // =========================================
    // CAMBIAR ROL
    // =========================================

    public UserResponse changeRole(
            UUID id,
            ChangeRoleRequest request
    ) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado"
                        )
                );

        Role role = roleRepository
                .findByName(request.role())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Rol no encontrado"
                        )
                );

        user.getRoles().clear();

        user.getRoles().add(role);

        User updatedUser =
                userRepository.save(user);

        return UserResponse.fromEntity(
                updatedUser
        );
    }


    // =========================================
    // ELIMINAR USUARIO
    // =========================================

    public void deleteUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado"
                        )
                );

        refreshTokenRepository.deleteByUser(user);

        userRepository.delete(user);
    }
}
