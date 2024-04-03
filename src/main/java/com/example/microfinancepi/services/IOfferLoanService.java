package com.example.microfinancepi.services;


import com.example.microfinancepi.entities.OfferLoan;

import java.util.List;

public interface IOfferLoanService {
    public List<OfferLoan> retrieveAllOfferLoans();
    public OfferLoan retrieveOfferLoan(Long idOffer);
    public OfferLoan addOfferLoan (OfferLoan o);
    public OfferLoan modifyOfferLoan (OfferLoan offer);
    public void removeOfferLoan(Long idOffer);

    //Assign + unassign:
    public void assignRequestToOffer (Long idOffer, Long idReqt);
    public void unassignRequestFromOffer (Long idOffr, Long idReqt);
    public List<OfferLoan> findOffersByMinAmntLessThanEqual(Long searchedAmount);
    public int countAvailableOffers();
    public int countRequestLoansByOfferId(Long offerId);

    /* methode wassim:

    List<Double> calculateAmortization (Integer id_offer);
    Map<Integer, Map<String, Double>> calculateCapitalAmortiAndRestant(Integer id_offer, Integer id_request);
    List<Offers_Credit> findMatchingOffer(Integer amount, String repaymentPeriode);
    OfferStatistics getOfferStatistics(Integer id_offer);

     */


}
