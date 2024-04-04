package com.example.microfinancepi.repositories;

import com.example.microfinancepi.entities.Investment;
import com.example.microfinancepi.entities.Transaction;
import com.example.microfinancepi.entities.Type_transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction,Integer> {

    List<Transaction> findByType(Type_transaction type);
    List<Transaction> findByInvestment(Investment investment);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.date_transaction BETWEEN :startDate AND :endDate")
    long countTransactionsByDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
