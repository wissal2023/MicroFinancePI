package com.example.microfinancepi.services;

import com.example.microfinancepi.entities.Investment;
import com.example.microfinancepi.entities.Transaction;
import com.example.microfinancepi.entities.Type_transaction;
import com.example.microfinancepi.entities.User;
import com.example.microfinancepi.repositories.IInvestmentRepository;
import com.example.microfinancepi.repositories.ITransactionRepository;
import com.example.microfinancepi.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Calendar;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService{
   // private EmailSenderService emailSenderService;
    private ITransactionRepository ItransactionRepository;
    private UserRepository userRepository;
    private IInvestmentRepository iinvestmentRepository;
    @Override
    public List<Transaction> retrieveAllTransactions(){
        return ItransactionRepository.findAll();
    }
    @Override
    public Transaction AddTransaction(Transaction transaction){
        return  ItransactionRepository.save(transaction);
    }
    @Override
   public void removeTransaction(Integer numTransaction){
        ItransactionRepository.deleteById(numTransaction);
   }
   @Override
    public Transaction retrieveTransaction(Integer numTransaction){
        return ItransactionRepository.findById(numTransaction).orElse(null);
    }
    @Override
    public Transaction updateTransaction(Transaction transaction){
        return ItransactionRepository.save(transaction);
    }

    public Transaction assignTransactionToInvestment(Integer id_transaction,Integer id_invest){
        Transaction transaction=ItransactionRepository.findById(id_transaction).orElse(null);
        Investment investment = iinvestmentRepository.findById(id_invest).orElse(null);
      //  transaction.setInvestment(investment);
        return ItransactionRepository.save(transaction);
    }
   @Override
   public void assignTransactionToUser(Integer userId, Integer transactionId) {
       User user = userRepository.findById(userId).orElse(null);
       if (user == null) {
           throw new IllegalArgumentException("User not found with ID: " + userId);
       }

       Transaction transaction = ItransactionRepository.findById(transactionId).orElse(null);
       if (transaction == null) {
           throw new IllegalArgumentException("Transaction not found with ID: " + transactionId);
       }

       transaction.setUser(user);
       ItransactionRepository.save(transaction);
   }
    @Override
  public Transaction withdraw(Integer userId, Float amount, Integer receiverId, Type_transaction type){
      User user = userRepository.findById(userId)
              .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
      User receiver = userRepository.findById(receiverId)
              .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
      Float userAmount = user.getAmount();
      Float recAmount=receiver.getAmount();
      if (user.getAmount() < amount) {
          throw new IllegalArgumentException("User does not have enough balance to make the transaction");
      }
      user.setAmount(userAmount - amount);

      receiver.setAmount(recAmount + amount);
      userRepository.save(receiver);
      Transaction transaction= new Transaction();

      transaction.setDate_transaction(Calendar.getInstance().getTime());
      transaction.setValue(amount);
      transaction.setSender(userId);
      transaction.setReceiver(receiverId);
      transaction.setUser(user);
      transaction.setType(type);

        userRepository.save(user);
       /* if (transaction.getType()==Type_transaction.WITHDRAW){
            emailSenderService.sendTransactionEmail("samar.saidana@esprit.tn",user.getEmail_address(),transaction.getValue());
            emailSenderService.receiveTransactionEmail("samar.saidana@esprit.tn",receiver.getEmail_address(),transaction.getValue());
        } */
return  ItransactionRepository.save(transaction);
  }
    @Override
    public Type_transaction testtr(Integer transactionId){
        Transaction transaction = ItransactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + transactionId));
        Type_transaction type = transaction.getType();
         return type;
    }
@Override
    public List<Transaction> findByType(Type_transaction type) {
        return ItransactionRepository.findByType(type);
    }
@Override
    public List<Transaction> findByInvestment(Investment investment) {
        return ItransactionRepository.findByInvestment(investment);
    }
}
