package com.example.microfinancepi.Controllers;

import com.example.microfinancepi.entities.Investment;
import com.example.microfinancepi.services.InvestmentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/Investment")
public class InvestissementRestController {
    private InvestmentService investmentService;

    @PostMapping("/add")
    Investment AddInvestissement(@RequestBody Investment investment){
        return investmentService.AddInvestissement(investment);
    }
    @GetMapping("/all")
    List<Investment> retrieveAllInvestissements(){

        return investmentService.retrieveAllInvestissements();
    }
    @GetMapping("/get/{id}")
    Investment retrieveInvestissement(@PathVariable("id") Integer numInvestissement){
        return investmentService.retrieveInvestissement(numInvestissement);
    }
    @DeleteMapping("/delete/{id}")
    void RemoveInvestissement(@PathVariable("id") Integer numInvestissement){
        investmentService.RemoveInvestissement(numInvestissement);
    }
    @PutMapping ("/update")
    Investment UpdateInvestissement(@RequestBody Investment investment){
        return investmentService.UpdateInvestissement(investment);
    }
    @PostMapping("/users/{userId}/invest")
    public void invest(@PathVariable Integer userId, @RequestParam("amount") Float amount,@RequestParam("period") Integer project_id) {
        investmentService.invest(userId, amount,project_id);
    }

@GetMapping("/invIncome/{inv_id}")
    public Float calculateTotalInvestmentIncome(@PathVariable Integer investment_id){
       return investmentService.calculateTotalInvestmentIncome(investment_id);
    }

    @GetMapping("/invInterest/{inv_id}")
    public Float calculateTotalInvestmentInterest(@PathVariable Integer investment_id){
        return investmentService.calculateTotalInvestmentInterest(investment_id);
    }
   @PostMapping("/check")
    public void checkinvest() {
        investmentService.checkinvest();
    }
    @GetMapping("/{calculTotalInvest}")
    public Float calculateTotalInvestment(@PathVariable Integer investment_id){
        return investmentService.calculateTotalInvestment(investment_id);

    }
@PostMapping("/checkst")
    public void checkInvestStatus(){
    investmentService.checkInvestStatus();
    }}

