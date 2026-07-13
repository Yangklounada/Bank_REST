package com.example.bankcards.mapper;

import com.example.bankcards.dto.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.util.CardMaskUtil;
import com.example.bankcards.util.EncryptionUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * Преобразует сущности {@link Card} в DTO {@link CardResponse}.
 * Расшифровывает номер карты и маскирует его перед возвратом.
 */
@Component
public class CardMapper {

    private final EncryptionUtil encryptionUtil;

    public CardMapper(EncryptionUtil encryptionUtil) {
        this.encryptionUtil = encryptionUtil;
    }

    /**
     * Преобразует сущность Card в DTO CardResponse с маскированным номером карты.
     */
    public CardResponse toResponse(Card card) {
        String decrypted = encryptionUtil.decrypt(card.getEncrypted());
        String masked = CardMaskUtil.mask(decrypted);
        return new CardResponse(
                card.getId(),
                masked,
                card.getCardHolder(),
                card.getExpiryDate(),
                card.getStatus().name(),
                card.getBalance(),
                card.getOwner().getId()
        );
    }

    /**
     * Преобразует страницу сущностей Card в страницу DTO CardResponse.
     */
    public Page<CardResponse> toResponsePage(Page<Card> cards) {
        return cards.map(this::toResponse);
    }
}
