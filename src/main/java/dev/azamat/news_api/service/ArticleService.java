package dev.azamat.news_api.service;

import dev.azamat.news_api.dto.ApiResponse;
import dev.azamat.news_api.dto.ArticleDto;
import dev.azamat.news_api.entity.Article;
import dev.azamat.news_api.entity.Category;
import dev.azamat.news_api.entity.Comment;
import dev.azamat.news_api.repository.ArticleRepository;
import dev.azamat.news_api.repository.CategoryRepository;
import dev.azamat.news_api.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final CommentRepository commentRepository;

    private final CategoryRepository categoryRepository;
    public ApiResponse save(ArticleDto articleDto) {
        Article article = new Article();
        article.setTitle(articleDto.getTitle());

        Comment comment = commentRepository.save(new Comment(articleDto.getCommentInfo()));
        article.setComment(comment);

        List<Category> categories = new ArrayList<>();
        for (String category : articleDto.getCategories()) {
            Optional<Category> byName = categoryRepository.findByName(category);
            if (byName.isPresent()){
                categories.add(byName.get());
            }else {
                Category save = categoryRepository.save(new Category(category));
                categories.add(save);
            }
        }
        article.setCategory(categories);
        Article save = articleRepository.save(article);
        if (save != null) {
            return ApiResponse.builder().data(save).message("Saved!").success(true).build();
        } else {
            return ApiResponse.builder().data(save).message("Error!").success(false).build();
        }
    }

    public ApiResponse getAll() {
        return ApiResponse.builder().message("Mana").success(true).data(articleRepository.findAll()).build();
    }

    public ApiResponse getAllFromMe(String usernameFromToken) {
        return ApiResponse.builder().message("Mana").success(true).data(articleRepository.findAllByOwner_Phone(usernameFromToken)).build();
    }

    public ApiResponse getOneFromMe(String usernameFromToken,Long id) {
        return ApiResponse.builder().message("Mana").success(true).data(articleRepository.findByOwner_PhoneAndId(usernameFromToken,id)).build();
    }

    public ApiResponse getOne(Long id) {
        Optional<Article> byId = articleRepository.findById(id);
        if (byId.isPresent()) {
            return ApiResponse.builder().data(byId.get()).success(true).message("Mana").build();
        } else {
            return ApiResponse.builder().success(false).message("Not Found").build();
        }
    }
}
