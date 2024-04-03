package com.example.microfinancepi.repositories;



import com.example.microfinancepi.entities.Investment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IInvestmentRepository extends JpaRepository<Investment,Integer> {

}
