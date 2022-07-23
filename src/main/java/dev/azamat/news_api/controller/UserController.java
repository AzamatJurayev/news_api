package dev.azamat.news_api.controller;

import dev.azamat.news_api.dto.ApiResponse;
import dev.azamat.news_api.dto.LoginDto;
import dev.azamat.news_api.dto.UserDto;
import dev.azamat.news_api.entity.Role;
import dev.azamat.news_api.entity.User;
import dev.azamat.news_api.security.JwtProvider;
import dev.azamat.news_api.service.AuthService;
import dev.azamat.news_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody LoginDto loginDto) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
//        if (authenticate.isAuthenticated())return ResponseEntity.ok("Tizimga hush kelibsiz!");
//        return ResponseEntity.badRequest().body("Nimadur hatolik bor!!!");
        String token = jwtProvider.generateToken(loginDto.getUsername());
        return ResponseEntity.ok(token);
    }
}
