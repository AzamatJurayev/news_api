package dev.azamat.news_api.controller;

import dev.azamat.news_api.dto.ApiResponse;
import dev.azamat.news_api.dto.CommentDto;
import dev.azamat.news_api.entity.Comment;
import dev.azamat.news_api.entity.Role;
import dev.azamat.news_api.repository.CommentRepository;
import dev.azamat.news_api.security.JwtProvider;
import dev.azamat.news_api.service.CommentService;
import dev.azamat.news_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final AuthService authService;
    private final CommentRepository commentRepository;
    private final JwtProvider jwtProvider;

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PostMapping
    public ResponseEntity<?> save(HttpServletRequest request, @RequestBody CommentDto commentDto) {
        String token = jwtProvider.getToken(request);
        ApiResponse save = commentService.save(commentDto, token);
        return ResponseEntity.status(save.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(save);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @GetMapping
    public ResponseEntity<?> getAll() {
        ApiResponse response = commentService.getAll();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable String info) {
        ApiResponse response = commentService.getOne(info);
        return ResponseEntity.ok("Not Found!");
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(HttpServletRequest request, @RequestBody CommentDto commentDto, @PathVariable Long id) {
        String token = jwtProvider.getToken(request);
        ApiResponse response = commentService.updateOneFromMe(token, id, commentDto);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request) {
        String token = jwtProvider.getToken(request);
        UserDetails userDetails = authService.loadUserByUsername(token);
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            if (authority.getAuthority().equalsIgnoreCase("User")) {
                Optional<Comment> byOwner_phoneAndId = commentRepository.findByOwner_PhoneAndId(token, id);
                if (byOwner_phoneAndId.isPresent()) {
                    ApiResponse response = commentService.delete(id);
                    return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
                }else return ResponseEntity.ok("Not Found");
            } else {
                ApiResponse response = commentService.delete(id);
                return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
            }
        }
        return ResponseEntity.ok("Not Found");
    }
}
