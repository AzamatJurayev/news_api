package dev.azamat.news_api.service;

import dev.azamat.news_api.dto.ApiResponse;
import dev.azamat.news_api.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    dev.azamat.news_api.repository.CommentRepository commentRepository;

    public ApiResponse getAll() {
        return ApiResponse.builder().message("Mana").success(true).data(commentRepository.findAll()).build();
    }

    public ApiResponse save(Comment comment) {
        Comment save = commentRepository.save(comment);
        if (save != null) {
            return ApiResponse.builder().data(save).message("Saved!").success(true).build();
        } else {
            return ApiResponse.builder().data(save).message("Error!").success(false).build();
        }
    }

    public ApiResponse getOne(Long id) {
        Optional<Comment> byId = commentRepository.findById(id);
        if (byId.isPresent()) {
            return ApiResponse.builder().data(byId.get()).success(true).message("Mana").build();
        } else {
            return ApiResponse.builder().success(false).message("Not Found").build();
        }
    }

    public ApiResponse edit(Long id,Comment comment) {
        Optional<Comment> byId = commentRepository.findById(id);
        if (byId.isPresent()){
            Comment comment1 = byId.get();
            comment1.setInfo(comment.getInfo());
            commentRepository.save(comment1);
            return ApiResponse.builder().message("Edited!").success(true).data(comment1).build();
        }
        else {
            return ApiResponse.builder().success(false).message("Not Found!").build();
        }
    }

    public ApiResponse delete(Long id) {
        Optional<Comment> byId = commentRepository.findById(id);
        if (byId.isPresent()){
            commentRepository.deleteById(id);
            return ApiResponse.builder().message("Deleted!").success(true).build();
        }
        else {
            return ApiResponse.builder().success(false).message("Not Found!").build();
        }
    }
}
