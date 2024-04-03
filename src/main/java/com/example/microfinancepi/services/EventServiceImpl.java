package com.example.microfinancepi.services;

import com.example.microfinancepi.entities.*;
import com.example.microfinancepi.repositories.IEventRepository;
import com.example.microfinancepi.repositories.IShareholderRepository;
import com.example.microfinancepi.repositories.UserRepository;
import lombok.AllArgsConstructor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    IEventRepository Ieventrepository;
    IShareholderRepository iShareholderRepository;
   UserRepository userRepository;
   ShareHolderService shareHolderService;
   EmailSenderService emailSenderService;




    @Override
    public List<Event> retrieveAllEvents() {
        return Ieventrepository.findAll();
    }

    @Override
    public Event AddEvent(Event event, User authentication) {

        User_role userRole = getCurrentUserRole(authentication);

        if (!userRole.equals(User_role.ADMIN)) {
            throw new AccessDeniedException("you're not authorized");
        }
        Set<User> userSet= new HashSet<>();
        event.setUserSet(userSet);
        return Ieventrepository.save(event);
    }
    public void addEvent(Event event, User authentication) {
        User_role userRole = getCurrentUserRole(authentication);
        if (!userRole.equals(User_role.ADMIN)) {
            throw new AccessDeniedException("You're not authorized");
        } else {
            if (event == null) {
                throw new IllegalArgumentException("Invalid input parameter: event is null");
            }

            // Get the current user's ID from security context
            String currentUserId = getCurrentUserIdFromSecurityContext();

            // Find the user
            User user = userRepository.findUserByEmail(currentUserId)
                    .orElseThrow(() -> new IllegalArgumentException("Current user not found"));

            event.getUserSet().add(user);

            // Save the event
            Event savedEvent = Ieventrepository.save(event);
        }
    }

    @Override
    public void removeEvent(Integer numEvent,User authentication) {

        User_role userRole = getCurrentUserRole(authentication);
        if (!userRole.equals(User_role.ADMIN)) {
            throw new AccessDeniedException("You're not authorized");
        } else {
            Ieventrepository.deleteById(numEvent);
        }
    }

    @Override
    public Event retrieveEvent(Integer numEvent, User authentication) {
        User_role userRole = getCurrentUserRole(authentication);

        if (!userRole.equals(User_role.ADMIN)) {
            throw new AccessDeniedException("you're not authorized");
        }
        return Ieventrepository.findById(numEvent).orElse(null);
    }

    @Override
    public Event updateEvent(Event event,User authentication) {
        User_role userRole = getCurrentUserRole(authentication);

        if (!userRole.equals(User_role.ADMIN)) {
            throw new AccessDeniedException("you're not authorized");
        }
        return Ieventrepository.save(event);
    }

    @Override
    @Transactional
    public List<Event> findByShareHolders_LastNameShareholderAndShareHolders_FirstNameShareholder(String lastNameShareholder, String firstNameShareholder,User authentication) {
        User_role userRole = getCurrentUserRole(authentication);

        if (!userRole.equals(User_role.ADMIN)) {
            throw new AccessDeniedException("you're not authorized");
        }

        return Ieventrepository.findByShareHolders_LastNameShareholderAndShareHolders_FirstNameShareholder(lastNameShareholder, firstNameShareholder);
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
    public Event getEventById(Integer EventId) {
        return Ieventrepository.findById(EventId).orElse(null);
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
    public List<Event> historiqueEvent(int idUser) {

        return Ieventrepository.findByUsers_Id(idUser);
    }

    private User_role getCurrentUserRole(User user) {
        return user.getRole();
    }



    private String getCurrentUserIdFromSecurityContext() {
        // Get the current authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the authentication object is not null and if the principal is an instance of UserDetails
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            // Cast the principal to UserDetails
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Return the username, which could represent the user's ID
            return userDetails.getUsername();
        } else {
            // If the authentication object is null or the principal is not an instance of UserDetails,
            return null;
        }
    }

    @Override
    public double calculIndiceRentabilite(int idEvent) {
        List<ShareHolder> shareHolders = iShareholderRepository.findByEvent_IdEvent(idEvent);

        double sommeInvestissement = 0;
        double sommeActualisee = 0;
        double tauxRendementExige = 10; // taux de rendement exigé de l'investisseur

        for (ShareHolder shareHolder : shareHolders) {
            if (shareHolder.getEvent() == null || shareHolder.getEvent().getDateEvent() == null) {
                // Ignorer cet actionnaire si la date de l'événement est nulle
                continue;
            }

            sommeInvestissement += shareHolder.getInvestment();

            LocalDate dateEvent = shareHolder.getEvent().getDateEvent();
            LocalDate now = LocalDate.now();
            int yearsInvestment = now.getYear() - dateEvent.getYear();
            double actualisation = 1 / Math.pow(1 + tauxRendementExige, yearsInvestment);
            double valeurActualisee = shareHolder.getInvestment() * actualisation;
            sommeActualisee += valeurActualisee;
        }

        // Assurez-vous que sommeInvestissement n'est pas égal à zéro pour éviter une division par zéro
        if (sommeInvestissement != 0) {
            // Calcul de l'indice de rentabilité
            double indiceRentabilite = sommeActualisee / sommeInvestissement;
            return indiceRentabilite;
        } else {
            // Gérer le cas où sommeInvestissement est égal à zéro
            // Par exemple, vous pouvez renvoyer une valeur par défaut ou lancer une exception
            return 0; // Valeur par défaut
        }
    }

    @Override
    public void assignshrtoevent(Integer EventId, ShareHolder shareHolder, User authentication) {
        User_role userRole = getCurrentUserRole(authentication);
        if (!userRole.equals(User_role.SHAREHOLDER)) {
            throw new AccessDeniedException("You're not authorized");
        } else {
        Event event = getEventById(EventId);

        if (event != null) {
            shareHolder.setEvent(event);
            shareHolderService.saveShareHolder(shareHolder);
        } else {
            throw new EntityNotFoundException("Event with ID " + EventId + " not found.");
        }
    }}
    public Integer getEventIdByName(String eventName) {
        Event event = Ieventrepository.findByEventName(eventName);
        if (event != null) {
            return event.getIdEvent(); // Retourne l'ID de l'événement si trouvé
        } else {
            return null; // Retourne null si l'événement n'est pas trouvé
        }
    }
    @Override
    public String getRecommendationResponse() {
        Event recommendedEvent = recommendEventByLikes();
        if (recommendedEvent != null) {
            return "The most recommended event is : " + recommendedEvent.getNameEvent();
        } else {
            return null;
        }
    }

    @Override

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
    public List<Event> getEventsInTwoDays() {
        LocalDate today = LocalDate.now();
        LocalDate twoDaysLater = today.plusDays(2);

        List<Event> allEvents = Ieventrepository.findAll();
        List<Event> eventsInTwoDays = allEvents.stream()
                .filter(event -> event.getDateEvent().isAfter(today) && event.getDateEvent().isBefore(twoDaysLater))
                .collect(Collectors.toList());

        return eventsInTwoDays;
    }
   // @Scheduled(fixedDelay = 10000)
    public void sendEventReminders() {
        // Récupérer la liste des événements prévus dans deux jours
        List<Event> events = getEventsInTwoDays();
        log.info("nombre d'event : " + events.size());
        // Pour chaque événement, envoyer un rappel aux actionnaires
        for (Event event : events) {
            log.info(event.getNameEvent());
            List<ShareHolder> shareholders = event.getShareHolders();
            for (ShareHolder shareholder : shareholders) {
                String email = shareholder.getEmail();
                String subject = "Rappel: Événement dans deux jours";
                String message = "Cher " + shareholder.getFirstNameShareholder() + ",\n\n"
                        + "Ceci est un rappel que l'événement '" + event.getNameEvent() + "' se tiendra dans deux jours.\n\n"
                        + "Date de l'événement: " + event.getDateEvent() + "\n"
                        //   + "Lieu: " + event.getLocation() + "\n\n"
                        + "Cordialement,\n"
                        + "MAWALNY";

                // Envoyer l'e-mail de rappel à l'actionnaire
                try {
                    emailSenderService.sendEmail(email, subject, message);
                } catch (MessagingException e) {
                    // Gérer l'exception
                    e.printStackTrace();
                    // Vous pouvez enregistrer les détails de l'erreur ou prendre une autre action appropriée
                }
            }
        }
    }}



