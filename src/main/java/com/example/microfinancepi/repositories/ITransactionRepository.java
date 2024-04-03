package com.example.microfinancepi.repositories;

import com.example.microfinancepi.entities.Investment;
import com.example.microfinancepi.entities.Transaction;
import com.example.microfinancepi.entities.Type_transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ITransactionRepository extends JpaRepository<Transaction,Integer> {

    List<Transaction> findByType(Type_transaction type);
    List<Transaction> findByInvestment(Investment investment);
}
