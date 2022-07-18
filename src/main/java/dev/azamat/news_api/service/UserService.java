package dev.azamat.news_api.service;

import dev.azamat.news_api.dto.ApiResponse;
import dev.azamat.news_api.dto.UserDto;
import dev.azamat.news_api.entity.Role;
import dev.azamat.news_api.entity.User;
import dev.azamat.news_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public ApiResponse save(UserDto userDto) {
        User user = new User();
        user.setPhone(userDto.getPhone());
        user.setRole(Role.valueOf(userDto.getRole()));
        User save = userRepository.save(user);
        if (save != null) {
            return ApiResponse.builder().data(save).message("Saved!").success(true).build();
        } else {
            return ApiResponse.builder().data(save).message("Error!").success(false).build();
        }
    }
}
