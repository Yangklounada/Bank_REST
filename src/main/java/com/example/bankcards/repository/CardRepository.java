package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA репозиторий для сущности {@link Card}.
 */
public interface CardRepository extends JpaRepository<Card, Long> {
    Page<Card> findByOwnerId(Long ownerId, Pageable pageable);
    Optional<Card> findByIdAndOwnerId(Long id, Long ownerId);
}