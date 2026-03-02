package es.daw.foodexpressapi.controller;

import es.daw.foodexpressapi.dto.AuthRequest;
import es.daw.foodexpressapi.dto.AuthResponse;
import es.daw.foodexpressapi.repository.UserRepository;
import es.daw.foodexpressapi.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    //necesito el repositorio:
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request){
        /* authenticationManager.authenticate(...) es el punto central de validación en Spring Security
        *  Internamente llama al UserDetailsService.loadUserByUsername(...) para obtener el UserDetails
        *  Compara la contraseña ingresada con la almacenada en la BDD (mediante el PasswordEncoder).
        *  Si las credenciales son correctas, devuelve un Authentication lleno de datos del usuario (con el usuario y el token)
        *  Si no lo son, lanza una excepción de tipo BadCredentialsException. */

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        //TODO pendiente!!! controlar la excepción BadCredentialsException
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
