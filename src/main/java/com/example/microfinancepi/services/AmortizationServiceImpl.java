package com.example.microfinancepi.services;

import com.example.microfinancepi.entities.Amortization;
import com.example.microfinancepi.entities.RequestLoan;
import com.example.microfinancepi.repositories.AmortizationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor// inject repo in service
public class AmortizationServiceImpl implements  IAmortizationService{
    AmortizationRepository amortizationRepo;

    public List<Amortization> retrieveAllAmortization() { return amortizationRepo.findAll();   }
    public Amortization retrieveAmortization(Long idAmrt) {
        return amortizationRepo.findById(idAmrt).get();
    }
    public Amortization modifyAmortization (Amortization amrt) {
        return amortizationRepo.save(amrt);
    }
    public void removeAmortization(Long idAmrt) { amortizationRepo.deleteById(idAmrt);    }



    public Long calculateRemainingBalance(Long idAmrt, Long periodsElapsed) {
        Amortization amortization = amortizationRepo.findById(idAmrt).get();

        Long originalAmount = amortization.getStartAmount();
        Long interest = amortization.getIntrest();
        Long annuity = amortization.getAnnuity();
        Long remainingBalance = originalAmount;

         for (long i = 0; i < periodsElapsed; i++) {
            Long interestForPeriod = (remainingBalance * interest) / 100;
            Long principalForPeriod = annuity - interestForPeriod;
            remainingBalance -= principalForPeriod;
        }

        return remainingBalance;
    }



}
