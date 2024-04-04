package com.example.microfinancepi.Controllers;

import com.example.microfinancepi.entities.OfferLoan;
import com.example.microfinancepi.entities.User;
import com.example.microfinancepi.entities.typeAmort;
import com.example.microfinancepi.services.ECBApiService;
import com.example.microfinancepi.services.IOfferLoanService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/offer_loan")
@SecurityRequirement(name = "bearerAuth")
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
    @Secured({"ADMIN", "AGENT"})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AGENT')")
    @PostMapping("/add-offer")
    public OfferLoan addOfferLoan(@RequestBody OfferLoan o, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return offerLoanService.addOfferLoan(o, user);
    }
// edit
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AGENT')")
    @PutMapping("/modify-offer")
    public OfferLoan modifyOfferLoan(@RequestBody OfferLoan offer, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return offerLoanService.modifyOfferLoan(offer,user);
    }
// delete
    @Secured({"ADMIN", "AGENT"})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'AGENT')")
    @DeleteMapping("/remove-offer/{offer-id}")
    public void removeOfferLoan(@PathVariable("offer-id") Long idOffr, Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        offerLoanService.removeOfferLoan(idOffr, authenticatedUser);
    }


    // Search for loan types with minimum amount lower than the searched amount
    @GetMapping("/search_offer")
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
    @GetMapping("/simulation_calculate-monthly-repayment-amount")
    public ResponseEntity<Float> simulationCalMonthlyRepaymentAmount(@RequestParam Long offerId,
                                                                      @RequestParam Long loanAmnt,
                                                                      @RequestParam Long nbrMonth) {
        try {
            float monthlyRepaymentAmount = offerLoanService.simulationCalMonthlyRepaymentAmount(offerId, loanAmnt, nbrMonth) ;
            return ResponseEntity.ok(monthlyRepaymentAmount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @GetMapping("/simulation_calculate-yearly-repayment-amount/{typeAmort}")
    public ResponseEntity<Float> simulationCalYearlyRepaymentAmount(@RequestParam Long offerId,
                                                                    @RequestParam Long loanAmnt,
                                                                    @RequestParam Long nbrYears,
                                                                    @PathVariable typeAmort typeAmort) {
        try {
            float yearlyRepaymentAmount = offerLoanService.simulationCalYearlyRepaymentAmount(offerId, loanAmnt, nbrYears, typeAmort);
            return ResponseEntity.ok(yearlyRepaymentAmount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    /*
    @GetMapping("/fetch-tmm")
    public ResponseEntity<Float> fetchTmm() {
        try {
            float tmm = offerLoanService.fetchAndUpdateTmmValue();
            return ResponseEntity.ok(tmm);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


     */

}
