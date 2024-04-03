package com.example.microfinancepi.services;

import com.example.microfinancepi.entities.Amortization;
import com.example.microfinancepi.repositories.AmortizationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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
    public Amortization addAmortization (Amortization amrt)  {
        return amortizationRepo.save(amrt);
    }
    public Amortization modifyAmortization (Amortization amrt) {
        return amortizationRepo.save(amrt);
    }
    public void removeAmortization(Long idAmrt) { amortizationRepo.deleteById(idAmrt);    }


    public Long calculateRemainingBalance(Long idAmrt, Long periodsElapsed) {
        Amortization amortization = amortizationRepo.findById(idAmrt)
                .orElseThrow(() -> new EntityNotFoundException("Amortization not found for ID: " + idAmrt));

        // Assuming `startAmount` is the original loan amount
        Long originalAmount = amortization.getStartAmount();
        Long interest = amortization.getIntrest();
        Long annuity = amortization.getAnnuity();
        Long remainingBalance = originalAmount;

        // Simplified calculation for demonstration; actual formula might be different based on amortization type
        for (long i = 0; i < periodsElapsed; i++) {
            Long interestForPeriod = (remainingBalance * interest) / 100;
            Long principalForPeriod = annuity - interestForPeriod;
            remainingBalance -= principalForPeriod;
        }

        return remainingBalance;
    }


    /*
    methode wassim:
    @Override
    public void pret(Integer userId, Float amount,Integer investmentPeriodInMonths) {
        Transaction transaction = transactionService.withdraw(userId, amount, 1, Type_transaction.PRET);

        Pret pret = new Pret();


        User user = userRepository.findById(userId).orElse(null);
        pret.setId_user(userId);
        pret.setAmount(amount);
        pret.setDate_inv(Calendar.getInstance().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Calendar.getInstance().getTime());
        calendar.add(Calendar.MONTH, investmentPeriodInMonths);
        Date endDate = calendar.getTime();
        pret.setDatefin(endDate);
        pret.setInvestmentPeriodInMonths(investmentPeriodInMonths);

        Float rate = investmentPeriodInMonths == 6 ? 0.020f : 0.05f;
        pret.setInterest(rate * amount);
        pretRepository.save(pret);
    }
@Override
          @Transactional
    @Scheduled(fixedDelay = 86400000)
    public void checkPret() {
    List<Pret> prets = pretRepository.findByDatefinBefore(new Date());
    for (Pret pret : prets) {
    transactionService.withdraw(1,pret.getAmount(), pret.getId_user(),Type_transaction.RETURN);
    transactionService.withdraw(1,pret.getInterest(),pret.getId_user(),Type_transaction.INTEREST);
    }
    }
     */

}
