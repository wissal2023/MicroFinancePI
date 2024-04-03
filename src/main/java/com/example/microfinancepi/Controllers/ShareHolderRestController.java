package com.example.microfinancepi.Controllers;

import com.example.microfinancepi.entities.*;
import com.example.microfinancepi.repositories.IEventRepository;
import com.example.microfinancepi.repositories.IShareholderRepository;
import com.example.microfinancepi.services.EmailSenderService;
import com.example.microfinancepi.services.EventService;
import com.example.microfinancepi.services.ShareHolderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import javax.mail.MessagingException;
@SecurityRequirement(name = "bearerAuth")


@RestController
@AllArgsConstructor
@RequestMapping("/ShareHolder")

public class ShareHolderRestController {
    private ShareHolderService shareholderservice;
    private EventService eventService;
    private IShareholderRepository Ishareholderrepository;
   // private IEventRepository Ieventrepository;
    EmailSenderService emailSenderService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SHAREHOLDER')")
    ShareHolder addShareHolder(@RequestBody ShareHolder shareholder, Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        return shareholderservice.AddShareHolder(shareholder,authenticatedUser);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    List<ShareHolder> retrieveAllShareHolder( Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        return shareholderservice.retrieveAllShareHolder(authenticatedUser);
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    ShareHolder retrieveShareHolder(@PathVariable("id") Integer IdShareHolder,Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        return shareholderservice.retrieveShareHolder(IdShareHolder,authenticatedUser);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SHAREHOLDER')")
    void RemoveShareHolder(@PathVariable("id") Integer IdShareHolder,Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        shareholderservice.removeShareHolder(IdShareHolder,authenticatedUser);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('SHAREHOLDER')")
    ShareHolder updateShareHolder(@RequestBody ShareHolder shareHolder,Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        return shareholderservice.updateShareHolder(shareHolder,authenticatedUser);
    }


    @PutMapping("/assignshrtoevent/{idShareHolder}/{idEvent}")
    public ShareHolder assignshrstoevent(@PathVariable("idShareHolder") Integer idShareHolder, @PathVariable("idEvent") Integer idEvent) {
        return shareholderservice.assignShareHoldersToEvent(idShareHolder, idEvent);
    }



    @GetMapping("/shareholders/most-participated")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getShareholdersParticipatedInMostEvents(Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        List<ShareHolder> shareholders = shareholderservice.findShareholdersWithMoreThanOneEvent(authenticatedUser);
        if (shareholders.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(shareholders);
        }
    }
    @GetMapping("/shareholders/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ShareHolder> getShareHolder(@PathVariable int id,Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        ShareHolder shareHolder = Ishareholderrepository.findById(id).orElseThrow(() -> new RuntimeException("ShareHolder not found"));
        int year = shareholderservice.getEventYear(shareHolder,authenticatedUser);
        // ...
        return ResponseEntity.ok(shareHolder);
    }
    @GetMapping("/partnerinfo")
    public ResponseEntity<String> partnerInfo() {
        // Récupérer les données à afficher
        ShareHolder mostFrequent = shareholderservice.findMostFrequentPartner();
        ShareHolder lessFrequent = shareholderservice.findLessFrequentPartner();
        List<ShareHolder> partnersWithoutEvents = shareholderservice.findPartnersWithoutEvents();
        Long nbPartnerWithEvent = shareholderservice.countPartnersWithEvents();
        double percentageWithoutEvent = shareholderservice.getPartnersEventPercentages();
        double percentageWithEvent = shareholderservice.getPartnersEventPercentages1();

        if (mostFrequent != null && lessFrequent != null) {   // Créer les textes à afficher
        String texte1 = "Le partner le plus fréquent est : ";
        String texte2 = "Le partner le moins fréquent est : ";
        String texte3 = "Les partners qui n'ont pas participé à d'événement : ";
        String texte4 = "Le nombre de partenaires ayant participé à au moins un événement : ";
        String texte5 = "Selon les statistiques, nous pouvons déduire que : ";
        String texte6 = "% des partenaires n'ont participé à aucun événement";
        String texte7 = "% des partenaires ont participé à des événements";

        // Créer la réponse avec les données et les textes
        ResponseEntity<String> response = ResponseEntity.ok()
                .header("Custom-Header", "valeur-personnalisee")
                .body(texte1 + mostFrequent.getIdShareholder() + "\n\n" + texte2 + lessFrequent.getIdShareholder() + "\n\n"
                        + texte3 + partnersWithoutEvents + "\n\n" + texte4 + nbPartnerWithEvent + "\n\n" + texte5
                        + "\n\n" + percentageWithoutEvent + texte6 + "\n\n" + percentageWithEvent + texte7);

        return response;
    } else {
        // Gérer le cas où mostFrequent ou lessFrequent est null
        // Vous pouvez retourner une réponse différente ou lancer une exception, selon vos besoins
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun partenaire trouvé");
    }
    }




    @GetMapping("/calculTri/{idPartenaire}/{idEvent}")
    public double calculerTRI(@PathVariable("idPartenaire") int idPartenaire, @PathVariable("idEvent") int idEvent) {
        ShareHolder partenaire = Ishareholderrepository.findById(idPartenaire).orElse(null);
       // Event event = Ieventrepository.findById(idEvent).orElse(null);
        double revenu =eventService.getTotalInvestmentInEvent(idEvent);
        double[] cashFlows = { -partenaire.getInvestment(),revenu };
        // cashFlows[0] représente le coût initial (investissement)
        // cashFlows[1] représente les revenus générés par l'événement

        double TRI = irr(cashFlows);
        return TRI;
    }

    private double irr(double[] cashFlows) {
        double x0 = 0.1; // Valeur initiale de la recherche
        double tolerance = 0.00001; // Tolérance de la recherche
        double x1 = x0;
        double fx = 0;
        double dfx = 0;
        double error = 1.0;
        int maxIterations = 100;
        int i = 0;

        while (error > tolerance && i < maxIterations) {
            fx = 0;
            dfx = 0;

            for (int j = 0; j < cashFlows.length; j++) {
                fx += cashFlows[j] / Math.pow(1 + x1, j);
                dfx -= j * cashFlows[j] / Math.pow(1 + x1, j + 1);
            }

            double x2 = x1 - fx / dfx;
            error = Math.abs((x2 - x1) / x2);
            x1 = x2;
            i++;
        }

        return x1; }
    @GetMapping("/calculrendement/{id}/{taux}")
    public double calculerRendement(@PathVariable("id") int idpartenaire,@PathVariable("taux") double taux) {
        ShareHolder shareholder = Ishareholderrepository.findById(idpartenaire).orElse(null);
        double rendement = shareholder.getInvestment() * taux;
        return rendement;
    }

@GetMapping("/evaluerRisque/{idshareholder}/{duree}")
public ResponseEntity<String> evaluerRisque(@PathVariable("idshareholder") int idpartenaire, @PathVariable("duree") int duree) {
    ShareHolder shareholder = Ishareholderrepository.findById(idpartenaire).orElse(null);
    double rentabilite = calculerRendement(idpartenaire, 0.1);

    if (rentabilite < 0.05 && duree < 12) {
        return new ResponseEntity<>("Investissement risqué", HttpStatus.OK);
    } else {
        return new ResponseEntity<>("Investissement prudent", HttpStatus.OK);
    }
}

    @GetMapping("/shareholders/{shareholderId}/event/{eventId}/interest-rate")
    public double getInterestRateForShareholderInEvent(@PathVariable int shareholderId, @PathVariable int eventId) {
        return shareholderservice.estimateFinancialReturnForShareholderInEvent( shareholderId,eventId);
    }




}







