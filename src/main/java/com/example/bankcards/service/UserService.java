package com.example.bankcards.service;

import com.example.bankcards.dto.RegisterRequest;
import com.example.bankcards.dto.UserResponse;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.DuplicateResourceException;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Сервисный слой для управления пользователями: регистрация, получение,
 * удаление и назначение роли администратора.
 */
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    /**
     * Регистрирует нового пользователя с ролью {@link Role#ROLE_USER} и возвращает DTO.
     *
     * @throws DuplicateResourceException если имя пользователя или email уже заняты
     */
    @Transactional
    public UserResponse registerUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already taken");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setEnabled(true);

        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);
        user.setRoles(roles);

        return userMapper.toResponse(userRepository.save(user));
    }

    /**
     * Возвращает сущность User по ID. Только для внутреннего использования (например, CardService).
     *
     * @throws ResourceNotFoundException если пользователь не существует
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    /**
     * Возвращает DTO пользователя по ID.
     */
    public UserResponse getUserResponseById(Long id) {
        return userMapper.toResponse(getUserById(id));
    }

    /**
     * Возвращает список всех зарегистрированных пользователей в виде DTO.
     */
    public List<UserResponse> getAllUsers() {
        return userMapper.toResponseList(userRepository.findAll());
    }

    /**
     * Удаляет пользователя по ID.
     */
    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    /**
     * Назначает роль ADMIN пользователю по ID.
     */
    @Transactional
    public UserResponse assignAdminRole(Long id) {
        User user = getUserById(id);
        user.getRoles().add(Role.ROLE_ADMIN);
        return userMapper.toResponse(userRepository.save(user));
    }
}
