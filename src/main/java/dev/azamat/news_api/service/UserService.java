package dev.azamat.news_api.service;

import dev.azamat.news_api.dto.ApiResponse;
import dev.azamat.news_api.dto.RegisterDto;
import dev.azamat.news_api.dto.UserDto;
import dev.azamat.news_api.entity.Role;
import dev.azamat.news_api.entity.User;
import dev.azamat.news_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    public ApiResponse register(RegisterDto registerDto) {
        User user = new User();
        user.setPhone(registerDto.getPhone());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        if (registerDto.getRoleCode().equalsIgnoreCase("admin")){
            user.setRole(Role.ADMIN);
        } else if (registerDto.getRoleCode().equalsIgnoreCase("moderator")) {
            user.setRole(Role.MODERATOR);
        }else user.setRole(Role.USER);
        user.setAccountNonExpired(true);
        user.setCredentialNonExpired(true);
        user.setAccountNonLocked(true);
        user.setEnabled(true);
        userRepository.save(user);
        return ApiResponse.builder().data(user).message("register amalga oshirildi").success(true).build();
    }
}
