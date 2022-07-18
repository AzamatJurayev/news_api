package dev.azamat.news_api.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDto {
//    @NotNull(message = "Nomini to'ldirish shart")
    private String phone;
//    @NotNull(message = "Nomini to'ldirish shart")
    private String role;

}
