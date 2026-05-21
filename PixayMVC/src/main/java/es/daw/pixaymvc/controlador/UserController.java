package es.daw.pixaymvc.controlador;

import es.daw.pixaymvc.dto.UserProfileUpdateRequest;
import es.daw.pixaymvc.dto.response.UserProfileResponse; // Asegúrate de importar tu Record
import es.daw.pixaymvc.service.ApiAuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            System.out.println("DEBUG: Profile loaded, returning template");


        } catch (WebClientResponseException e) {
            System.out.println("DEBUG: Exception caught: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/login";
        }

        return "pantallas/mi-perfil/editar-perfil";
    }
    @PostMapping("/mi-perfil/editar-perfil")
    public String guardarCambios(HttpSession session,
                                 Model model,
                                 @RequestParam String username,
                                 @RequestParam String email,
                                 @RequestParam(required = false) String description,
                                 @RequestParam(required = false) String password,
                                 @RequestParam(required = false) String password2,
                                 RedirectAttributes redirectAttributes) {

        String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";

        try {
            UserProfileUpdateRequest updateRequest = new UserProfileUpdateRequest();
            updateRequest.setUsername(username);
            updateRequest.setEmail(email);
            updateRequest.setDescription(description);
            updateRequest.setPassword(password);

            if (password != null && !password.isEmpty() && password2!= null && !password2.isEmpty()) {
                if (password.equals(password2)) {
                    updateRequest.setPassword(password);
                } else {
                    redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden");
                    return "pantallas/mi-perfil/editar-perfil";
                }

                webClientAPI.patch()
                        .uri("usuarios/me")
                        .headers(h -> h.setBearerAuth(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(updateRequest)
                        .retrieve()
                        .toBodilessEntity()
                        .block();

                redirectAttributes.addFlashAttribute("message", "¡Perfil actualizado!");
                System.out.println("DEBUG: Perfil actualizado");
                return "redirect:/mi-perfil/mis-imagenes";
            }

        } catch (WebClientResponseException e) {
            System.out.println("DEBUG: Exception caught: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al guardar los cambios");
            return "pantallas/mi-perfil/editar-perfil";
        }

        return "redirect:/mi-perfil/mis-imagenes";
    }


}