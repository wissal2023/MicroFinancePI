package com.example.microfinancepi.Controllers;


import com.example.microfinancepi.entities.*;
import com.example.microfinancepi.repositories.IEventRepository;
import com.example.microfinancepi.repositories.UserRepository;
import com.example.microfinancepi.services.EmailSenderService;
import com.example.microfinancepi.services.EventService;
import com.example.microfinancepi.services.ShareHolderService;
import com.example.microfinancepi.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.example.microfinancepi.entities.ShareHolder;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@AllArgsConstructor

@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/Event")
public class EventRestController {
    private ShareHolderService shareHolderService;
    private EventService eventService;
    private EmailSenderService emailSenderService;

    private IEventRepository iEventRepository;

    private UserRepository userRepository;
    UserService userService;


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public Event addEvent(@RequestBody Event event, Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();

        return eventService.AddEvent(event, authenticatedUser);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/create-and-assign")
    public ResponseEntity<String> createEventAndAssignUser(@Valid @RequestBody Event event, Authentication authentication) {
        // Vérifier si la date de l'événement est dans le futur ou aujourd'hui
        if (event.getDateEvent().isBefore(LocalDate.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La date de l'événement doit être aujourd'hui ou dans le futur.");
        }

        // Continuer avec la création et l'attribution de l'événement
        User authenticatedUser = (User) authentication.getPrincipal();
        eventService.addEvent(event, authenticatedUser);

        // Retourner une réponse réussie
        return ResponseEntity.ok("L'événement a été créé et attribué avec succès.");
    }


    @GetMapping("/all")
    List<Event> retrieveAllEvents() {

        return eventService.retrieveAllEvents();
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    Event retrieveEvent(@PathVariable("id") Integer IdEvent, Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        return eventService.retrieveEvent(IdEvent, authenticatedUser);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    void RemoveEvent(@PathVariable("id") Integer IdEvent, Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        eventService.removeEvent(IdEvent, authenticatedUser);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    Event updateEvent(@RequestBody Event event, Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        return eventService.updateEvent(event, authenticatedUser);
    }

    @PostMapping("/assignshrtoevent/{eventName}")
    @PreAuthorize("hasAuthority('SHAREHOLDER')")
    public ResponseEntity<String> assignshrtoevent(@RequestBody ShareHolder shareHolder, @PathVariable("eventName") String eventName, Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();

        // Récupérer l'ID de l'événement à partir de son nom
        Integer eventId = eventService.getEventIdByName(eventName);

        if (eventId != null) {
            Event event = eventService.getEventById(eventId);
            // Affecter l'événement au shareholder en utilisant l'ID récupéré
            eventService.assignshrtoevent(eventId, shareHolder, authenticatedUser);

            // Investir dans l'événement
            shareHolderService.investInEvent(shareHolder.getIdShareholder(), eventId);
            if (event.getEventStatus() == EventStatus.COMPLETED) {
                // Si l'événement est terminé, renvoyer un message indiquant que l'investissement n'est pas possible
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot invest in a completed event.");
            }

            // Récupérer l'e-mail du donateur depuis l'entité ShareHolder
            String shareHolderEmail = shareHolder.getEmail();

            // Envoyer l'e-mail à l'adresse du donateur
            String subject = "Thank you for your investment";
            String message = "Dear shareholder, Thank you for your investment in our event " + eventName + ".";
            try {
                emailSenderService.sendEmail(shareHolderEmail, subject, message);
                return ResponseEntity.ok("Shareholder added successfully and email sent.");
            } catch (MessagingException e) {
                // Gérer l'exception si l'envoi d'e-mail échoue
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email to shareholder.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event with name " + eventName + " not found.");
        }
    }

    @GetMapping("/getEventByShareholder/{nom}/{prenom}")
    @PreAuthorize("hasAuthority('ADMIN')")
    List<Event> getEventByShareholder(@PathVariable("nom") String Firstname, @PathVariable("prenom") String Lastname, Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        return eventService.findByShareHolders_LastNameShareholderAndShareHolders_FirstNameShareholder(Lastname, Firstname, authenticatedUser);
    }

    @GetMapping("/{eventId}/totalInvestment")
    public ResponseEntity<Double> getTotalInvestmentInEvent(@PathVariable int eventId) {
        Double totalInvestment = eventService.getTotalInvestmentInEvent(eventId);
        return ResponseEntity.ok(totalInvestment);
    }

    @GetMapping("/getArchive")
    public List<Event> getArchive() {
        return eventService.getArchiveEvent();
    }

    @PreAuthorize("hasAnyAuthority('SHAREHOLDER','CUSTOMER','AGENT','INVESTOR')")
    @PostMapping("/events/{eventName}/like")
    public void likeEvent(@PathVariable String eventName, Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        // Trouvez l'événement correspondant dans la base de données
        Event event = iEventRepository.findByEventName(eventName);

        // Vérifier si l'événement a été trouvé
        if (event == null) {
            throw new RuntimeException("Event not found");
        }

        // Incrémenter le nombre de "likes" pour l'événement
        event.setLikes(event.getLikes() + 1);

        // Enregistrez les modifications de l'événement dans la base de données
        iEventRepository.save(event);
    }

    @PostMapping("/events/{eventName}/dislike")
    @PreAuthorize("hasAnyAuthority('SHAREHOLDER','CUSTOMER','AGENT','INVESTOR')")
    public void dislikeEvent(@PathVariable String eventName, Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        // Trouver l'événement correspondant dans la base de données par son nom
        Event event = iEventRepository.findByEventName(eventName);

        // Vérifier si l'événement a été trouvé
        if (event == null) {
            throw new RuntimeException("Event not found");
        }


        // Incrémenter le nombre de "dislikes" pour l'événement
        event.setDislikes(event.getDislikes() + 1);

        // Enregistrez les modifications de l'événement dans la base de données
        iEventRepository.save(event);
    }

    @PostMapping("/{eventId}/like/{shareholderId}")
    public ResponseEntity<String> voteLike(@PathVariable int eventId, @PathVariable int shareholderId) {
        eventService.voteLike(eventId, shareholderId);
        return ResponseEntity.ok("Event " + eventId + " liked by shareholder " + shareholderId);
    }

    @PostMapping("/{eventId}/dislike/{shareholderId}")
    public ResponseEntity<String> voteDislike(@PathVariable int eventId, @PathVariable int shareholderId) {
        eventService.voteDislike(eventId, shareholderId);
        return ResponseEntity.ok("Event " + eventId + " disliked by shareholder " + shareholderId);
    }

    @GetMapping("/recommend")
    public ResponseEntity<Event> recommendEventByLikes() {
        Event event = eventService.recommendEventByLikes();
        if (event != null) {
            return ResponseEntity.ok(event);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    @PostMapping("/events/{eventId}/cancel")
    public ResponseEntity<String> cancelEvent(@PathVariable int eventId) {
        Optional<Event> optionalEvent = iEventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();

            // Récupérer tous les actionnaires affectés à cet événement
            List<ShareHolder> shareholders = event.getShareHolders();

            // Supprimer toutes les entrées associées dans la table event_user_set
            event.getUserSet().clear();

            // Annuler l'événement
            event.cancelEvent();

            // Enregistrer les modifications de l'événement dans la base de données
            iEventRepository.save(event);

            // Supprimer l'événement de la base de données
            iEventRepository.delete(event);

            // Parcourir tous les actionnaires et leur envoyer un e-mail
            for (ShareHolder shareholder : shareholders) {
                String shareHolderEmail = shareholder.getEmail(); // Supposons que vous avez un getter pour l'e-mail dans la classe Shareholder

                // Envoyer l'e-mail à l'adresse de l'actionnaire
                String subject = "Cancellation of the event";
                String message = "Dear shareholder, the event " + event.getNameEvent() + " has been cancelled.";
                try {
                    emailSenderService.sendEmail(shareHolderEmail, subject, message);
                } catch (MessagingException e) {
                    // Gérer l'exception si l'envoi d'e-mail échoue
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email to shareholder.");
                }
            }

            return ResponseEntity.ok("Event has been cancelled successfully and emails have been sent to shareholders.");
        } else {
            throw new ResourceNotFoundException("Event not found with id " + eventId);
        }
    }

    @PostMapping("/events/{eventId}/reported")
    public ResponseEntity<String> reportedEvent(@PathVariable int eventId) {
        Optional<Event> optionalEvent = iEventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();

            // Annuler l'événement
            event.cancelEvent();

            // Reporter l'événement de 7 jours
            event.setDateEvent(event.getDateEvent().plusDays(7));

            // Enregistrer les modifications de l'événement dans la base de données
            iEventRepository.save(event);

            // Récupérer les actionnaires associés à l'événement
            List<ShareHolder> shareholders = event.getShareHolders();

            // Envoyer un e-mail à chaque actionnaire
            String subject = "Event Report";
            String message = "Dear Shareholder,\n\nThe event " + event.getNameEvent() + " has been reported and will now take place on " + event.getDateEvent() + ".\n\nKind regards,\nThe MAWALNY Team";
            for (ShareHolder shareholder : shareholders) {
                try {
                    emailSenderService.sendEmail(shareholder.getEmail(), subject, message);
                } catch (MessagingException e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email to shareholders.");
                }
            }

            return ResponseEntity.ok("Event has been reported successfully");
        } else {
            throw new ResourceNotFoundException("Event not found with id " + eventId);
        }
    }

    @GetMapping("/historiquedesEvent/{iduser}")
    public ResponseEntity<List<Event>> historiqueEvent(@PathVariable("iduser") int userid) {
        User user = userRepository.findById(userid).orElse(null);
        List<Event> offers = eventService.historiqueEvent(userid);
        String Text1 = "l'historique des offres de l'utilisateur " + user.getUser_firstname() + "-" + user.getUser_lastname();
        return new ResponseEntity<List<Event>>(offers, HttpStatus.OK);

    }

    @GetMapping("/events/{id}/indice-rentabilite")
    public ResponseEntity<Double> calculIndiceRentabilite(@PathVariable("id") int idEvent) {
        double indiceRentabilite = eventService.calculIndiceRentabilite(idEvent);
        return ResponseEntity.ok(indiceRentabilite);
    }

    @GetMapping("/eventinfo")
    public ResponseEntity<String> eventInfo() {
        // Récupérer les données à afficher
        Event mostFrequent = eventService.findMostFrequentEvent();
        Event lessFrequent = eventService.findLessFrequentEvent();
        List<Event> eventsWithoutShareholders = eventService.findEventsWithoutShareholders();
        Long nbEventsWithShareholders = eventService.countEventsWithAtLeastOneShareholder();
        double percentageWithoutShareholders = eventService.getEventsShareholdersPercentages();
        double percentageWithShareholders = eventService.getEventsShareholdersPercentages1();

        // Créer les textes à afficher
        String texte1 = "L'événement le plus fréquent est : ";
        String texte2 = "L'événement le moins fréquent est : ";
        String texte3 = "Les événements qui n'ont aucun partenaire : ";
        String texte4 = "Le nombre d'événements ayant au moins un Shareholder : ";
        String texte5 = "Selon les statistiques, nous pouvons déduire que : ";
        String texte6 = "% des événements n'ont pas de Shareholders";
        String texte7 = "% des événements ont des Shareholders";

        // Créer la réponse avec les données et les textes
        ResponseEntity<String> response = ResponseEntity.ok()
                .header("Custom-Header", "valeur-personnalisee")
                .body(texte1 + mostFrequent.getNameEvent() + "\n\n" + texte2 + lessFrequent.getNameEvent() + "\n\n"
                        + texte3 + eventsWithoutShareholders + "\n\n" + texte4 + nbEventsWithShareholders + "\n\n" + texte5
                        + "\n\n" + percentageWithoutShareholders + texte6 + "\n\n" + percentageWithShareholders + texte7);

        return response;
    }

    @GetMapping("/shareholders/{type}/{investmentAmount}/interest-rate")
    public double getInterestRateForShareholder(@PathVariable TypeShareholder type, @PathVariable double investmentAmount) {
        return shareHolderService.calculateInterestRateForShareholder(investmentAmount, type);
    }
}


