package com.example.microfinancepi.repositories;

import com.example.microfinancepi.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IEventRepository extends JpaRepository<Event,Integer> {
    @Query("select e from Event e inner join e.shareHolders shareHolders where shareHolders.lastNameShareholder = ?1 and shareHolders.FirstNameShareholder = ?2")
    List<Event> findByShareHolders_LastNameShareholderAndShareHolders_FirstNameShareholder(String lastNameShareholder, String FirstNameShareholder);

    @Query("SELECT SUM(s.investment) FROM ShareHolder s WHERE s.event.idEvent = :eventId")
    Double findByToltalInvestmentEvent(@Param("eventId") int eventId);

    @Query("select i from Event i where i.dateEvent between ?1 and ?2")
    List<Event> findByEventDateBetween(LocalDate date1, LocalDate date2);

    @Query("select i from Event i where i.domain = ?1")
    List<Event> findByActivitySector(String ActivitySector);

    @Query("select o from Event o inner join o.userSet users where users.id_user = ?1")
    List<Event> findByUsers_Id(int id);
    // List<Event> findAll(); // retourne une liste de tous les événements

    List<Event> findAllByOrderByLikesDesc();
    @Query("SELECT e FROM Event e WHERE e.type = :type ORDER BY SIZE(e.shareHolders) DESC")
    List<Event> findMostFrequentEvent(@Param("type") TypeEvent typeEvent);
    @Query("SELECT s FROM Event s WHERE s.type = :type ORDER BY SIZE(s.shareHolders) ASC")
    List<Event> findLessFrequentEvent(@Param("type") TypeEvent typeEvent);
    @Query("SELECT e FROM Event e WHERE e.shareHolders IS EMPTY")
    List<Event> findEventsWithoutShareholders();

    @Query("SELECT COUNT(e) FROM Event e WHERE e.shareHolders IS NOT EMPTY")
    Long countEventsWithAtLeastOneShareholder();

   @Query("SELECT COUNT(e) FROM Event e WHERE e.shareHolders IS EMPTY")
    Long countEventsWithoutShareholders();

  /*  @Query("SELECT e FROM Event e ORDER BY e.shareHolders.size DESC")
    Event findMostFrequentEvent();

    @Query("SELECT e FROM Event e ORDER BY e.shareHolders.size ASC")
    Event findLessFrequentEvent();*/
  //List<ShareHolder> findByEvent_IdEvent(int idEvent);


}



