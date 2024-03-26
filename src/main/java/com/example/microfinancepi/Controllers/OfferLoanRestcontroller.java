package com.example.microfinancepi.Controllers;

import com.example.microfinancepi.entities.OfferLoan;
import com.example.microfinancepi.services.IOfferLoanService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/offer_loan")
public class OfferLoanRestcontroller {

    IOfferLoanService offerLoanService;
//Afficher
    @GetMapping("/retrieve_all_offers")
    public List<OfferLoan> retrieveAllOfferLoans()
    {
        return offerLoanService.retrieveAllOfferLoans();
    }
// get by id
    @GetMapping("/retrieve_offer/{idOffer}")
    public OfferLoan retrieveOfferLoan (@PathVariable("idOffer") Long idOff) {
        return offerLoanService.retrieveOfferLoan(idOff);
    }

// add
    @PostMapping("/add-offer")
    public OfferLoan addOfferLoan(@RequestBody OfferLoan o) {
        return offerLoanService.addOfferLoan(o);
    }

// edit
    @PutMapping("/modify-offer")
    public OfferLoan modifyOfferLoan(@RequestBody OfferLoan offer) {
        return offerLoanService.modifyOfferLoan(offer);
    }

// delete
    @DeleteMapping("/remove-offer/{offer-id}")
    public void removeOfferLoan(@PathVariable("offer-id") Long idOffr) {
        offerLoanService.removeOfferLoan(idOffr);
    }

//Assign + unassign
    @PutMapping("/affecter-request-to-offer/{request-id}/{offer-id}")
    public void assignRequestToOffer(@PathVariable("request-id") Long reqtId,
                                     @PathVariable("offer-id") Long offerId ) {
        offerLoanService.assignRequestToOffer(reqtId,offerId);
    }
    @PutMapping("/unaffecter-request-from-offer/{request-id}/{offer-id}")
    public void unassignRequestFromOffer(@PathVariable("request-id") Long reqtId,
                                         @PathVariable("offer-id") Long offerId
    ) {
        offerLoanService.unassignRequestFromOffer(reqtId,offerId);
    }

    // Search for loan types with minimum amount lower than the searched amount
    @GetMapping("/search")
    public ResponseEntity<?> searchOffersByMinAmount(@RequestParam("searchedAmount") Long searchedAmount) {
        List<OfferLoan> offers = offerLoanService.findOffersByMinAmntLessThanEqual(searchedAmount);
        if (offers.isEmpty()) {
            return ResponseEntity.ok("No offer found with the amount requested");
        }
        return ResponseEntity.ok(offers);
    }
    @GetMapping("/available_offer")
    public ResponseEntity<Integer> countAvailableOffers() {
        int count = offerLoanService.countAvailableOffers();
        return ResponseEntity.ok(count);
    }
    @GetMapping("/count_requestLoans/{offerId}")
    public ResponseEntity<Integer> countRequestLoansByOfferId(@PathVariable Long offerId) {
        int count = offerLoanService.countRequestLoansByOfferId(offerId);
        return ResponseEntity.ok(count);
    }

    /*

    @GetMapping("/{id_offer}/{id_request}/amortization")
    public ResponseEntity<Map<Integer, Map<String, Double>>> calculateCapitalAmortiAndRestant(
            @PathVariable Integer id_offer, @PathVariable Integer id_request) {
        Map<Integer, Map<String, Double>> result = offers_creditService.calculateCapitalAmortiAndRestant(id_offer, id_request);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/{amount}/{repaymentPeriod}/offer")
    public ResponseEntity<List<Offers_Credit>> getOffer(@PathVariable Integer amount,
                                                        @PathVariable String repaymentPeriod) {
        List<Offers_Credit> matchingOffers =  offers_creditService.findMatchingOffer(amount, repaymentPeriod);
        if (matchingOffers != null){
            return  ResponseEntity.ok(matchingOffers);
        }
        else {
            return ResponseEntity.notFound().build();
        }



    }
   @GetMapping("/{id_offer}/statistics")
    public ResponseEntity<OfferStatistics> getOfferStatistics(@PathVariable ("id_offer")Integer id_offer) {
        OfferStatistics statistics = offers_creditService.getOfferStatistics(id_offer);
        return ResponseEntity.ok(statistics);
    }

     */
}
