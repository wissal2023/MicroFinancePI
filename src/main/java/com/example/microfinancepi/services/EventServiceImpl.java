package com.example.microfinancepi.services;

import com.example.microfinancepi.entities.*;
import com.example.microfinancepi.repositories.IEventRepository;
import com.example.microfinancepi.repositories.IShareholderRepository;
import com.example.microfinancepi.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private IEventRepository Ieventrepository;
    private final IShareholderRepository iShareholderRepository;
    private final UserRepository userRepository;


    @Override
    public List<Event> retrieveAllEvents() {
        return Ieventrepository.findAll();
    }

    @Override
    public Event AddEvent(Event event) {
        return Ieventrepository.save(event);
    }


    @Override
    public void removeEvent(Integer numEvent) {
        Ieventrepository.deleteById(numEvent);
    }

    @Override
    public Event retrieveEvent(Integer numEvent) {
        return Ieventrepository.findById(numEvent).orElse(null);
    }

    @Override
    public Event updateEvent(Event event) {
        return Ieventrepository.save(event);
    }

    @Override
    @Transactional
    public List<Event> findByShareHolders_LastNameShareholderAndShareHolders_FirstNameShareholder(String lastNameShareholder, String FirstNameShareholder) {

        return Ieventrepository.findByShareHolders_LastNameShareholderAndShareHolders_FirstNameShareholder(lastNameShareholder, FirstNameShareholder);
    }

    @Override
    public Double getTotalInvestmentInEvent(int eventId) {
        Optional<Event> eventOptional = Ieventrepository.findById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            List<ShareHolder> shareholders = event.getShareHolders();
            double totalInvestment = 0.0;
            for (ShareHolder shareholder : shareholders) {
                totalInvestment += shareholder.getInvestment();
            }
            return totalInvestment;
        } else {
            throw new EntityNotFoundException("Event not found with id " + eventId);
        }
    }

    @Override
    public List<Event> ShowEventByActivitySector(Integer iduser) {
        User user= userRepository.findById(iduser).get();
        return Ieventrepository.findByActivitySector(user.getSector_activite());

    }


    @Override
    public List<Event> getEventsWithin24Hours() {
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = now.plusDays(1);
        return Ieventrepository.findByEventDateBetween(now, tomorrow);
    }
    @Override
    public List<Event> getArchiveEvent(){
        LocalDate now =LocalDate.now();
        List<Event> eventArchive= new ArrayList<>();
        List<Event> eventList=Ieventrepository.findAll();
        for (Event event : eventList){
            if(event.getDateEvent().isBefore(now)){
                eventArchive.add(event);
            }
        }
        return eventArchive;
    }
    public void voteLike(int eventId, int shareholderId) {
        Event event = Ieventrepository.findById(eventId).orElse(null);
        if (event != null) {
            ShareHolder shareholder = event.getShareHolders()
                    .stream()
                    .filter(s -> s.getIdShareholder() == shareholderId)
                    .findFirst()
                    .orElse(null);
            if (shareholder != null) {
                event.addLike();
                Ieventrepository.save(event);
            }
        }
    }
    public void voteDislike(int eventId, int shareholderId) {
        Event event = Ieventrepository.findById(eventId).orElse(null);
        if (event != null) {
            ShareHolder shareholder = event.getShareHolders()
                    .stream()
                    .filter(s -> s.getIdShareholder() == shareholderId)
                    .findFirst()
                    .orElse(null);
            if (shareholder != null) {
                event.addDislike();
                Ieventrepository.save(event);
            }
        }
    }



    @Override
    public Event assignUserToEvent(int idUser, int idEvent) {
        Event event=Ieventrepository.findById(idEvent).orElse(null);
        User user=userRepository.findById(idUser).orElse(null);
        if(event.getUserSet()==null){
            Set<User> userSet= new HashSet<>();
            userSet.add(user);
            event.setUserSet(userSet);
        }
        else{
            event.getUserSet().add(user);
        }
        return  Ieventrepository.save(event);

    }

    @Override
    public List<Event> historiqueEvent(int idUser) {

        return Ieventrepository.findByUsers_Id(idUser);
    }

    @Override
    public double calculIndiceRentabilite(int idEvent) {
        List<ShareHolder> shareHolders = iShareholderRepository.findByEvent_IdEvent(idEvent);

        double sommeInvestissement = 0;
        double sommeActualisee = 0;
        double tauxRendementExige = 10; // taux de rendement exigé de l'investisseur

        for (ShareHolder shareHolder : shareHolders) {
            sommeInvestissement += shareHolder.getInvestment();

            // Calcul de la valeur actualisée nette de chaque actionnaire
            LocalDate dateEvent = shareHolder.getEvent().getDateEvent();
            LocalDate now = LocalDate.now();
            int yearsInvestment = now.getYear() - dateEvent.getYear();
            double actualisation = 1 / Math.pow(1 + tauxRendementExige, yearsInvestment);
            double valeurActualisee = shareHolder.getInvestment() * actualisation;
            sommeActualisee += valeurActualisee;
        }

        // Calcul de l'indice de rentabilité
        double indiceRentabilite = sommeActualisee / sommeInvestissement;
        return indiceRentabilite;
    }


  /* public double calculValeurActuelleNetteAjustee(int idEvent, double tauxRendementExige) {
        Event event = Ieventrepository.findById(idEvent).orElse(null);
        double sommeInvestissement = 0;
        double sommeActualisee = 0;
        for (ShareHolder shareHolder : event.getShareHolders()) {
            sommeInvestissement += shareHolder.getInvestment();

            // Calcul de la valeur actualisée nette de chaque actionnaire
            LocalDate dateEvent = shareHolder.getEvent().getDateEvent();
            LocalDate now = LocalDate.now();
            int yearsInvestment = now.getYear() - dateEvent.getYear();
            double actualisation = 1 / Math.pow(1 + tauxRendementExige, yearsInvestment);
            double valeurActualisee = shareHolder.getInvestment() * actualisation;
            sommeActualisee += valeurActualisee;
        }

        // Calcul de la valeur actuelle nette ajustée
        double valeurActuelleNetteAjustee = sommeActualisee - sommeInvestissement;
        /*if (valeurActuelleNetteAjustee < 0) {
            valeurActuelleNetteAjustee = 0;
        }
        return valeurActuelleNetteAjustee;
    }*/
  @Autowired

    // Méthode de recommandation pour l'événement avec le plus de likes
    public Event recommendEventByLikes() {
      // Requête JPA pour trier les événements par likes décroissants
      List<Event> events = Ieventrepository.findAllByOrderByLikesDesc();

      // Vérifie s'il y a des événements dans la liste triée et renvoie le premier événement s'il y en a un
      if (!events.isEmpty()) {
          return events.get(0);
      } else {
          return null;
      }
  }

    public Event findMostFrequentEvent() {
        List<Event> EventList = Ieventrepository.findMostFrequentEvent(TypeEvent.MEETING);
        if (!EventList.isEmpty()) {
            return EventList.get(0);
        } else {
            return null;
        }
    }

    public Event findLessFrequentEvent() {
        List<Event> EventList = Ieventrepository.findLessFrequentEvent(TypeEvent.MEETING);
        if (!EventList.isEmpty()) {
            return EventList.get(0);
        } else {
            return null; // retourne null si la liste est vide
        }
    }
    public List<Event> findEventsWithoutShareholders() {
        return Ieventrepository.findEventsWithoutShareholders();
    }

    public Long countEventsWithAtLeastOneShareholder() {
        return Ieventrepository.countEventsWithAtLeastOneShareholder();
    }

    public double getEventsShareholdersPercentages() {
        Long nbEventsWithoutShareholders = Ieventrepository.countEventsWithoutShareholders();
        Long totalEvents = Ieventrepository.count();
        double percentageWithoutShareholders = (double) nbEventsWithoutShareholders / totalEvents * 100.0;
        return percentageWithoutShareholders;
    }

    public double getEventsShareholdersPercentages1() {
        Long nbEventsWithShareholders = Ieventrepository.countEventsWithAtLeastOneShareholder();
        Long totalEvents = Ieventrepository.count();
        double percentageWithShareholders = (double) nbEventsWithShareholders / totalEvents * 100.0;
        return percentageWithShareholders;
    }

}