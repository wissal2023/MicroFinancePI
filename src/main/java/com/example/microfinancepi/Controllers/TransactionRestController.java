package com.example.microfinancepi.Controllers;



import com.example.microfinancepi.entities.Transaction;
import com.example.microfinancepi.entities.Type_transaction;
import com.example.microfinancepi.services.TransactionService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/Transaction")
public  class TransactionRestController {
    private TransactionService transactionService;

    @PostMapping("/add")
    Transaction AddTransaction(@RequestBody Transaction transaction){
        return transactionService.AddTransaction(transaction);
    }
    @GetMapping("/all")
    List<Transaction> retrieveAllTransactions(){
        return  transactionService.retrieveAllTransactions();
    }
    @GetMapping("/get/{id}")
    Transaction retrieveTransaction(@PathVariable("id") Integer id_transaction){
        return transactionService.retrieveTransaction(id_transaction);
    }
    @DeleteMapping("/delete/{id}")
    void removeTransaction(@PathVariable("id") Integer id_transaction){
        transactionService.removeTransaction(id_transaction);
    }
    @PutMapping("/update")
    Transaction updateTransaction(@RequestBody Transaction transaction){
        return transactionService.updateTransaction(transaction);
    }
    @PutMapping("/assigntrtoinv/{id_transaction}/{id_invest}")
    Transaction assignTransactionToInvestment(@PathVariable("id_transaction") Integer id_transaction,
                                              @PathVariable("id_invest") Integer id_invest){
        return transactionService.assignTransactionToInvestment(id_transaction,id_invest);
    }
@PutMapping("/assignTrtoUser/{id_transaction}/{id_user}")
    public void assignTransactionToUser(@PathVariable Integer id_transaction, @PathVariable Integer id_user){
        transactionService.assignTransactionToUser(id_transaction,id_user);
    }
    @PostMapping("/withdraw/{user_id}/{rece_id}/{amount}/{type}")
    public Transaction withdraw(@PathVariable("user_id") Integer userId,
                                @PathVariable("amount") Float amount,
                                @PathVariable("rece_id") Integer receiverId,@PathVariable("type") Type_transaction type) {
        return transactionService.withdraw(userId, amount, receiverId,type);
    }
  @GetMapping("/testtype/{id_transaction}")

  public Type_transaction testtr(@PathVariable("trans_id") Integer transactionId){
        return transactionService.testtr(transactionId);
  }
}