package es.daw.foodexpressapi.entity;

import lombok.Data;
import java.io.Serializable;

@Data
public class OrderDetailId implements Serializable {
    private Long orderId;
    private Long dishId;
}