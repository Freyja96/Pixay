package es.daw.pixaymvc.controlador;

import es.daw.pixaymvc.dto.response.UserProfileResponse; // Asegúrate de importar tu Record
import es.daw.pixaymvc.service.ApiAuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
public class UserController {
    private final ApiAuthService apiAuthService;
    private final WebClient webClientAPI; // Añadimos WebClient

    public UserController(ApiAuthService apiAuthService, WebClient webClientAPI) {
        this.apiAuthService = apiAuthService;
        this.webClientAPI = webClientAPI;
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "pantallas/login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String username,
                                @RequestParam String password,
                                HttpSession session,
                                Model model) {
        try {
            String token = apiAuthService.login(username, password);

            if (token != null && !token.isEmpty()) {
                session.setAttribute("token", token);
                session.setAttribute("username", username);

                UserProfileResponse profile = webClientAPI.get()
                        .uri("usuarios/me")
                        .headers(h -> h.setBearerAuth(token))
                        .retrieve()
                        .bodyToMono(UserProfileResponse.class)
                        .block();

                if (profile != null) {
                    session.setAttribute("usuarioIdLogueado", profile.id());
                    System.out.println("Login MVC OK. ID guardado en sesión: " + profile.id());
                }

                return "redirect:/";
            } else {
                model.addAttribute("error", "Credenciales incorrectas");
                return "pantallas/login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "pantallas/login";
        }
    }

    @GetMapping("/mi-perfil/editar-perfil")
    public String editarPerfil(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";

        try {

            UserProfileResponse profile = webClientAPI.get()
                    .uri("usuarios/me")
                    .headers(h -> h.setBearerAuth(token))
                    .retrieve()
                    .bodyToMono(UserProfileResponse.class)
                    .block(); 


            model.addAttribute("usuario", profile);

        } catch (Exception e) {

            return "redirect:/login";
        }

        return "pantallas/mi-perfil/editar-perfil";
    }


}