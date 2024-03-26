package com.example.microfinancepi.services;

import com.example.microfinancepi.entities.Event;
import com.example.microfinancepi.entities.ShareHolder;

import java.util.List;

public interface EventService {
    List<Event> retrieveAllEvents();

    Event AddEvent(Event event);

    void removeEvent(Integer numEvent);

    Event retrieveEvent(Integer numEvent);

    Event updateEvent(Event event);

    List<Event> findByShareHolders_LastNameShareholderAndShareHolders_FirstNameShareholder(String lastNameShareholder, String FirstNameShareholder);

    Double getTotalInvestmentInEvent(int eventId);
    List<Event> ShowEventByActivitySector(Integer iduser) ;
    List<Event> getEventsWithin24Hours();
     List<Event> getArchiveEvent();
    void voteLike(int eventId, int shareholderId);
    void voteDislike(int eventId, int shareholderId);
    //int getEventYear(ShareHolder shareHolder);
    double calculIndiceRentabilite(int idEvent);
     //double calculIndiceRentabilite(int idEvent, double tauxRendementExige);
    Event assignUserToEvent(int idUser,int idEvent);
    List<Event> historiqueEvent(int idUser);
     //double calculValeurActuelleNetteAjustee(int idEvent, double tauxRendementExige);
     Event recommendEventByLikes();

    Event findMostFrequentEvent();
    Event findLessFrequentEvent();
    List<Event> findEventsWithoutShareholders();
    Long countEventsWithAtLeastOneShareholder();
    double getEventsShareholdersPercentages();
    double getEventsShareholdersPercentages1();
}