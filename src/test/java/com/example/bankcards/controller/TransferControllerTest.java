package com.example.bankcards.controller;

import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.security.JwtUtils;
import com.example.bankcards.security.SecurityUtils;
import com.example.bankcards.security.UserDetailsServiceImpl;
import com.example.bankcards.service.TransferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransferController.class)
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransferService transferService;

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
    void transfer_returns200() throws Exception {
        TransferRequest request = new TransferRequest();
        request.setFromCardId(1L);
        request.setToCardId(2L);
        request.setAmount(BigDecimal.valueOf(100));

        when(securityUtils.getCurrentUserId()).thenReturn(1L);

        mockMvc.perform(post("/api/transfers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void transfer_invalidAmount_returns400() throws Exception {
        TransferRequest request = new TransferRequest();
        request.setFromCardId(1L);
        request.setToCardId(2L);
        request.setAmount(BigDecimal.valueOf(-1));

        mockMvc.perform(post("/api/transfers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void transfer_withoutAuth_returns401() throws Exception {
        TransferRequest request = new TransferRequest();
        request.setFromCardId(1L);
        request.setToCardId(2L);
        request.setAmount(BigDecimal.valueOf(100));

        mockMvc.perform(post("/api/transfers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
