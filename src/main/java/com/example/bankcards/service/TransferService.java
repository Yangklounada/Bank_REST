package com.example.bankcards.service;

import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.exception.TransferException;
import com.example.bankcards.repository.CardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервисный слой для переводов между картами одного пользователя.
 * Проверяет статус карт, баланс и владение перед выполнением перевода.
 */
@Service
@Transactional(readOnly = true)
public class TransferService {
    private final CardRepository cardRepository;

    public TransferService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    /**
     * Выполняет перевод между двумя картами одного пользователя.
     *
     * @throws ResourceNotFoundException если карта не найдена или не принадлежит пользователю
     * @throws TransferException         если валидация не пройдена (та же карта, неактивна, недостаточно средств)
     */
    @Transactional
    public void transfer(TransferRequest request, Long userId) {
        Card fromCard = cardRepository.findByIdAndOwnerId(request.getFromCardId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Source card not found"));
        Card toCard = cardRepository.findByIdAndOwnerId(request.getToCardId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Destination card not found"));

        if (fromCard.getId().equals(toCard.getId())) {
            throw new TransferException("Cannot transfer to the same card");
        }
        if (fromCard.getStatus() != CardStatus.ACTIVE) {
            throw new TransferException("Source card is not active");
        }
        if (toCard.getStatus() != CardStatus.ACTIVE) {
            throw new TransferException("Destination card is not active");
        }
        if (fromCard.getBalance().compareTo(request.getAmount()) < 0) {
            throw new TransferException("Insufficient balance");
        }

        fromCard.setBalance(fromCard.getBalance().subtract(request.getAmount()));
        toCard.setBalance(toCard.getBalance().add(request.getAmount()));

        cardRepository.save(fromCard);
        cardRepository.save(toCard);
    }
}
