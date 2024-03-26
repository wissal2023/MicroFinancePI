package com.example.microfinancepi.repositories;

import com.example.microfinancepi.entities.RequestLoan;
import com.example.microfinancepi.entities.StatLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestLoanRepository extends JpaRepository<RequestLoan,Long> {
    List<RequestLoan> findByStatus(StatLoan status);

    /*

     @Query("SELECT r FROM Request r JOIN r.offer o WHERE o.id_offer = :id_offer")
    List<Request> findRequestsByOfferId(@Param("id_offer") Integer id_offer);

   @Query("SELECT r FROM Request r JOIN r.user u WHERE u.id_user= :id_user" )
    List<Request> findRequestByUser(@Param("id_user") Integer id_user);

    // @Query("SELECT r FROM Request r WHERE r.offer IS NOT NULL AND r.user IS NOT NULL")
    //List<Request> hazem();
 //  @Query  ("select '*' from Request r where r.offer.id_offer= :id_offer")
  //List<Request> findRequestByOffer(@Param("id_offer") Offers_Credit id_offer);



     */
}
