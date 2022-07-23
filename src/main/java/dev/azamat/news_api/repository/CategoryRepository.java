package dev.azamat.news_api.repository;


import dev.azamat.news_api.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {



    Optional<Category> findByName(String category);
}
