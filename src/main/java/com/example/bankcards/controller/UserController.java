package com.example.bankcards.controller;

import com.example.bankcards.dto.UserResponse;
import com.example.bankcards.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-контроллер для управления пользователями (только для администратора).
 * Позволяет просматривать, удалять пользователей и назначать роли администратора.
 */
@RestController
@RequestMapping("/api/admin/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Возвращает список всех зарегистрированных пользователей.
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Возвращает конкретного пользователя по ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserResponseById(id));
    }

    /**
     * Удаляет пользователя по ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Назначает роль ADMIN пользователю по ID.
     */
    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponse> assignAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(userService.assignAdminRole(id));
    }
}
