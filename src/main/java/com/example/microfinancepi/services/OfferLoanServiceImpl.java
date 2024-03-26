package com.example.microfinancepi.services;

import com.example.microfinancepi.entities.OfferLoan;
import com.example.microfinancepi.entities.RequestLoan;
import com.example.microfinancepi.repositories.OfferLoanRepository;
import com.example.microfinancepi.repositories.RequestLoanRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor// inject repo in service
public class OfferLoanServiceImpl implements  IOfferLoanService {

    OfferLoanRepository offerLoanRepo;
    RequestLoanRepository requestLoanRepo;

    public List<OfferLoan> retrieveAllOfferLoans() {
        return offerLoanRepo.findAll();
    }

    public OfferLoan retrieveOfferLoan(Long idOffer) {
        return offerLoanRepo.findById(idOffer).get();
    }

    public OfferLoan addOfferLoan(OfferLoan o) {
        return offerLoanRepo.save(o);
        //if (!user.getRoles().contains(RoleName.ADMIN)) {
        //      throw new AccessDeniedException("you're not authorized");
        //    }
    }

    public OfferLoan modifyOfferLoan(OfferLoan offer) {
        return offerLoanRepo.save(offer);
    }

    public void removeOfferLoan(Long idOffer) {
        offerLoanRepo.deleteById(idOffer);
    }

    // Assign + unassign child to/from parent
    public void assignRequestToOffer(Long idOffer, Long idReqt) {
        OfferLoan offerLoan = offerLoanRepo.findById(idOffer).get();//parent
        RequestLoan requestLoan = requestLoanRepo.findById(idReqt).get();//child

        offerLoan.getRequestloans().add(requestLoan);
        offerLoanRepo.save(offerLoan);
    }

    public void unassignRequestFromOffer(Long idOffr, Long idReqt) {
        OfferLoan offerLoan = offerLoanRepo.findById(idOffr).get();//parent
        RequestLoan requestLoan = requestLoanRepo.findById(idReqt).get();//child

        offerLoan.getRequestloans().remove(requestLoan);
        offerLoanRepo.save(offerLoan);
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

}
    /*
    //scheduler only in service to display on console
    @Scheduled(fixedDelay = 15000) // en milliseconde
    public List<OfferLoan> retrieveAllOffers(){

        List<OfferLoan> listOffr = offerLoanRepo.findAll();
        log.info("how many offers found:" + listOffr.size());
        for (OfferLoan offer: listOffr){
            log.info("list of offers:" + offer);
        }
        return  listOffr;
    }

}

methodes wassim:
@Override
    public Map<Integer, Map<String, Double>> calculateCapitalAmortiAndRestant(Integer id_offer, Integer id_request) {
        Offers_Credit offreCredit = Ioffers_creditrepository.findById(id_offer).orElse(null);
        Request request = IRequestRepository.findById(id_request).orElse(null);
        if (offreCredit == null || request == null) {
            throw new IllegalArgumentException("Invalid offre-credit or request id");
        }

        Map<Integer, Map<String, Double>> result = new LinkedHashMap<>();
        double capitalRestant = request.getAmount_req();

        double annuity = calculateAnnuity(request.getAmount_req(), offreCredit.getInterest_rate(), Integer.parseInt(request.getTerm_loan()));
        double term_loan = Integer.parseInt(request.getTerm_loan());
        double interest_rate = Double.parseDouble(offreCredit.getInterest_rate());
        double amount_req = request.getAmount_req();


        for (int year = 1; year <= Integer.parseInt(request.getTerm_loan()); year++) {
            Map<String, Double> yearData = new LinkedHashMap<>();
            double capitalAmorti = calculateCapitalAmorti(capitalRestant, annuity, offreCredit.getInterest_rate());
            capitalRestant -= capitalAmorti;
            yearData.put("capitalAmorti", capitalAmorti);
            yearData.put("capitalRestant", capitalRestant);
            yearData.put("term_loan", term_loan);
            yearData.put("interest_rate", interest_rate);
            yearData.put("amount_req", amount_req);

            result.put(year, yearData);
        }


        return result;
    }


    private double calculateAnnuity(double amount, String interestRate, int term) {
        double rate = Double.parseDouble(interestRate.replace("%", "")) / 100.0;
        double r = rate / 12.0;
        double n = term * 12.0;
        return (amount * r) / (1 - Math.pow(1 + r, -n));
    }

    private double calculateCapitalAmorti(double capitalRestant, double annuity, String interestRate) {
        double rate = Double.parseDouble(interestRate.replace("%", "")) / 100.0;
        double r = rate / 12.0;
        return annuity - (capitalRestant * r);

    }

    @Override
    public List<Offers_Credit> findMatchingOffer(Integer amount, String repaymentPeriode) {
        // Récupérer toutes les offres de crédit
        List<Offers_Credit> all = Ioffers_creditrepository.findAll();

        List<Offers_Credit> matchingOffers = new ArrayList<>();

        // Parcourir chaque offre de crédit
        for (Offers_Credit offer : all) {
            if (amount >= offer.getMin_amount() && amount <= offer.getMax_amount()) {
                if (repaymentPeriode.equals(offer.getRepayment_period())) {

                    matchingOffers.add(offer);


                }
            }
        }

        return matchingOffers;
    }
   @Override
    public OfferStatistics getOfferStatistics(Integer id_offer) {


      List<Request> requests=IRequestRepository.findRequestsByOfferId(id_offer);
        Integer acceptedRequests = 0;
        Integer rejectedRequests = 0;
        Integer pendingRequests = 0;
        for (Request request : requests) {

            if (request.getAccOrRef() != null) {
                if ("Request accepté".equalsIgnoreCase(request.getAccOrRef().getCheck_loan())) {
                    acceptedRequests++;
                } else if ("Request réfusé".equalsIgnoreCase(request.getAccOrRef().getCheck_loan())) {
                    rejectedRequests++;
                }
            } else {
                pendingRequests++;
            }
        }
        OfferStatistics statistics = new OfferStatistics();
        statistics.setOfferId(id_offer);
        statistics.setAcceptedRequests(acceptedRequests);
        statistics.setRejectedRequests(rejectedRequests);
        statistics.setPendingRequests(pendingRequests);
        return statistics;



   }


     */
