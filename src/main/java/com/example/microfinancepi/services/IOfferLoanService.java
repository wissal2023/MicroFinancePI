package com.example.microfinancepi.services;


import com.example.microfinancepi.entities.OfferLoan;
import com.example.microfinancepi.entities.User;
import com.example.microfinancepi.entities.typeAmort;

import java.io.IOException;
import java.util.List;

public interface IOfferLoanService {

    public List<OfferLoan> retrieveAllOfferLoans();
    public OfferLoan retrieveOfferLoan(Long idOffer);
    public OfferLoan addOfferLoan (OfferLoan o, User user);
    public OfferLoan modifyOfferLoan(OfferLoan offer, User user);
    public void removeOfferLoan(Long idOffer, User user);
    public List<OfferLoan> findOffersByMinAmntLessThanEqual(Long searchedAmount);
    public int countAvailableOffers();
    public int countRequestLoansByOfferId(Long offerId);
    public float simulationCalMonthlyRepaymentAmount(Long offerId, Long loanAmnt, Long nbrMonth);
    public float simulationCalYearlyRepaymentAmount(Long offerId, Long loanAmnt, Long nbrYears, typeAmort type);
    //public float fetchAndUpdateTmmValue() throws IOException;
    // public void unassignRequestFromOffer (Long idOffr, Long idReqt);




}
