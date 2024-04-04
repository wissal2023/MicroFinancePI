package com.example.microfinancepi.services;

import com.example.microfinancepi.entities.*;
import com.example.microfinancepi.repositories.OfferLoanRepository;
import com.example.microfinancepi.repositories.RequestLoanRepository;
import com.example.microfinancepi.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor// inject repo in service
public class OfferLoanServiceImpl implements  IOfferLoanService {

    OfferLoanRepository offerLoanRepo;
    RequestLoanRepository requestLoanRepo;
    UserRepository userRepository;

    public List<OfferLoan> retrieveAllOfferLoans() {
        return offerLoanRepo.findAll();
    }

    public OfferLoan retrieveOfferLoan(Long idOffer) {
        return offerLoanRepo.findById(idOffer).get();
    }

    private User_role getCurrentUserRole(User user) {
        return user.getRole();
    }
    public OfferLoan addOfferLoan(OfferLoan offer, User user) {
        User_role userRole = getCurrentUserRole(user);

        if (!Arrays.asList(User_role.ADMIN, User_role.AGENT).contains(userRole)) {
            throw new AccessDeniedException("You're not authorized to add an offer");
        }
        offer.setUser(user);
        return offerLoanRepo.save(offer);
    }

    public OfferLoan modifyOfferLoan(OfferLoan offer, User user) {
        User_role userRole = getCurrentUserRole(user);
        if (!Arrays.asList(User_role.ADMIN, User_role.AGENT).contains(userRole)) {
            throw new AccessDeniedException("You're not authorized to add an offer");
        }
        offer.setUser(user);
        return offerLoanRepo.save(offer);
    }

    public void removeOfferLoan(Long idOffer, User user) {
        if (!user.getRole().equals(User_role.ADMIN)) {
            throw new AccessDeniedException("Only admins are authorized to delete offers.");
        }
        offerLoanRepo.deleteById(idOffer);
    }

       public float simulationCalMonthlyRepaymentAmount(Long offerId, Long loanAmnt, Long nbrMonth) {
        OfferLoan offer = offerLoanRepo.findById(offerId).get();
        LoanType loanType = offer.getTypeLoan();
        if (loanType == LoanType.STUDENT || loanType == LoanType.FARMER || loanType == LoanType.HOMEMAKER) {
            float intRate = offer.getIntRate();
            float annualIntRate = intRate / 100;
            float monthlyIntRate= annualIntRate/12;
            return (float) ((loanAmnt * monthlyIntRate) / (1 - Math.pow(1 + monthlyIntRate, -nbrMonth*12)));
        } else {
            throw new IllegalArgumentException("Constant Monthly repayment calculation is only applicable for LoanType STUDENT, FARMER, or HOMEMAKER.");
        }
    }

    public float simulationCalYearlyRepaymentAmount(Long offerId, Long loanAmnt, Long nbrYears, typeAmort type) {
        OfferLoan offer = offerLoanRepo.findById(offerId).get();

        if (offer.getTypeLoan() != LoanType.PROJECT) {
            throw new IllegalArgumentException("Yearly repayment calculation is only applicable for LoanType PROJECT.");
        }
        float intRate = offer.getIntRate();
        float annualRate = intRate / 100;
        switch (type) {
            case CONST_ANNUITY:
                return (float) ((loanAmnt * annualRate) / (1 - Math.pow(1 + annualRate, -nbrYears)));
            case CONST_AMORTIZATION:
                float amort = loanAmnt / nbrYears;
                float interest = loanAmnt * annualRate;
                return amort + interest;
            case LOAN_PER_BLOC:
                float sumInterest = 0;
                for (int i = 0; i < nbrYears; i++) {
                    sumInterest += loanAmnt * annualRate;
                }
                float totalAmount = loanAmnt + sumInterest;
                return totalAmount;
            default:
                throw new IllegalArgumentException("Unsupported amortization type: " + type);
        }
    }
    //search and display offers that the minAmount is greater than the searchedAmount
    public List<OfferLoan> findOffersByMinAmntLessThanEqual(Long searchedAmount) {
        return offerLoanRepo.findOffersByMinAmntLessThanEqual(searchedAmount);
    }

    //count the number of offers available
    public int countAvailableOffers() {
        return offerLoanRepo.countByStatus("AVAILABLE");
    }


    public int countRequestLoansByOfferId(Long offerId) {
        return offerLoanRepo.countRequestLoansByOfferId(offerId);
    }
/*
    // Assign + unassign child to/from parent:
    public void unassignRequestFromOffer(Long idOffr, Long idReqt) {
        OfferLoan offerLoan = offerLoanRepo.findById(idOffr).get();//parent
        RequestLoan requestLoan = requestLoanRepo.findById(idReqt).get();//child
        offerLoan.getRequestloans().remove(requestLoan);
        offerLoanRepo.save(offerLoan);
    }
     */
/*
public float fetchAndUpdateTmmValue() {
    String url = "https://tunisiacompetitiveness.tn/sectors/category/secteur-monetaire";
    float tmm = 0.0f;
    try {
        Document doc = Jsoup.connect(url).get();
        Elements rows = doc.select("#detailsNat tr");
        for (Element row : rows) {
            Elements columns = row.select("td");
            if (columns.size() == 2) {
                String indicator = columns.get(0).text();
                if (indicator.contains("Taux du marché monétaire")) {
                    String tmmString = columns.get(1).text().trim();
                    tmm = Float.parseFloat(tmmString);
                    log.info("TMM value: " + tmm);
                    break; // Assuming there is only one row for TMM value
                }
            }
        }
        if (tmm != 0.0f) {
            OfferLoan offerLoan = new OfferLoan();
            offerLoan.setTmm(tmm);
            offerLoanRepo.save(offerLoan);
        } else {
            throw new RuntimeException("TMM value not found on the webpage");
        }
    } catch (IOException e) {
        e.printStackTrace();
        // Handle IOException as needed
    }
    return tmm;
}
*/

  //  @Scheduled(cron = "0 */5 * * * *")
    /*public void scheduledFetchAndUpdateTmmValue() {
        fetchAndUpdateTmmValue();


     */
}

