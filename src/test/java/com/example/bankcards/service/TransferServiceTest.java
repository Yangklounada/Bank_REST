package com.example.bankcards.service;

import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.exception.TransferException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransferService transferService;

    private User createUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setUsername("testuser");
        return user;
    }

    private Card createCard(Long id, User owner, CardStatus status, BigDecimal balance) {
        Card card = new Card();
        card.setId(id);
        card.setEncrypted("encrypted");
        card.setCardHolder("Holder");
        card.setExpiryDate(LocalDate.of(2028, 12, 31));
        card.setStatus(status);
        card.setBalance(balance);
        card.setOwner(owner);
        return card;
    }

    @Test
    void transfer_success() {
        User owner = createUser(1L);
        Card fromCard = createCard(1L, owner, CardStatus.ACTIVE, BigDecimal.valueOf(500));
        Card toCard = createCard(2L, owner, CardStatus.ACTIVE, BigDecimal.valueOf(100));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(owner));
        when(cardRepository.findByIdAndOwnerId(1L, 1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findByIdAndOwnerId(2L, 1L)).thenReturn(Optional.of(toCard));

        TransferRequest request = new TransferRequest();
        request.setFromCardId(1L);
        request.setToCardId(2L);
        request.setAmount(BigDecimal.valueOf(200));

        transferService.transfer(request, "testuser");

        assertEquals(BigDecimal.valueOf(300), fromCard.getBalance());
        assertEquals(BigDecimal.valueOf(300), toCard.getBalance());
        verify(cardRepository, times(2)).save(any(Card.class));
    }

    @Test
    void transfer_sourceCardNotFound_throwsException() {
        User owner = createUser(1L);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(owner));
        when(cardRepository.findByIdAndOwnerId(1L, 1L)).thenReturn(Optional.empty());

        TransferRequest request = new TransferRequest();
        request.setFromCardId(1L);
        request.setToCardId(2L);
        request.setAmount(BigDecimal.valueOf(100));

        assertThrows(ResourceNotFoundException.class,
                () -> transferService.transfer(request, "testuser"));
    }

    @Test
    void transfer_insufficientBalance_throwsException() {
        User owner = createUser(1L);
        Card fromCard = createCard(1L, owner, CardStatus.ACTIVE, BigDecimal.valueOf(50));
        Card toCard = createCard(2L, owner, CardStatus.ACTIVE, BigDecimal.valueOf(100));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(owner));
        when(cardRepository.findByIdAndOwnerId(1L, 1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findByIdAndOwnerId(2L, 1L)).thenReturn(Optional.of(toCard));

        TransferRequest request = new TransferRequest();
        request.setFromCardId(1L);
        request.setToCardId(2L);
        request.setAmount(BigDecimal.valueOf(100));

        assertThrows(TransferException.class,
                () -> transferService.transfer(request, "testuser"));
    }

    @Test
    void transfer_sourceCardBlocked_throwsException() {
        User owner = createUser(1L);
        Card fromCard = createCard(1L, owner, CardStatus.BLOCKED, BigDecimal.valueOf(500));
        Card toCard = createCard(2L, owner, CardStatus.ACTIVE, BigDecimal.valueOf(100));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(owner));
        when(cardRepository.findByIdAndOwnerId(1L, 1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findByIdAndOwnerId(2L, 1L)).thenReturn(Optional.of(toCard));

        TransferRequest request = new TransferRequest();
        request.setFromCardId(1L);
        request.setToCardId(2L);
        request.setAmount(BigDecimal.valueOf(100));

        assertThrows(TransferException.class,
                () -> transferService.transfer(request, "testuser"));
    }

    @Test
    void transfer_sameCard_throwsException() {
        User owner = createUser(1L);
        Card card = createCard(1L, owner, CardStatus.ACTIVE, BigDecimal.valueOf(500));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(owner));
        when(cardRepository.findByIdAndOwnerId(1L, 1L)).thenReturn(Optional.of(card));

        TransferRequest request = new TransferRequest();
        request.setFromCardId(1L);
        request.setToCardId(1L);
        request.setAmount(BigDecimal.valueOf(100));

        assertThrows(TransferException.class,
                () -> transferService.transfer(request, "testuser"));
    }
}