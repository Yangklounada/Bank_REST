package com.example.bankcards.controller;

import com.example.bankcards.dto.UserResponse;
import com.example.bankcards.entity.Role;
import com.example.bankcards.security.JwtUtils;
import com.example.bankcards.security.UserDetailsServiceImpl;
import com.example.bankcards.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getAllUsers_returns200() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(
                new UserResponse(1L, "testuser", "test@mail.ru", true, Set.of(Role.ROLE_USER))));

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[0].password").doesNotExist());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getUserById_returns200() throws Exception {
        when(userService.getUserResponseById(1L)).thenReturn(
                new UserResponse(1L, "testuser", "test@mail.ru", true, Set.of(Role.ROLE_USER)));

        mockMvc.perform(get("/api/admin/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteUser_returns204() throws Exception {
        mockMvc.perform(delete("/api/admin/users/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void assignAdmin_returns200() throws Exception {
        when(userService.assignAdminRole(1L)).thenReturn(
                new UserResponse(1L, "testuser", "test@mail.ru", true,
                        Set.of(Role.ROLE_USER, Role.ROLE_ADMIN)));

        mockMvc.perform(patch("/api/admin/users/1/role")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roles").isArray());
    }
}
