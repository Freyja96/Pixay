package es.daw.pixayapi.controller;

import es.daw.pixayapi.dto.AuthRequest;
import es.daw.pixayapi.dto.AuthResponse;
import es.daw.pixayapi.entity.User;
import es.daw.pixayapi.repository.UserRepository;
import es.daw.pixayapi.security.JwtService;
import es.daw.pixayapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    //necesito el repositorio:
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService; // <--- Inyectamos tu nuevo servicio

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            userService.registrarUsuario(user);
            return ResponseEntity.ok("Usuario registrado con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al registrar: " + e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request){
        //  HASH CORRECTO para admin123
        System.out.println("HASH GENERADO PARA 'admin123': " + new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("admin123"));
        try {
            // LOG DE SEGURIDAD
            System.out.println("Intentando autenticar a: " + request.getUsername());

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            System.out.println("¡Autenticación exitosa!");
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (BadCredentialsException e) {
            System.out.println("ERROR: Contraseña incorrecta para " + request.getUsername());
            return ResponseEntity.status(401).body("Usuario o contraseña incorrectos");
        } catch (Exception e) {
            System.out.println("ERROR GENÉRICO: " + e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
