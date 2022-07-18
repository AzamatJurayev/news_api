package dev.azamat.news_api.controller;

import dev.azamat.news_api.dto.LoginDto;
import dev.azamat.news_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class Auth {
    private final AuthService authService;

    public ResponseEntity<?> login(@RequestBody LoginDto loginDto){
        UserDetails userDetails = authService.loadUserByUsername(loginDto.getUsername());
        if (userDetails.getPassword().equals(loginDto.getPassword())){
            return ResponseEntity.ok("Xush kelibsiz");
        }
        return ResponseEntity.badRequest().body("Xatolik");
    }
}
