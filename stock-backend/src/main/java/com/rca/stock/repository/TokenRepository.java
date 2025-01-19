package com.rca.stock.repository;

import com.rca.stock.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<VerificationToken , Integer> {
    Optional<VerificationToken> findByToken(String token);
}
