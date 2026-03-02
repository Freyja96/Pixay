package es.daw.foodexpressapi.dto;

import lombok.Data;

@Data
//@Builder
public class RestaurantDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
}
