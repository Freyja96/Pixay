package es.daw.pixayapi.repository;

import es.daw.pixayapi.entity.CommentReaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentReactionRepository extends JpaRepository<CommentReaction, Long> {
    List<CommentReaction> findAllByCommentId(Long commentId);
    Optional<CommentReaction> findByCommentIdAndUserId(Long commentId, Long userId);

}