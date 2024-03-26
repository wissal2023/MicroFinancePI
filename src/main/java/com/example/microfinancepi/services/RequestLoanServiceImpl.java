package com.example.microfinancepi.services;

import com.example.microfinancepi.entities.Amortization;
import com.example.microfinancepi.entities.OfferLoan;
import com.example.microfinancepi.entities.RequestLoan;
import com.example.microfinancepi.entities.StatLoan;
import com.example.microfinancepi.repositories.AmortizationRepository;
import com.example.microfinancepi.repositories.RequestLoanRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor// inject repo in service
public class RequestLoanServiceImpl implements IRequestLoanService{

    RequestLoanRepository requestLoanRepo;
    OfferLoanServiceImpl offerLoan;
    AmortizationRepository amortizationRepos;

    public List<RequestLoan> retrieveAllLoans() { return requestLoanRepo.findAll();   }
    public RequestLoan retrieveLoan(Long idRequest) {
        return requestLoanRepo.findById(idRequest).get();
    }
    public RequestLoan addLoan (RequestLoan o)  {        return requestLoanRepo.save(o);    }
    public RequestLoan modifyLoan (RequestLoan request) {
        return requestLoanRepo.save(request);
    }
    public void removeLoan(Long idRequest) { requestLoanRepo.deleteById(idRequest);    }

    // Assign + unassign
    public void assignAmortizationToRequest (Long idReqt,Long idAmrt)
    {
        RequestLoan requestLoan= requestLoanRepo.findById(idReqt).get();//parent
        Amortization amortization = amortizationRepos.findById(idAmrt).get();//child

        requestLoan.setAmortization(amortization);
        requestLoanRepo.save(requestLoan);
    }
    public void unassignAmortizationFromRequest (Long idReqt)
    {
        RequestLoan requestLoan= requestLoanRepo.findById(idReqt).get();//parent
        requestLoan.setAmortization(null);
        requestLoanRepo.save(requestLoan);
    }
//status
/*Retrieve all loan requests with a specific status
public List<RequestLoan> retrieveAllLoansByStatus(String status) {
    return requestLoanRepo.findAllByStatus(status);
}
 */
    public List<RequestLoan> findLoansByStatus(StatLoan status) {
        return requestLoanRepo.findByStatus(status);
    }
    public void approveLoan(Long requestId) {
        RequestLoan requestLoan = requestLoanRepo.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request Loan not found with id: " + requestId));

        if (requestLoan.getStatus() == StatLoan.PENDING) {
            requestLoan.setStatus(StatLoan.APPROVED);
            requestLoanRepo.save(requestLoan);
            log.info("Loan with ID {} has been approved.", requestId);
        } else {
            throw new IllegalStateException("Cannot approve loan with ID " + requestId + ". It is not in PENDING status.");
        }
    }
    public void rejectLoan(Long reqId) {
        RequestLoan requestLoan = requestLoanRepo.findById(reqId)
                .orElseThrow(() -> new IllegalArgumentException("Request Loan not found with id: " + reqId));

        if (requestLoan.getStatus() == StatLoan.PENDING) {
            requestLoan.setStatus(StatLoan.REJECTED);
            requestLoanRepo.save(requestLoan);
            log.info("Loan with ID {} has been rejected.", reqId);
        } else {
            throw new IllegalStateException("Cannot reject loan with ID " + reqId + ". It is not in PENDING status.");
        }
    }

    public float calculateTotalRepaymentAmount(RequestLoan requestLoan) {
        float loanAmount = requestLoan.getLoanAmnt();
        float totalRepaymentAmount = 0;
        Set<OfferLoan> offerLoans = requestLoan.getOfferloan();
        if (!offerLoans.isEmpty()) {
            // Iterate over each OfferLoan and calculate the total repayment amount
            for (OfferLoan offerLoan : offerLoans) {
                float interestRate = offerLoan.getIntRate(); // Get interest rate from OfferLoan entity
                long repaymentPeriod = offerLoan.getMinRepaymentPer(); // Get repayment period from OfferLoan entity
                totalRepaymentAmount += loanAmount + (loanAmount * interestRate * repaymentPeriod);
            }
            return totalRepaymentAmount;
        } else {
            log.error("OfferLoan not found for RequestLoan ID: {}", requestLoan.getRequestId());
            return 0;
        }
    }


