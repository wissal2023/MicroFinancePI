package com.example.microfinancepi.services;
import com.example.microfinancepi.entities.RequestLoan;
import com.example.microfinancepi.entities.StatLoan;

import java.util.List;

public interface IRequestLoanService {
    public List<RequestLoan> retrieveAllLoans();
    public RequestLoan retrieveLoan(Long idRequest);
    public RequestLoan addLoan (RequestLoan o);
    public RequestLoan modifyLoan (RequestLoan request);
    public void removeLoan(Long idRequest);

    //Assign + unassign child to/from parent
    public void assignAmortizationToRequest (Long idReqt,Long idAmrt);
    public void unassignAmortizationFromRequest (Long idReqt);
    public List<RequestLoan> findLoansByStatus(StatLoan status);
    public void approveLoan(Long requestId);
    public void rejectLoan(Long reqId);
    public float calculateTotalRepaymentAmount(RequestLoan requestLoan);

    /*
    meth wassim:
    Request assignRequestToOffers_Credit(Integer id_request, Integer id_offer,Integer id_user );
    AccOrRef matching (Integer id_request);
    List<String> chek_loan(Integer id_user);
     */


}
