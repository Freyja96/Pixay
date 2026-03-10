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


    @Value("${api.auth-username}")
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
        //request.setUsername(apiUsername);
        //request.setPassword(apiPassword);

        try {
            ApiLoginResponse response = webClientAuth
                    .post()
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ApiLoginResponse.class)
                    .block();//síncrono

//            if (response == null || response.getToken() == null){
//                throw new ConnectApiRestException("API login failed: no token received");
//            }


            //apiSessionToken.setApiToken(response.getToken());

            return "";//response.getToken();
        } catch (Exception ex){
            throw new ConnectApiRestException("Couldn't authenticate against FoodExpress API: " + ex.getMessage() + "");
        }
    }
}
