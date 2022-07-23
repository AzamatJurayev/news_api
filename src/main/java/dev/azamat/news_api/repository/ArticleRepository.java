package dev.azamat.news_api.repository;


import dev.azamat.news_api.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Article findByOwner_PhoneAndId(String phone,Long id);
    List<Article> findAllByOwner_Phone(String phone);
}
