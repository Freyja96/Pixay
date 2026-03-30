package es.daw.pixayapi.controller;

import es.daw.pixayapi.dto.response.UserProfileResponse;
import es.daw.pixayapi.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UserController {
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileResponse> getMyProfile(@AuthenticationPrincipal User user) {
        // Mapeamos los datos de la entidad al Record
        UserProfileResponse response = new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfilePicture(), // Asegúrate de que tu entidad User tenga este campo
                user.getDescription(),
                0, // TODO: Implementar lógica de conteo de seguidores
                0               // TODO: Implementar lógica de conteo de seguidos
        );
        return ResponseEntity.ok(response);
    }
}