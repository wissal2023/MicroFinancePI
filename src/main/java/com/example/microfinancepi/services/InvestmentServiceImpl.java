package com.example.microfinancepi.services;

import com.example.microfinancepi.entities.*;
import com.example.microfinancepi.repositories.IInvestmentRepository;
import com.example.microfinancepi.repositories.ITransactionRepository;
import com.example.microfinancepi.repositories.UserRepository;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class InvestmentServiceImpl implements InvestmentService {
    private IInvestmentRepository iinvestmentRepository;
    private ITransactionRepository iTransactionRepository;
 //   private EmailSenderService emailSenderService;

    private UserRepository userRepository;
    private TransactionService transactionService;

    @Override
    public List<Investment> retrieveAllInvestissements(){
        return iinvestmentRepository.findAll();
    }
    @Override
    public Investment AddInvestissement(Investment investment){


        investment.setDate_debut(Calendar.getInstance().getTime());
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(Calendar.getInstance().getTime());
        calendar.add(Calendar.MONTH, 12);
        Date endDate= calendar.getTime();
        investment.setDate_fin(endDate);
        return iinvestmentRepository.save(investment);
    }
    @Override
    public void RemoveInvestissement(Integer numInvestissement){
        iinvestmentRepository.deleteById(numInvestissement);
    }
    @Override
    public Investment retrieveInvestissement(Integer numInvestissement){
        return iinvestmentRepository.findById(numInvestissement).orElse(null);
    }
    @Override
    public Investment UpdateInvestissement(Investment investment){
        return iinvestmentRepository.save(investment);
    }

    @Override
    public void invest(Integer userId, Float amount, Integer investment_id){
       Investment investment= iinvestmentRepository.findById(investment_id).orElse(null);
        if(investment.getAmount_inv()+amount> investment.getInvest_value()){
            throw new IllegalArgumentException("The amount that you give is higher than we ask , please try with less one");
        }


        Float income=amount*0.05f;
        investment.setAmount_inv(amount*0.95f+investment.getAmount_inv());
        Transaction transaction=transactionService.withdraw(userId,amount*0.95f,investment.getOwner_id(), Type_transaction.INVESTMENT);
        Transaction transaction1=transactionService.withdraw(userId,income,1,Type_transaction.INCOME);
        //transaction1.setInvestment(investment);
      //  transaction.setInvestment(investment);
        iTransactionRepository.save(transaction);
        iTransactionRepository.save(transaction1);
  //      emailSenderService.SendInvestmentEmail("samar.saidana@esprit.tn","", (float) (amount*0.095));
    }
    @Override
    @Transactional
    @Scheduled(fixedDelay = 86400000)
    public void checkinvest() {
        List<Investment> investments = iinvestmentRepository.findAll();

        for (Investment investment : investments) {
            Float value_inv= investment.getInvest_value();

                Float amount_invts=0.0f;
                  for(Transaction transaction : investment.getTransactions()){

                      if(transaction.getType() == Type_transaction.INVESTMENT){

                           amount_invts=transaction.getValue();
                          Float interest= (amount_invts/value_inv)*investment.getIncome_by_day()*0.20f;
                          Float income= (amount_invts/value_inv)*investment.getIncome_by_day()*0.05f;

                          transactionService.withdraw(investment.getOwner_id(),interest,transaction.getUser().getId_user(),Type_transaction.INTEREST);
                          transactionService.withdraw(investment.getOwner_id(),income,1,Type_transaction.INCOME);
                         User user=  userRepository.findById(investment.getOwner_id()).orElse(null);
                   //       emailSenderService.SendInvestmentInterestEmail("samar.saidana@esprit.tn",user.getEmail_address(),interest,investment.getProject_name());
                      }
                  }


            }

        }
    @Override
    @Transactional
    @Scheduled(fixedDelay = 86400000)
public void checkInvestStatus(){
    List<Investment> investments = iinvestmentRepository.findAll();

    for (Investment investment : investments) {



      investment.setStatus(Status_inv.INPROGRESS);
        if (investment.getInvest_value()==investment.getAmount_inv()) {

            investment.setStatus(Status_inv.CLOSED);
        }
        if(investment.getDate_fin().before(Calendar.getInstance().getTime())) {
            investment.setStatus(Status_inv.COMPLETED);}
        iinvestmentRepository.save(investment);

    }

}

    @Override
    public Float calculateTotalInvestmentIncome(Integer investment_id) {
        Investment investment = iinvestmentRepository.findById(investment_id).orElse(null);
        Float totalInvestmentIncome = 0.0f;
        for (Transaction transaction : investment.getTransactions()) {
            if (transaction.getType() == Type_transaction.INCOME) {
                totalInvestmentIncome += transaction.getValue();
            }
        }
        return totalInvestmentIncome;
    }

    @Override
    public Float calculateTotalInvestmentInterest(Integer investment_id) {
         Investment investment = iinvestmentRepository.findById(investment_id).orElse(null);
        Float totalInvestmentInterest = 0.0f;
        for (Transaction transaction : investment.getTransactions()) {
            if (transaction.getType() == Type_transaction.INTEREST) {
                totalInvestmentInterest += transaction.getValue();
            }
        }
        return totalInvestmentInterest;
    }
    @Override
    public Float calculateTotalInvestment(Integer investment_id) {
        List<Transaction> transactions= iTransactionRepository.findAll();
        Float totalInvestment = 0.0f;
        for (Transaction transaction : transactions) {
            if (transaction.getType() == Type_transaction.INVESTMENT) {
                totalInvestment += transaction.getValue();
            }
        }
        return totalInvestment;
    }




}
