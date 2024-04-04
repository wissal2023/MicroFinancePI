package com.example.microfinancepi.services;
import com.example.microfinancepi.entities.*;

import java.util.List;

public interface IRequestLoanService {
    public List<RequestLoan> retrieveAllLoans();
    public RequestLoan retrieveLoan(Long idRequest);
    //public RequestLoan addLoan (RequestLoan o);
    public RequestLoan addLoanAndAssignRequestToOffer(RequestLoan requestLoan, Long offerId, typeAmort type);
    public RequestLoan modifyLoan (RequestLoan request);
    public void removeLoan(Long idRequest);

    public List<RequestLoan> findLoansByStatus(StatLoan status);
    public void approveLoan(Long requestId);
    public void rejectLoan(Long reqId, TwilioService twilioService);
    //Assign + unassign child to/from parent
    public void unassignAmortizationFromRequest (Long idReqt);
    public List<RequestLoan> retrieveAllLoansWithAmortization();
    //public List<RequestLoan> searchFilterLoanApi(String keyword);
    //public List<RequestLoan> searchSortLoanApi(String sortBy);

}

