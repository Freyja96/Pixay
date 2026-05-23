package es.daw.pixayapi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "comment_reactions")
@Data
public class CommentReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "reaction_type", nullable = false)
    private Integer reactionType;
}