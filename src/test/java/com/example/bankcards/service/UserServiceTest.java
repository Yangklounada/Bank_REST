package com.example.bankcards.service;

import com.example.bankcards.dto.RegisterRequest;
import com.example.bankcards.dto.UserResponse;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.DuplicateResourceException;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_success() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("new@mail.ru");
        request.setPassword("password123");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("newuser");
        savedUser.setEmail("new@mail.ru");
        savedUser.setPassword("encodedPass");
        savedUser.setEnabled(true);
        savedUser.setRoles(Set.of(Role.ROLE_USER));

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@mail.ru")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toResponse(savedUser)).thenReturn(new UserResponse(
                1L, "newuser", "new@mail.ru", true, Set.of(Role.ROLE_USER)));

        UserResponse result = userService.registerUser(request);

        assertNotNull(result);
        assertEquals("newuser", result.getUsername());
        assertEquals("new@mail.ru", result.getEmail());
        assertTrue(result.getRoles().contains(Role.ROLE_USER));
        assertTrue(result.isEnabled());

        verify(userRepository).existsByUsername("newuser");
        verify(userRepository).existsByEmail("new@mail.ru");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_duplicateUsername_throwsException() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existing");
        request.setEmail("new@mail.ru");
        request.setPassword("password123");

        when(userRepository.existsByUsername("existing")).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> userService.registerUser(request));

        verify(userRepository).existsByUsername("existing");
        verify(userRepository, never()).existsByEmail(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerUser_duplicateEmail_throwsException() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("existing@mail.ru");
        request.setPassword("password123");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("existing@mail.ru")).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> userService.registerUser(request));

        verify(userRepository).existsByUsername("newuser");
        verify(userRepository).existsByEmail("existing@mail.ru");
        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserById_success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void getUserById_notFound_throwsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(99L));
    }

    @Test
    void getAllUsers_returnsList() {
        when(userRepository.findAll()).thenReturn(List.of());
        when(userMapper.toResponseList(any())).thenReturn(List.of());

        List<UserResponse> result = userService.getAllUsers();

        verify(userRepository).findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void deleteUser_success() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository).delete(user);
    }

    @Test
    void assignAdminRole_success() {
        User user = new User();
        user.setId(1L);
        user.setRoles(new HashSet<>(Set.of(Role.ROLE_USER)));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userMapper.toResponse(any())).thenReturn(new UserResponse(
                1L, "testuser", "test@mail.ru", true, Set.of(Role.ROLE_USER, Role.ROLE_ADMIN)));

        UserResponse result = userService.assignAdminRole(1L);

        assertTrue(result.getRoles().contains(Role.ROLE_ADMIN));
        assertTrue(result.getRoles().contains(Role.ROLE_USER));
    }
}
