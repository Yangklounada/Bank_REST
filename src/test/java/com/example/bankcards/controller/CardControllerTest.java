package com.example.bankcards.controller;

import com.example.bankcards.dto.CardRequest;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.security.JwtUtils;
import com.example.bankcards.security.SecurityUtils;
import com.example.bankcards.security.UserDetailsServiceImpl;
import com.example.bankcards.service.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CardController.class)
@Import(SpringDataWebAutoConfiguration.class)
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CardService cardService;

    @MockBean
    private SecurityUtils securityUtils;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void createCard_returns201() throws Exception {
        CardRequest request = new CardRequest();
        request.setCardNumber("1234567890123456");
        request.setCardHolder("Test Holder");
        request.setExpiryDate("2028-12-31");
        request.setInitialBalance(BigDecimal.valueOf(1000));

        CardResponse response = new CardResponse(
                1L,
                "**** **** **** 3456",
                "Test Holder",
                LocalDate.of(2028, 12, 31),
                "ACTIVE",
                BigDecimal.valueOf(1000),
                1L
        );

        when(securityUtils.getCurrentUserId()).thenReturn(1L);
        when(cardService.createCard(any(CardRequest.class), eq(1L)))
                .thenReturn(response);

        mockMvc.perform(post("/api/cards")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.maskedNumber").value("**** **** **** 3456"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void getCardById_returns200() throws Exception {
        CardResponse response = new CardResponse(
                1L,
                "**** **** **** 3456",
                "Test Holder",
                LocalDate.of(2028, 12, 31),
                "ACTIVE",
                BigDecimal.valueOf(1000),
                1L
        );

        when(securityUtils.getCurrentUserId()).thenReturn(1L);
        when(cardService.getCardById(1L, 1L)).thenReturn(response);

        mockMvc.perform(get("/api/cards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void blockCard_returns200() throws Exception {
        CardResponse response = new CardResponse(
                1L,
                "**** **** **** 3456",
                "Test Holder",
                LocalDate.of(2028, 12, 31),
                "BLOCKED",
                BigDecimal.valueOf(1000),
                1L
        );

        when(securityUtils.getCurrentUserId()).thenReturn(1L);
        when(cardService.blockCard(1L, 1L)).thenReturn(response);

        mockMvc.perform(patch("/api/cards/1/block")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("BLOCKED"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void deleteCard_returns204() throws Exception {
        when(securityUtils.getCurrentUserId()).thenReturn(1L);

        mockMvc.perform(delete("/api/cards/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
