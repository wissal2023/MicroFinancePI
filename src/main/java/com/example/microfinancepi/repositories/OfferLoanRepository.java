package com.example.microfinancepi.repositories;

import com.example.microfinancepi.entities.OfferLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferLoanRepository extends JpaRepository<OfferLoan,Long> {

    List<OfferLoan> findOffersByMinAmntLessThanEqual(Long searchedAmount);// search the
    int countByStatus(String status); //count the number of offers dispo

    @Query("SELECT COUNT(r) FROM OfferLoan o JOIN o.requestloans r WHERE o.idOffer = :offerId")
    int countRequestLoansByOfferId(@Param("offerId") Long offerId);



}
