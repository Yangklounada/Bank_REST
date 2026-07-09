package com.example.bankcards.service;

import com.example.bankcards.dto.CardRequest;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.util.CardMaskUtil;
import com.example.bankcards.util.EncryptionUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final EncryptionUtil encryptionUtil;
    private final UserService userService;

    public CardService(CardRepository cardRepository,
                       EncryptionUtil encryptionUtil,
                       UserService userService) {
        this.cardRepository = cardRepository;
        this.encryptionUtil = encryptionUtil;
        this.userService = userService;
    }

    @Transactional
    public CardResponse createCard(CardRequest request, Long ownerId) {
        User owner = userService.getUserById(ownerId);

        Card card = new Card();
        card.setEncrypted(encryptionUtil.encrypt(request.getCardNumber()));
        card.setCardHolder(request.getCardHolder());
        card.setExpiryDate(LocalDate.parse(request.getExpiryDate()));
        card.setStatus(CardStatus.ACTIVE);
        card.setBalance(request.getInitialBalance() != null
                ? request.getInitialBalance()
                : java.math.BigDecimal.ZERO);
        card.setOwner(owner);

        Card saved = cardRepository.save(card);
        return toCardResponse(saved);
    }

    public CardResponse getCardById(Long cardId, Long userId) {
        Card card = cardRepository.findByIdAndOwnerId(cardId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));
        return toCardResponse(card);
    }

    public Page<CardResponse> getUserCards(Long userId, Pageable pageable) {
        return cardRepository.findByOwnerId(userId, pageable)
                .map(this::toCardResponse);
    }

    @Transactional
    public CardResponse blockCard(Long cardId, Long userId) {
        Card card = cardRepository.findByIdAndOwnerId(cardId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));
        card.setStatus(CardStatus.BLOCKED);
        Card saved = cardRepository.save(card);
        return toCardResponse(saved);
    }

    @Transactional
    public void deleteCard(Long cardId, Long userId) {
        Card card = cardRepository.findByIdAndOwnerId(cardId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));
        cardRepository.delete(card);
    }

    private CardResponse toCardResponse(Card card) {
        return new CardResponse(
                card.getId(),
                CardMaskUtil.mask(encryptionUtil.decrypt(card.getEncrypted())),
                card.getCardHolder(),
                card.getExpiryDate(),
                card.getStatus().name(),
                card.getBalance(),
                card.getOwner().getId()
        );
    }
}