package es.daw.pixaymvc.controlador;

import es.daw.pixaymvc.dto.response.UserProfileResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // Para mostrar la pagina de ajustes
    @GetMapping("/ajustes")
    public String ajustes(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) return "redirect:/login";

        return "pantallas/ajustes/ajustes";
    }

}
