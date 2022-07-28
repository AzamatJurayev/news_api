package dev.azamat.news_api.service;

import dev.azamat.news_api.dto.ApiResponse;
import dev.azamat.news_api.dto.CommentDto;
import dev.azamat.news_api.entity.*;
import dev.azamat.news_api.entity.Comment;
import dev.azamat.news_api.repository.*;
import dev.azamat.news_api.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ApiResponse save(CommentDto commentDto, String usernameFromToken) {
        Comment comment = new Comment();
        comment.setInfo(commentDto.getInfo());
        Optional<UserDetails> byPhone = userRepository.findByPhone(usernameFromToken);
        comment.setOwner((User) byPhone.get());
        Optional<Article> article = articleRepository.findById(commentDto.getArticleId());
        if (article.isPresent()){
            comment.setArticle(article.get());
        }
        Comment save = commentRepository.save(comment);
        return ApiResponse.builder().data(save).message("Added!").success(true).build();
    }

    public ApiResponse getAll() {
        return ApiResponse.builder().message("Mana").success(true).data(commentRepository.findAll()).build();
    }

    public ApiResponse getOne(String info) {
        Optional<Comment> byId = commentRepository.findByInfo(info);
        if (byId.isPresent()) {
            return ApiResponse.builder().data(byId.get()).success(true).message("Mana").build();
        } else {
            return ApiResponse.builder().success(false).message("Not Found").build();
        }
    }

    public ApiResponse updateOneFromMe(String token, Long id, CommentDto commentDto) {
        Optional<Comment> byOwner_phoneAndId = commentRepository.findByOwner_PhoneAndId(token, id);
        Comment comment = byOwner_phoneAndId.get();
        if (byOwner_phoneAndId.isPresent()) {
            comment.setInfo(commentDto.getInfo());
            Comment save = commentRepository.save(comment);
            return ApiResponse.builder().data(save).message("Saved!").success(true).build();
        }
        return ApiResponse.builder().success(false).message("Not Found!").build();
    }

    public ApiResponse delete(Long id) {
        commentRepository.deleteById(id);
        return ApiResponse.builder().message("Deleted!").success(true).build();
    }
}
