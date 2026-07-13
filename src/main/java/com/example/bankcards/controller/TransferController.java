package com.example.bankcards.controller;

import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.security.SecurityUtils;
import com.example.bankcards.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST-контроллер для переводов между собственными картами аутентифицированного пользователя.
 */
@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;
    private final SecurityUtils securityUtils;

    public TransferController(TransferService transferService, SecurityUtils securityUtils) {
        this.transferService = transferService;
        this.securityUtils = securityUtils;
    }

    /**
     * Переводит средства между двумя картами аутентифицированного пользователя.
     */
    @PostMapping
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequest request) {
        Long userId = securityUtils.getCurrentUserId();
        transferService.transfer(request, userId);
        return ResponseEntity.ok().build();
    }
}
