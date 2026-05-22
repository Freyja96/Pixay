package es.daw.pixaymvc.controlador;

import es.daw.pixaymvc.dto.response.UserProfileResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
public class PageController {

    private final WebClient webClientAPI;

    public PageController(WebClient webClientAPI) {
        this.webClientAPI = webClientAPI;
    }

    // Para mostrar la pagina de ajustes
    @GetMapping("/ajustes")
    public String ajustes(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";

        return "pantallas/ajustes/ajustes";
    }

}
