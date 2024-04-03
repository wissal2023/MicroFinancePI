package com.example.microfinancepi.Controllers;

import com.example.microfinancepi.entities.RequestLoan;
import com.example.microfinancepi.entities.StatLoan;
import com.example.microfinancepi.services.IRequestLoanService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/request_loan")
public class RequestLoanRestcontroller {

    IRequestLoanService requestLoanService;

//Afficher
    @GetMapping("/retrieve_all_requests")
        public List<RequestLoan> retrieveAllLoans()
    {
        return requestLoanService.retrieveAllLoans();

    }
// get by id
    @GetMapping("/retrieve_request/{idReq}")
    public RequestLoan retrieveLoan (@PathVariable("idReq") Long idOff) {
        return requestLoanService.retrieveLoan(idOff);

    }

// add
    @PostMapping("/add-loan")
    public RequestLoan addLoan(@RequestBody RequestLoan req) {
        return requestLoanService.addLoan(req);
    }

// edit
    @PutMapping("/modify-loan")
    public RequestLoan modifyLoan(@RequestBody RequestLoan req) {
        return requestLoanService.modifyLoan(req);
    }

// delete
    @DeleteMapping("/remove-loan/{idReq}")
    public void removeRequestLoan(@PathVariable("idReq") Long idReq) {
        requestLoanService.removeLoan(idReq);
    }

// assign + unassign parent to/from child
    @PutMapping("/affecter-request-to-amortization/{request-id}/{amort-id}")
    public void assignAmortizationToRequest(@PathVariable("request-id") Long reqtId,
                                            @PathVariable("amort-id") Long amortId ) {
        requestLoanService.assignAmortizationToRequest(reqtId,amortId);
    }
    @PutMapping("/unaffecter-request-from-amortization/{request-id}")
    public void unassignAmortizationFromRequest(@PathVariable("request-id") Long reqtId ) {
        requestLoanService.unassignAmortizationFromRequest(reqtId);
    }
//afficher status loan
    @GetMapping("/find_by_status")
    public List<RequestLoan> findLoansByStatus(@RequestParam("status") StatLoan status) {
        return requestLoanService.findLoansByStatus(status);
    }
    @PutMapping("/approve_req/{requestId}")
    public ResponseEntity<String> approveLoan(@PathVariable("requestId") Long requestId) {
        requestLoanService.approveLoan(requestId);
        return ResponseEntity.ok("Loan with ID " + requestId + " has been approved.");
    }
    @PutMapping("/reject_req/{requestId}")
    public ResponseEntity<String> rejectLoan(@PathVariable("requestId") Long reqId) {
        requestLoanService.rejectLoan(reqId);
        return ResponseEntity.ok("Loan with ID " + reqId + " has been rejected.");
    }

    @GetMapping("/calculate-total-repayment-amount/{requestId}")
    public ResponseEntity<Float> calculateTotalRepaymentAmount(@PathVariable Long requestId) {
        RequestLoan requestLoan = requestLoanService.retrieveLoan(requestId);
        if (requestLoan != null) {
            float totalRepaymentAmount = requestLoanService.calculateTotalRepaymentAmount(requestLoan);
            return ResponseEntity.ok(totalRepaymentAmount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

/*
meth wassi:
@PostMapping("/mat/{id_request}")
    void matching(@PathVariable("id_request") Integer id_request){
        requestService.matching(id_request);
    }



    @GetMapping("/users/{userId}/loans")
    public ResponseEntity<List<String>> getLoanStatusForUser(@PathVariable Integer userId) {
        List<String> loanStatusList = requestService.chek_loan(userId);
        return ResponseEntity.ok(loanStatusList);
    }



 */


}