    /*
    //Retrieve all loan requests for a specific user
    public List<RequestLoan> retrieveAllLoansByUser(Long userId) {
        return requestLoanRepo.findAllByUserId(userId);
    }
    //Retrieve all loan requests for a specific loan type
    public List<RequestLoan> retrieveAllLoansByType(String loanType) {
        return requestLoanRepo.findAllByLoanType(loanType);
    }

    //Retrieve all loan requests within a specific date range
    public List<RequestLoan> retrieveAllLoansByDateRange(LocalDate startDate, LocalDate endDate) {
        return requestLoanRepo.findAllByRequestDateBetween(startDate, endDate);
    }
    //Retrieve all loan requests that have been assigned an amortization schedule
    public List<RequestLoan> retrieveAllLoansWithAmortization() {
        return requestLoanRepo.findAllByAmortizationIsNotNull();
    }
    //Retrieve all loan requests that have been approved/ rejected
    public List<RequestLoan> retrieveAllApprovedLoans() {
        return requestLoanRepo.findAllByStatus("approved");
    }
    public List<RequestLoan> retrieveAllRejectedLoans() {
        return requestLoanRepo.findAllByStatus("rejected");
    }
    //Retrieve all loan requests that have been pending for a certain number of days
    public List<RequestLoan> retrieveAllPendingLoansOlderThan(int days) {
        LocalDate cutoffDate = LocalDate.now().minusDays(days);
        return requestLoanRepo.findAllByRequestDateBeforeAndStatus("pending", cutoffDate);
    }

--------------------------------------------------------
meth wassim:
 @Override
    public Request assignRequestToOffers_Credit(Integer id_request, Integer id_offer, Integer id_user){
        Request request=IRequestrepository.findById(id_request).orElse(null);
        User user = userRepository.findById(id_user).orElse(null);
        if(user==null){
            throw new IllegalArgumentException("user invalid");
        }
        Offers_Credit offers_credit=iOffersCreditRepository.findById(id_offer).orElse(null);


        request.setUser(user);
        request.setOffer(offers_credit);
        return IRequestrepository.save(request);
    }




    @Override
    public AccOrRef matching(Integer id_request ) {
        List<AccOrRef> all =acc.findAll();

        Request req = IRequestrepository.findById(id_request).orElse(null);
        AccOrRef azz = new AccOrRef();

            if (req.getOffer().getId_offer() != null && req.getUser().getId_user() != null) {
                for (AccOrRef verif:all){
                    if (verif.getRequest().getOffer().getId_offer()==req.getOffer().getId_offer() && verif.getRequest().getUser().getId_user()==req.getUser().getId_user()){
                        return null;

                    }
                }


                if (req.getOffer().getMax_amount() >= req.getAmount_req() && req.getOffer().getMin_amount() <= req.getAmount_req()) {
                    azz.setRequest(req);
                    azz.setCheck_loan("Request accepté");
                    req.setAccOrRef(azz);



                }

                else {
                    azz.setRequest(req);
                    azz.setCheck_loan("Request réfusé");
                    req.setAccOrRef(azz);



                }

            }
        return acc.save(azz);
        }

  @Override
    public List<String> chek_loan(Integer id_user) {
        List<String> All= new ArrayList<>();
        List<Request> list = IRequestrepository.findRequestByUser(id_user);
        for (Request req : list ){
           {
               AccOrRef accOrRef = req.getAccOrRef();
               if (accOrRef.getCheck_loan()!=null){
                   All.add(req.getUser().getUser_firstname()+ " :request numéro: " +accOrRef.getRequest().getId_request() +" : "+  accOrRef.getCheck_loan()+   " :Offer credit numéro: "+ req.getOffer().getId_offer());
               }


            }
        }
      return All;
  }

     */




}
