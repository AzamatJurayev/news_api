package dev.azamat.news_api.repository;


import dev.azamat.news_api.entity.Category;
import dev.azamat.news_api.entity.Comment;
import dev.azamat.news_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByInfo(String comment);

    Optional<Comment> findByOwner_PhoneAndId(String token, Long id);

}
