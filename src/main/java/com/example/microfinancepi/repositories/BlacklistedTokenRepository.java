package com.example.microfinancepi.repositories;

import com.example.microfinancepi.entities.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken,Integer> {
    Optional<BlacklistedToken> findByToken(String token);
    void deleteAllByRemovingTimeBefore(Date removingTime);

}
