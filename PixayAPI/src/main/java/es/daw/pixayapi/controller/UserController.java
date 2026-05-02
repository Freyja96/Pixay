package es.daw.pixayapi.controller;

import es.daw.pixayapi.dto.response.UserProfileResponse;
import es.daw.pixayapi.entity.User;
import es.daw.pixayapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileResponse> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());

        UserProfileResponse response = new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfilePicture(),
                user.getDescription(),
                0, // TODO Seguidores
                0  // TODO Seguidos
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable Long id) {
        User user = userService.findById(id);

        UserProfileResponse response = new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfilePicture(),
                user.getDescription(),
                0, // TODO Seguidores
                0  // TODO Seguidos
        );
        return ResponseEntity.ok(response);
    }
}