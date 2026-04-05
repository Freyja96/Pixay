package es.daw.pixaymvc.dto;

import lombok.Data;

@Data
public class ApiLoginRequest {
    private String username;
    private String password;
}