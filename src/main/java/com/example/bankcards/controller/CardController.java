package com.example.bankcards.controller;

import com.example.bankcards.dto.CardRequest;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.CardService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;
    private final UserRepository userRepository;

    public CardController(CardService cardService, UserRepository userRepository) {
        this.cardService = cardService;
        this.userRepository = userRepository;
    }
    @PostMapping
    public ResponseEntity<CardResponse> createCard(@Valid @RequestBody CardRequest request, Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.createCard(request, userId));
    }
    @GetMapping
    public ResponseEntity<Page<CardResponse>> getUserCards(Authentication authentication,
                                                           Pageable pageable) {
        Long userId = getCurrentUserId(authentication);
        return ResponseEntity.ok(cardService.getUserCards(userId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardResponse> getCardById(@PathVariable Long id,
                                                    Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return ResponseEntity.ok(cardService.getCardById(id, userId));
    }

    @PatchMapping("/{id}/block")
    public ResponseEntity<CardResponse> blockCard(@PathVariable Long id,
                                                  Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return ResponseEntity.ok(cardService.blockCard(id, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id,
                                           Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        cardService.deleteCard(id, userId);
        return ResponseEntity.noContent().build();
    }

    public Long getCurrentUserId(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found")).getId();
    }
}
