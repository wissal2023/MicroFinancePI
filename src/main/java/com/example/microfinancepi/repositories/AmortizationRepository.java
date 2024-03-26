package com.example.microfinancepi.repositories;

import com.example.microfinancepi.entities.Amortization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AmortizationRepository extends JpaRepository<Amortization,Long> {

    // List<Pret> findByDatefinBefore(Date date);
}
