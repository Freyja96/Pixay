package es.daw.pixayapi.controller;

import es.daw.pixayapi.dto.request.UserProfileUpdateRequest;
import es.daw.pixayapi.dto.response.UserProfileResponse;
import es.daw.pixayapi.entity.User;
import es.daw.pixayapi.security.JwtService;
import es.daw.pixayapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

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

    @PatchMapping("/me")
    public ResponseEntity<Void> updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                                             @RequestBody UserProfileUpdateRequest dto) {

        User user = userService.findByUsername(userDetails.getUsername());
        boolean userNameChanged =  dto.getUsername() !=  null && !dto.getUsername().equals(user.getUsername());


        if (dto.getEmail() != null ) user.setEmail(dto.getEmail());
        if (dto.getDescription() != null ) user.setDescription(dto.getDescription());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        userService.save(user);




        return ResponseEntity.noContent().build();
    }
}