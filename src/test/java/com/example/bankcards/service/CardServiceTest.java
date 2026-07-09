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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private EncryptionUtil encryptionUtil;

    @Mock
    private UserService userService;

    @InjectMocks
    private CardService cardService;

    private User createUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setUsername("testuser");
        return user;
    }

    private Card createCard(Long id, User owner) {
        Card card = new Card();
        card.setId(id);
        card.setEncrypted("encrypted123");
        card.setCardHolder("Test Holder");
        card.setExpiryDate(LocalDate.of(2028, 12, 31));
        card.setStatus(CardStatus.ACTIVE);
        card.setBalance(BigDecimal.valueOf(1000));
        card.setOwner(owner);
        return card;
    }

    @Test
    void createCard_success() {
        User owner = createUser(1L);
        CardRequest request = new CardRequest();
        request.setCardNumber("1234567890123456");
        request.setCardHolder("Test Holder");
        request.setExpiryDate("2028-12-31");
        request.setInitialBalance(BigDecimal.valueOf(500));

        when(userService.getUserById(1L)).thenReturn(owner);
        when(encryptionUtil.encrypt("1234567890123456")).thenReturn("encrypted123");
        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> {
            Card saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        CardResponse result = cardService.createCard(request, 1L);

        assertNotNull(result);
        assertEquals("Test Holder", result.getCardHolder());
        assertEquals("ACTIVE", result.getStatus());
        assertEquals(BigDecimal.valueOf(500), result.getBalance());
        assertEquals(1L, result.getOwnerId());

        verify(userService).getUserById(1L);
        verify(encryptionUtil).encrypt("1234567890123456");
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    void getCardById_success() {
        User owner = createUser(1L);
        Card card = createCard(1L, owner);

        when(cardRepository.findByIdAndOwnerId(1L, 1L)).thenReturn(Optional.of(card));
        when(encryptionUtil.decrypt("encrypted123")).thenReturn("1234567890123456");

        CardResponse result = cardService.getCardById(1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Holder", result.getCardHolder());
        assertTrue(result.getMaskedNumber().endsWith("3456"));
    }

    @Test
    void getCardById_notFound_throwsException() {
        when(cardRepository.findByIdAndOwnerId(99L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> cardService.getCardById(99L, 1L));
    }

    @Test
    void getUserCards_returnsPage() {
        User owner = createUser(1L);
        Card card = createCard(1L, owner);
        PageRequest pageable = PageRequest.of(0, 10);

        when(cardRepository.findByOwnerId(1L, pageable))
                .thenReturn(new PageImpl<>(List.of(card)));
        when(encryptionUtil.decrypt("encrypted123")).thenReturn("1234567890123456");

        Page<CardResponse> result = cardService.getUserCards(1L, pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("Test Holder", result.getContent().get(0).getCardHolder());
    }

    @Test
    void blockCard_success() {
        User owner = createUser(1L);
        Card card = createCard(1L, owner);

        when(cardRepository.findByIdAndOwnerId(1L, 1L)).thenReturn(Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(encryptionUtil.decrypt("encrypted123")).thenReturn("1234567890123456");

        CardResponse result = cardService.blockCard(1L, 1L);

        assertEquals("BLOCKED", result.getStatus());
        verify(cardRepository).save(card);
    }

    @Test
    void deleteCard_success() {
        User owner = createUser(1L);
        Card card = createCard(1L, owner);

        when(cardRepository.findByIdAndOwnerId(1L, 1L)).thenReturn(Optional.of(card));

        cardService.deleteCard(1L, 1L);

        verify(cardRepository).delete(card);
    }

    @Test
    void deleteCard_notFound_throwsException() {
        when(cardRepository.findByIdAndOwnerId(99L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> cardService.deleteCard(99L, 1L));
    }
}