package com.example.bankcards.controller;

import com.example.bankcards.dto.CardRequest;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.security.SecurityUtils;
import com.example.bankcards.service.CardService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST-контроллер для операций с картами. Все endpoints требуют аутентификации.
 * Карты привязаны к текущему аутентифицированному пользователю.
 */
@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;
    private final SecurityUtils securityUtils;

    public CardController(CardService cardService, SecurityUtils securityUtils) {
        this.cardService = cardService;
        this.securityUtils = securityUtils;
    }

    /**
     * Создаёт новую банковскую карту для аутентифицированного пользователя.
     */
    @PostMapping
    public ResponseEntity<CardResponse> createCard(@Valid @RequestBody CardRequest request) {
        Long userId = securityUtils.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.createCard(request, userId));
    }

    /**
     * Возвращает постраничный список карт, принадлежащих аутентифицированному пользователю.
     */
    @GetMapping
    public ResponseEntity<Page<CardResponse>> getUserCards(Pageable pageable) {
        Long userId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(cardService.getUserCards(userId, pageable));
    }

    /**
     * Возвращает конкретную карту по ID, привязанную к аутентифицированному пользователю.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CardResponse> getCardById(@PathVariable Long id) {
        Long userId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(cardService.getCardById(id, userId));
    }

    /**
     * Блокирует карту по ID. Только владелец может заблокировать свою карту.
     */
    @PatchMapping("/{id}/block")
    public ResponseEntity<CardResponse> blockCard(@PathVariable Long id) {
        Long userId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(cardService.blockCard(id, userId));
    }

    /**
     * Удаляет карту по ID. Только владелец может удалить свою карту.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        Long userId = securityUtils.getCurrentUserId();
        cardService.deleteCard(id, userId);
        return ResponseEntity.noContent().build();
    }
}
