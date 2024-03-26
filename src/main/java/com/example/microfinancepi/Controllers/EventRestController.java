package com.example.microfinancepi.Controllers;


import com.example.microfinancepi.entities.*;
import com.example.microfinancepi.repositories.IEventRepository;
import com.example.microfinancepi.repositories.UserRepository;
import com.example.microfinancepi.services.EmailSenderService;
import com.example.microfinancepi.services.EventService;
import com.example.microfinancepi.services.ShareHolderService;
import com.example.microfinancepi.services.ShareHolderServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/Event")
public class EventRestController {
    private ShareHolderService shareHolderService;
    private EventService eventService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private IEventRepository iEventRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    Event addEvent(@RequestBody Event event){
        return eventService.AddEvent(event);
    }
    @GetMapping("/all")
    List<Event> retrieveAllEvents(){

        return eventService.retrieveAllEvents();
    }
    @GetMapping("/get/{id}")
    Event retrieveEvent(@PathVariable("id") Integer IdEvent){
        return eventService.retrieveEvent(IdEvent);
    }
    @DeleteMapping("/delete/{id}")
    void RemoveEvent(@PathVariable("id") Integer IdEvent){
        eventService.removeEvent(IdEvent);
    }
    @PutMapping ("/update")
    Event updateEvent(@RequestBody Event event){
        return eventService.updateEvent(event);
    }
    @GetMapping("/getEventByShareholder/{nom}/{prenom}")
    List<Event> getEventByShareholder(@PathVariable("nom") String Firstname,@PathVariable("prenom") String Lastname) {
        return eventService.findByShareHolders_LastNameShareholderAndShareHolders_FirstNameShareholder(Lastname, Firstname);
    }

    @GetMapping("/{eventId}/totalInvestment")
    public ResponseEntity<Double> getTotalInvestmentInEvent(@PathVariable int eventId) {
        Double totalInvestment = eventService.getTotalInvestmentInEvent(eventId);
        return ResponseEntity.ok(totalInvestment);
    }
    /* @PostMapping("/{eventId}/send-reminder-emails")
     public ResponseEntity<?> sendEventReminderEmails(@PathVariable int eventId) {
         eventService.sendEventReminderEmail(eventId);
         return ResponseEntity.ok().build();
     }*/
   // @Scheduled(fixedRate = 60 * 60 * 1000) // exécute toutes les heures
    // @Scheduled(cron="*/30 * * * * *")
   /* public void sendEventReminderEmails() {
        List<Event> events = eventService.getEventsWithin24Hours();
        for (Event event : events) {
            emailSenderService.sendSimpleEmail("samar.saidana@esprit.tn"," Bonjour Ceci est un rappel pour l'événement "+ event.getNameEvent() +"  qui aura lieu dans 24 heures." +"\n\n"+"Cordialement,L'équipe de microfinance.","reminder event "+event.getNameEvent());
        }
    }*/
    @GetMapping("/getEventByUser/{id}")
    @Transactional
    public List<Event> ShowEventByActivitySector(@PathVariable("id") Integer iduser) {
        return eventService.ShowEventByActivitySector(iduser);
    }
    @GetMapping("/getArchive")
    public List<Event> getArchive(){
        return eventService.getArchiveEvent();
    }
    @PostMapping("/events/{eventId}/like")
    public void likeEvent(@PathVariable int eventId) {
        // Trouvez l'événement correspondant dans la base de données
        Event event = iEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Incrémenter le nombre de "likes" pour l'événement
        event.setLikes(event.getLikes() + 1);

        // Enregistrez les modifications de l'événement dans la base de données
        iEventRepository.save(event);
    }
    @PostMapping("/events/{eventId}/dislike")
    public void dislikeEvent(@PathVariable int eventId) {
        // Trouvez l'événement correspondant dans la base de données
        Event event = iEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

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
    public class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }
    @PostMapping("/events/{eventId}/cancel")
    public ResponseEntity<String> cancelEvent(@PathVariable int eventId) {
        Optional<Event> optionalEvent = iEventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            event.cancelEvent();
            iEventRepository.delete(event);
            //emailSenderService.sendSimpleEmail("samar.saidana@esprit.tn"," Bonjour Ceci est une annulation  pour l'événement "+ event.getNameEvent() +"  qui aura lieu le "+ event.getDateEvent() +"\n\n"+"Cordialement,L'équipe de microfinance.","reminder event "+event.getNameEvent());
            return ResponseEntity.ok("Event has been cancelled successfully");
        } else {
            throw new ResourceNotFoundException("Event not found with id " + eventId);
        }
    }
    @PostMapping("/events/{eventId}/reported")
    public ResponseEntity<String> reportedEvent(@PathVariable int eventId) {
        Optional<Event> optionalEvent = iEventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            event.cancelEvent();
            event.setDateEvent(event.getDateEvent().plusDays(10));
            iEventRepository.save(event);
            //emailSenderService.sendSimpleEmail("samar.saidana@esprit.tn"," Bonjour Ceci est une reportation  pour l'événement  "+ event.getNameEvent() +"  qui sera reporte pour le "+ event.getDateEvent() +"\n\n"+"Cordialement,L'équipe de microfinance.","reminder event "+event.getNameEvent());
            return ResponseEntity.ok("Event has been reported successfully");
        } else {
            throw new ResourceNotFoundException("Event not found with id " + eventId);
        }
    }
    @PutMapping("/assignEventtTouser/{idevent}/{iduser}")
    Event assignUsertoEvent(@PathVariable("idevent") Integer idevent,@PathVariable("iduser") Integer idIUser){
        return eventService.assignUserToEvent(idevent,idIUser);
    }

    @GetMapping("/historiquedesEvent/{iduser}")
    public ResponseEntity<List<Event>>  historiqueEvent(@PathVariable("iduser") int userid){
        User user= userRepository.findById(userid).orElse(null);
        List<Event> offers= eventService.historiqueEvent(userid);
        String Text1="l'historique des offres de l'utilisateur "+ user.getUser_firstname()+"-"+user.getUser_lastname();
        return  new ResponseEntity<List<Event>>(offers, HttpStatus.OK);

    }
    @GetMapping("/events/{id}/indice-rentabilite")
    public ResponseEntity<Double> calculIndiceRentabilite(@PathVariable("id") int idEvent) {
        double indiceRentabilite = eventService.calculIndiceRentabilite(idEvent);
        return ResponseEntity.ok(indiceRentabilite);
    }
   /* @GetMapping("/events/{id}/valeurActuelleNetteAjustee")
    public ResponseEntity<Double> calculerValeurActuelleNetteAjustee(@PathVariable int id, @RequestParam double tauxRendementExige) {
        double valeurActuelleNetteAjustee = eventService.calculValeurActuelleNetteAjustee(id, tauxRendementExige);
        return ResponseEntity.ok(valeurActuelleNetteAjustee);
    }*/


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

}


