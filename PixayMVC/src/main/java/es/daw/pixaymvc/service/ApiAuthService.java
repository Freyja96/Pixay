package es.daw.pixaymvc.service;

import es.daw.pixaymvc.dto.ApiLoginRequest;
import es.daw.pixaymvc.dto.ApiLoginResponse;
import es.daw.pixaymvc.exception.ConnectApiRestException;
import es.daw.pixaymvc.session.ApiSessionToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class ApiAuthService {
    private final WebClient webClientAuth;

    @Value("${api.credential}")
    private String apiUsername;

    @Value("${api.auth-password}")
    private String apiPassword;
    //private final HttpSession httpSession;
    private final ApiSessionToken apiSessionToken;

    public String getToken(){
        //Component de sessionScope
        if (apiSessionToken.getApiToken() != null){
            return apiSessionToken.getApiToken();
        }
        //si es nulo hago el login
        ApiLoginRequest request = new ApiLoginRequest();
        request.setUsername(apiUsername);
        request.setPassword(apiPassword);

        try {
            ApiLoginResponse response = webClientAuth
                    .post()
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ApiLoginResponse.class)
                    .block();//síncrono

            if (response != null && response.getToken() != null) {
                apiSessionToken.setApiToken(response.getToken());
                return response.getToken();
            }
            //apiSessionToken.setApiToken(response.getToken());

            return "";

        } catch (Exception ex){
            throw new ConnectApiRestException("Couldn't authenticate against Pixay API: " + ex.getMessage() + "");
        }
    }
    public String login(String username, String password) {
        ApiLoginRequest request = new ApiLoginRequest();
        request.setUsername(username);
        request.setPassword(password);

        try {
            ApiLoginResponse response = webClientAuth
                    .post()
                    .uri("/login") // Asegúrate de que la ruta en la API sea exacta
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ApiLoginResponse.class)
                    .block();

            if (response != null && response.getToken() != null) {
                // ¡IMPORTANTE! Guardamos el token en el componente de sesión
                apiSessionToken.setApiToken(response.getToken());
                return response.getToken();
            }
            return null;
        } catch (Exception ex) {
            throw new ConnectApiRestException("Error en el login: " + ex.getMessage());
        }
    }
}
