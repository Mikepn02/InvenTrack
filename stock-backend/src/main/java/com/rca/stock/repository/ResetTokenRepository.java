package com.rca.stock.repository;

import com.rca.stock.model.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetTokenRepository extends JpaRepository<ResetToken , Integer> {
    Optional<ResetToken> findByToken(String token);
}
