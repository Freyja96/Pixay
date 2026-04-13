package es.daw.pixaymvc.controlador;

import es.daw.pixaymvc.service.ApiAuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
//@RequestMapping("/perfil")//WTF por qué iba a ser de perfil?
public class UserController {
    private final ApiAuthService apiAuthService;

    public UserController(ApiAuthService apiAuthService) {
        this.apiAuthService = apiAuthService;
    }
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login"; // Carga login.html
    }
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String username,
                                @RequestParam String password,
                                HttpSession session,
                                Model model) {
        try {
            String token = apiAuthService.login(username, password);
            if (!token.isEmpty() && token != null) {
                session.setAttribute("token", token);
                session.setAttribute("username", username);
                System.out.println("Login MVC OK. Token: " + token + " Username: " + username + " guardados");
                return "redirect:/";
            } else {
                model.addAttribute("error", "Credenciales incorrectas");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "login";
        }
    }
//    @GetMapping("/{username}")
//    public String verPerfil(@PathVariable String username, Model model) {
//        // llama a la API con WebClient para pedir los datos de 'username'
//        // model.addAttribute("usuario", datosDesdeAPI);
//        return "perfil"; // Devuelve perfil.html
//    }
//TODO tiene que redirigir? no tiene simplemente que mostrar el perfil?
//    @GetMapping("/perfil/{username}/mis-imagenes"")
//    public String verMisImagenes(@PathVariable String username, Model model) {
//        // Llama a la API: /api/imagenes/mis-imagenes
//        // model.addAttribute("imagenes", listaDesdeAPI);
//        return "pantallas/mi-perfil/mis-imagenes";
//    }
}