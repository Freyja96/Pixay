package es.daw.foodexpressapi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "order_details")
@Data
@IdClass(OrderDetailId.class) // clave compuesta
public class OrderDetail {

    @Id
    @Column(name = "order_id")
    private Long orderId;

    @Id
    @Column(name = "dish_id")
    private Long dishId;

    private Integer quantity;

    private Double price;

    @ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "dish_id", insertable = false, updatable = false)
    private Dish dish;
}