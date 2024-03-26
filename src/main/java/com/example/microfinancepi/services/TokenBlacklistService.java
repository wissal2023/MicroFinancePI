package com.example.microfinancepi.services;

import com.example.microfinancepi.entities.BlacklistedToken;
import com.example.microfinancepi.repositories.BlacklistedTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;

import static com.example.microfinancepi.security.SecurityConstants.EXPIRATION_TIME;

@Service
public class TokenBlacklistService {
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    public TokenBlacklistService(BlacklistedTokenRepository blacklistedTokenRepository) {
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }

    public void addToBlacklist(String token) {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
//        calendar.add(Calendar.HOUR_OF_DAY, 1);
        calendar.add(Calendar.MILLISECOND, (int) EXPIRATION_TIME);
        Date removingTime = calendar.getTime();
        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setRemovingTime(removingTime);
        blacklistedTokenRepository.save(blacklistedToken);
    }
    @Transactional
    @Scheduled(fixedDelay = 120000) // runs every 2 minutes
    public void removeExpiredTokensFromBlacklist() {
        Date removingTime = new Date();
        blacklistedTokenRepository.deleteAllByRemovingTimeBefore(removingTime);
    }
}
