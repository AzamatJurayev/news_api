package dev.azamat.news_api.controller;

import dev.azamat.news_api.dto.ApiResponse;
import dev.azamat.news_api.dto.LoginDto;
import dev.azamat.news_api.dto.RegisterDto;
import dev.azamat.news_api.security.JwtFilter;
import dev.azamat.news_api.security.JwtProvider;
import dev.azamat.news_api.service.AuthService;
import dev.azamat.news_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class Auth {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDTO) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

        //jwt yasab berishimz kk
        String token = jwtProvider.generateToken(loginDTO.getUsername());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto){
        ApiResponse response = userService.register(registerDto);
        String token = jwtProvider.generateToken(registerDto.getPhone());
        return ResponseEntity.ok(token);
    }
}
