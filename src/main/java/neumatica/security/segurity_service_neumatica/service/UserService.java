package neumatica.security.segurity_service_neumatica.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import neumatica.security.segurity_service_neumatica.dto.ChangeRoleRequest;
import neumatica.security.segurity_service_neumatica.dto.UpdateUserRequest;
import neumatica.security.segurity_service_neumatica.dto.UserResponse;
import neumatica.security.segurity_service_neumatica.entity.Role;
import neumatica.security.segurity_service_neumatica.entity.User;
import neumatica.security.segurity_service_neumatica.repository.RoleRepository;
import neumatica.security.segurity_service_neumatica.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	private UserResponse userResponse;
	
	
	// =========================================
    // LISTAR
    // =========================================

    //@Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
    }


    // =========================================
    // OBTENER POR ID
    // =========================================

    //@Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado"
                        )
                );

        return this.userResponse.fromEntity(user);
    }


    // =========================================
    // ACTUALIZAR DATOS BÁSICOS
    // =========================================

    public UserResponse updateUser(UUID id, UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado"
                        )
                );

        user.setName(request.name());
        user.setEmail(request.email());

        User updatedUser = userRepository.save(user);

        return this.userResponse.fromEntity(updatedUser);
    }


    // =========================================
    // CAMBIAR ROL
    // =========================================
    
    public UserResponse changeRole(UUID id, ChangeRoleRequest request) {

        System.out.println("========== CHANGE ROLE ==========");
        System.out.println("ID: " + id);
        System.out.println("ROLE: " + request.role());

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado: " + id)
                );

        System.out.println("Usuario encontrado: " + user.getEmail());

        Role role = roleRepository.findByName(request.role())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Rol no encontrado: " + request.role()
                        )
                );

        System.out.println("Rol encontrado: " + role.getName());

        user.setRoles(Set.of(role));

        System.out.println("Roles asignados al usuario");

        User updatedUser = userRepository.save(user);

        System.out.println("Usuario guardado correctamente");

        UserResponse response = this.userResponse.fromEntity(updatedUser);

        System.out.println("DTO creado correctamente");

        return response;
    }

    /*public UserResponse changeRole(UUID id, ChangeRoleRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado")
                );

        Role role = roleRepository.findByName(request.role())
                .orElseThrow(() ->
                        new RuntimeException("Rol no encontrado")
                );

        user.setRoles(Set.of(role));

        User updatedUser = userRepository.save(user);

        return this.userResponse.fromEntity(updatedUser);
    }*/


    // =========================================
    // ELIMINAR
    // =========================================

    public void deleteUser(UUID id) {

        if (!userRepository.existsById(id)) {
            throw new RuntimeException(
                    "Usuario no encontrado"
            );
        }

        userRepository.deleteById(id);
    }


    // =========================================
    // MAPPER
    // =========================================

    /*private UserResponse toDTO(User user) {

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                null
        );
    }*/

}
