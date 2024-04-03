package com.example.microfinancepi.Controllers;

import com.example.microfinancepi.entities.Amortization;
import com.example.microfinancepi.services.IAmortizationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/amortization")
public class AmortizationRestcontroller {

    IAmortizationService amortizationService;

//Afficher
    @GetMapping("/retrieve_all_remborsment")
    public List<Amortization> retrieveAllAmortization()
    {
        return amortizationService.retrieveAllAmortization();
    }
// get by id
    @GetMapping("/retrieve_amortization/{idAmrt}")
    public Amortization retrieveAmortization (@PathVariable("idAmrt") Long idAmrt) {
        return amortizationService.retrieveAmortization(idAmrt);
    }

// add
    @PostMapping("/add-amortization")
    public Amortization addAmortization(@RequestBody Amortization amrt) {
        return amortizationService.addAmortization(amrt);
    }

// edit
    @PutMapping("/modify-amortization")
    public Amortization modifyAmortization(@RequestBody Amortization amrt) {
        return amortizationService.modifyAmortization(amrt);
    }

// delete
    @DeleteMapping("/remove-amortization/{idAmrt}")
    public void removeAmortization(@PathVariable("idAmrt") Long idAmrt) {
        amortizationService.removeAmortization(idAmrt);
    }

    @GetMapping("/calculate-remaining-balance/{idAmrt}/{periodsElapsed}")
    public ResponseEntity<Long> calculateRemainingBalance(@PathVariable Long idAmrt, @PathVariable Long periodsElapsed) {
        try {
            Long remainingBalance = amortizationService.calculateRemainingBalance(idAmrt, periodsElapsed);
            return new ResponseEntity<>(remainingBalance, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
/*
meth wassim:
 @PostMapping("/users/{userId}/pret")
    public void pret(@PathVariable Integer userId, @RequestParam("amount") Float amount,@RequestParam("period") Integer period) {
        pretService.pret(userId, amount, period);
    }
    @PostMapping("/check")
    public void checkPret() {
        pretService.checkPret();
    }
    }
 */


}
