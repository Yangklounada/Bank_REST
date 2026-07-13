package com.example.bankcards.dto;

import com.example.bankcards.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * DTO ответа для данных пользователя. Исключает поле password.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private boolean enabled;
    private Set<Role> roles;
}
