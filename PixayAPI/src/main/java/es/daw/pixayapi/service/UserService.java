package es.daw.pixayapi.service;

import es.daw.pixayapi.entity.User;
import es.daw.pixayapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Inyectas el Bean de SecurityConfig

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
    }

    public void registrarUsuario(User user) {
        //Recibe contraseña plana
        String passwordPlana = user.getPassword();
        // hashea
        String passwordHasheada = passwordEncoder.encode(passwordPlana);
        //Guarda el hash en la BD
        user.setPassword(passwordHasheada);
        userRepository.save(user);
    }

    public void save(User user) {
        userRepository.save(user);
    }
}