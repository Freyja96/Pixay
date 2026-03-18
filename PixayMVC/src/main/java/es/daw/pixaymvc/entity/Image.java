package es.daw.pixaymvc.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private byte[] content; //archivo binario de UNA foto, TODO hacerlo LAZY para que no cargue todas a la vez

    @ManyToOne
    @JoinColumn(name = "user_id") //el nombre de la columna en la tabla de la BD
    private User userId; //a quién pertenece

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public User getUserId() {
        return userId;
    }
}
