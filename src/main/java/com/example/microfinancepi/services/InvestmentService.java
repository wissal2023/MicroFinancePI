package com.example.microfinancepi.services;

import com.example.microfinancepi.entities.Investment;
import org.springframework.scheduling.annotation.Scheduled;

import javax.transaction.Transactional;
import java.util.List;

public interface InvestmentService {

    List<Investment> retrieveAllInvestissements();
    Investment AddInvestissement(Investment investment);
    void RemoveInvestissement(Integer numInvestissement);
    Investment retrieveInvestissement(Integer numInvestissement);
    Investment UpdateInvestissement(Investment investment);


    void invest(Integer userId, Float amount, Integer project_id);

    @Transactional
    @Scheduled(fixedDelay = 86400000)
    void checkinvest();

    @Transactional
    @Scheduled(fixedDelay = 86400000)
    void checkInvestStatus();

    Float calculateTotalInvestmentIncome(Integer investment_id);

    Float calculateTotalInvestmentInterest(Integer investment_id);

    Float calculateTotalInvestment(Integer investment_id);


}

