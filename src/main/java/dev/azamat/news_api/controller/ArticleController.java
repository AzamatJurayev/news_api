package dev.azamat.news_api.controller;

import dev.azamat.news_api.dto.ApiResponse;
import dev.azamat.news_api.dto.ArticleDto;
import dev.azamat.news_api.entity.Role;
import dev.azamat.news_api.security.JwtProvider;
import dev.azamat.news_api.service.ArticleService;
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


@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public ResponseEntity<?> save(HttpServletRequest request, @RequestBody ArticleDto articleDto) {
        String token = jwtProvider.getToken(request);
        ApiResponse save = articleService.save(articleDto, token);
        return ResponseEntity.status(save.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(save);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @GetMapping
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        String token = jwtProvider.getToken(request);
        UserDetails userDetails = authService.loadUserByUsername(token);
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            if (authority.getAuthority().equalsIgnoreCase("User")) {
                ApiResponse response = articleService.getAllFromMe(token);
                return ResponseEntity.ok(response);
            }
        }

        ApiResponse response = articleService.getAll();
        return ResponseEntity.ok(response);

    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(HttpServletRequest request, @PathVariable Long id) {
        String token = jwtProvider.getToken(request);
        UserDetails userDetails = authService.loadUserByUsername(token);
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            if (authority.getAuthority().equalsIgnoreCase("User")) {
                ApiResponse response = articleService.getOneFromMe(token, id);
                return ResponseEntity.ok(response);
            } else {
                ApiResponse response = articleService.getOne(id);
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.ok("Not Found!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(HttpServletRequest request, @RequestBody ArticleDto articleDto, @PathVariable Long id) {
        String token = jwtProvider.getToken(request);
        UserDetails userDetails = authService.loadUserByUsername(token);
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            if (authority.getAuthority().equalsIgnoreCase("User")) {
                ApiResponse response = articleService.updateOneFromMe(token,id,articleDto);
                return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
            }else {
                ApiResponse response = articleService.update(id,articleDto);
                return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
            }
        }
        return ResponseEntity.ok("Not Found");
    }
}
