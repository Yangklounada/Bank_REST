package com.example.bankcards.service;

import com.example.bankcards.dto.CardRequest;
import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.EncryptionUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Сервисный слой для управления картами: создание, получение, блокировка и удаление.
 * Все операции привязаны к владельцу карты.
 */
@Service
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final EncryptionUtil encryptionUtil;
    private final CardMapper cardMapper;

    public CardService(CardRepository cardRepository,
                       UserRepository userRepository,
                       EncryptionUtil encryptionUtil,
                       CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.encryptionUtil = encryptionUtil;
        this.cardMapper = cardMapper;
    }

    /**
     * Создаёт новую карту, шифрует номер карты и возвращает DTO с маскированным номером.
     */
    @Transactional
    public CardResponse createCard(CardRequest request, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Card card = new Card();
        card.setEncrypted(encryptionUtil.encrypt(request.getCardNumber()));
        card.setCardHolder(request.getCardHolder());
        card.setExpiryDate(LocalDate.parse(request.getExpiryDate()));
        card.setStatus(CardStatus.ACTIVE);
        card.setBalance(request.getInitialBalance() != null
                ? request.getInitialBalance()
                : java.math.BigDecimal.ZERO);
        card.setOwner(owner);

        return cardMapper.toResponse(cardRepository.save(card));
    }

    /**
     * Возвращает карту по ID, привязанную к указанному пользователю.
     */
    public CardResponse getCardById(Long cardId, Long userId) {
        Card card = cardRepository.findByIdAndOwnerId(cardId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));
        return cardMapper.toResponse(card);
    }

    /**
     * Возвращает постраничный список карт указанного пользователя.
     */
    public Page<CardResponse> getUserCards(Long userId, Pageable pageable) {
        return cardMapper.toResponsePage(cardRepository.findByOwnerId(userId, pageable));
    }

    /**
     * Блокирует карту по ID. Выбрасывает исключение, если карта не принадлежит пользователю.
     */
    @Transactional
    public CardResponse blockCard(Long cardId, Long userId) {
        Card card = cardRepository.findByIdAndOwnerId(cardId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));
        card.setStatus(CardStatus.BLOCKED);
        return cardMapper.toResponse(cardRepository.save(card));
    }

    /**
     * Удаляет карту по ID, привязанную к пользователю.
     */
    @Transactional
    public void deleteCard(Long cardId, Long userId) {
        Card card = cardRepository.findByIdAndOwnerId(cardId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));
        cardRepository.delete(card);
    }
}
