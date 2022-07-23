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
    public ResponseEntity<?> save(@RequestBody ArticleDto articleDto){
        ApiResponse save = articleService.save(articleDto);
        return ResponseEntity.status(save.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(save);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @GetMapping
    public ResponseEntity<?> getAll(HttpServletRequest request){
        String token = jwtProvider.getToken(request);
        String usernameFromToken = jwtProvider.getUsernameFromToken(token);
        UserDetails userDetails = authService.loadUserByUsername(usernameFromToken);
        if (userDetails.getAuthorities().contains(Role.USER)) {
            ApiResponse response = articleService.getAllFromMe(usernameFromToken);
            return ResponseEntity.ok(response);
        }else {
            ApiResponse response = articleService.getAll();
            return ResponseEntity.ok(response);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(HttpServletRequest request,@PathVariable Long id){
        String token = jwtProvider.getToken(request);
        String usernameFromToken = jwtProvider.getUsernameFromToken(token);
        UserDetails userDetails = authService.loadUserByUsername(usernameFromToken);
        if (userDetails.getAuthorities().contains(Role.USER)) {
            ApiResponse response = articleService.getOneFromMe(usernameFromToken,id);
            return ResponseEntity.ok(response);
        }else {
            ApiResponse response = articleService.getOne(id);
            return ResponseEntity.ok(response);
        }
    }
}
