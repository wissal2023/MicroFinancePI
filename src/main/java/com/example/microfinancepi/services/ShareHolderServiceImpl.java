package com.example.microfinancepi.services;

import com.example.microfinancepi.entities.*;
import com.example.microfinancepi.repositories.IEventRepository;
import com.example.microfinancepi.repositories.IShareholderRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import com.example.microfinancepi.entities.TypeShareholder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.mail.MessagingException;
import java.util.*;

@Service
@AllArgsConstructor
public class ShareHolderServiceImpl implements ShareHolderService {
    IShareholderRepository Ishareholderrepository;
    IEventRepository iEventRepository;

    // EmailSenderService emailSenderService;

    @Override
    public List<ShareHolder> retrieveAllShareHolder(User authentication) {
        User_role userRole = getCurrentUserRole(authentication);

        if (!userRole.equals(User_role.ADMIN)) {
            throw new AccessDeniedException("you're not authorized");
        }
        return Ishareholderrepository.findAll();
    }

    @Override
    public ShareHolder AddShareHolder(ShareHolder shareholder, User authentication) {
        User_role userRole = getCurrentUserRole(authentication);

        if (!userRole.equals(User_role.SHAREHOLDER) && !userRole.equals(User_role.ADMIN)) {
            throw new AccessDeniedException("you're not authorized");
        }

        return Ishareholderrepository.save(shareholder);
    }

    private User_role getCurrentUserRole(User user) {
        return user.getRole();
    }

    @Override
    public void removeShareHolder(Integer numShareholder, User authentication) {
        User_role userRole = getCurrentUserRole(authentication);
        if (!userRole.equals(User_role.ADMIN)) {
            throw new AccessDeniedException("You're not authorized");
        } else {
            Ishareholderrepository.deleteById(numShareholder);
        }
    }

    @Override
    public ShareHolder retrieveShareHolder(Integer numShareholder, User authentication) {
        User_role userRole = getCurrentUserRole(authentication);

        if (!userRole.equals(User_role.ADMIN)) {
            throw new AccessDeniedException("you're not authorized");
        }
        return Ishareholderrepository.findById(numShareholder).orElse(null);
    }

    @Override
    public ShareHolder updateShareHolder(ShareHolder shareholder, User authentication) {
        User_role userRole = getCurrentUserRole(authentication);

        if (!userRole.equals(User_role.SHAREHOLDER)) {
            throw new AccessDeniedException("you're not authorized");
        }
        return Ishareholderrepository.save(shareholder);
    }

    @Override
    public ShareHolder assignShareHoldersToEvent(Integer idShareHolder, Integer idEvent) {
        ShareHolder shareholder = Ishareholderrepository.findById(idShareHolder).orElse(null);
        Event event = iEventRepository.findById(idEvent).orElse(null);
        shareholder.setEvent(event);
        return Ishareholderrepository.save(shareholder);
    }

    public void saveShareHolder(ShareHolder shareHolder) {
        Ishareholderrepository.save(shareHolder);
    }

    @Override
    public List<ShareHolder> findShareholdersWithMoreThanOneEvent(User authentication) {
        User_role userRole = getCurrentUserRole(authentication);

        if (!userRole.equals(User_role.ADMIN)) {
            throw new AccessDeniedException("you're not authorized");
        }
        List<ShareHolder> shareholders = Ishareholderrepository.findAll();
        List<ShareHolder> result = new ArrayList<>();

        for (ShareHolder shareholder : shareholders) {
            Event event = shareholder.getEvent();
            if (event != null && event.getShareHolders() != null && event.getShareHolders().size() > 1) {
                result.add(shareholder);
            }
        }

        return result;
    }

    public int getEventYear(ShareHolder shareHolder, User authentication) {
        User_role userRole = getCurrentUserRole(authentication);
        if (!userRole.equals(User_role.ADMIN)) {
            throw new AccessDeniedException("You're not authorized");
        } else {
            Event event = shareHolder.getEvent();
            if (event != null && event.getDateEvent() != null) {
                return event.getDateEvent().getYear();
            } else {
                // Gérer le cas où l'événement ou sa date est null
                // Vous pouvez retourner une valeur par défaut ou lancer une exception, selon vos besoins
                return -1; // Exemple de valeur par défaut, à remplacer par ce qui convient à votre application
            }
        }
    }

    public ShareHolder findMostFrequentPartner() {
        List<ShareHolder> partnerList = Ishareholderrepository.findMostFrequentPartner(TypeShareholder.BANK);
        if (!partnerList.isEmpty()) {
            return partnerList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public ShareHolder findLessFrequentPartner() {
        List<ShareHolder> partnerList = Ishareholderrepository.findLessFrequentPartner(TypeShareholder.BANK);
        if (!partnerList.isEmpty()) {
            return partnerList.get(0);
        } else {
            return null; // retourne null si la liste est vide
        }
    }


    @Override
    public List<ShareHolder> findPartnersWithoutEvents() {
        return Ishareholderrepository.findPartnersWithoutEvents();
    }

    @Override
    public Long countPartnersWithEvents() {
        return Ishareholderrepository.countPartnersWithEvents();
    }

    @Override
    public double getPartnersEventPercentages() {
        Long nbPartnersWithEvents = Ishareholderrepository.countPartnersWithEvents();
        Long nbPartnersWithoutEvents = Ishareholderrepository.count() - nbPartnersWithEvents;
        Long totalPartners = Ishareholderrepository.count();
        double pourcentageWithoutEvents = (double) nbPartnersWithoutEvents / totalPartners * 100.0;
        return pourcentageWithoutEvents;
    }

    @Override
    public double getPartnersEventPercentages1() {
        Long nbPartnersWithEvents = Ishareholderrepository.countPartnersWithEvents();
        Long totalPartners = Ishareholderrepository.count();
        double pourcentageWithEvents = (double) nbPartnersWithEvents / totalPartners * 100.0;
        return pourcentageWithEvents;
    }

    @Override
    public ShareHolder findShareHolderByInvestment(double investment) {
        return Ishareholderrepository.findShareHolderByInvestment(investment);
    }

    @Override
    public double calculateInterestRateForShareholderInEvent(int shareholderId, int eventId) {
        // Récupérer l'actionnaire par son ID
        ShareHolder shareholder = Ishareholderrepository.findById(shareholderId).orElse(null);
        if (shareholder == null) {
            throw new RuntimeException("Shareholder not found with ID: " + shareholderId);
        }

        // Récupérer le type de l'actionnaire
        TypeShareholder type = shareholder.getPartner();

        // Calculer le taux d'intérêt en fonction du type de l'actionnaire
        double interestRate;
        switch (type) {
            case SUPPLIER:
                interestRate = 0.05; // 5%
                break;
            case ASSOCIATION:
                interestRate = 0.03; // 3%
                break;
            case BANK:
                interestRate = 0.02; // 2%
                break;
            default:
                interestRate = 0.01; // 1% (default)
                break;
        }

        return interestRate;
    }

    // Méthode pour estimer le rendement financier potentiel de l'investissement dans un événement pour un actionnaire spécifique
    public double estimateFinancialReturnForShareholderInEvent(int shareholderId, int eventId) {
        // Récupérer l'actionnaire par son ID
        ShareHolder shareholder = Ishareholderrepository.findById(shareholderId).orElse(null);
        if (shareholder == null) {
            throw new RuntimeException("Shareholder not found with ID: " + shareholderId);
        }

        // Récupérer l'événement par son ID
        Event event = iEventRepository.findById(eventId).orElse(null);
        if (event == null) {
            throw new RuntimeException("Event not found with ID: " + eventId);
        }

        // Calculer le taux d'intérêt pour l'actionnaire dans cet événement
        double interestRate = calculateInterestRateForShareholderInEvent(shareholderId, eventId);

        // Estimer le rendement financier de l'investissement dans cet événement
        double financialReturn = shareholder.getInvestment() * interestRate;

        return financialReturn;
    }
    @Transactional
    @Override
    public ResponseEntity<String> investInEvent(int shareholderId, int eventId) {
        try {
            // Récupérez le shareholder par son ID depuis le repository
            ShareHolder shareholder = Ishareholderrepository.findById(shareholderId)
                    .orElseThrow(() -> new RuntimeException("Shareholder not found with ID: " + shareholderId));

            // Récupérez l'événement par son ID depuis le repository
            Event event = iEventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));

            // Vérifiez si l'événement est déjà complet
            if (event.getEventStatus() == EventStatus.COMPLETED) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Cannot invest in a completed event.");
            }

            // Vérifiez si le montant d'investissement du shareholder est inférieur ou égal au montant nécessaire pour l'événement
            if (shareholder.getInvestment() <= event.getInvestNeeded()) {
                // Faites investir le shareholder dans l'événement
                shareholder.investInEvent(event);

                // Mettez à jour les entités dans la base de données
                Ishareholderrepository.save(shareholder);
                iEventRepository.save(event);

                // Vérifiez si le solde disponible de l'événement est égal à zéro
                if (event.getInvestNeeded() == 0) {
                    // Mettez à jour le statut de l'événement à "completed"
                    event.setEventStatus(EventStatus.COMPLETED);
                    iEventRepository.save(event);
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Investment amount exceeds event balance.");
            }

            // Retournez une réponse de succès si l'investissement a réussi
            return ResponseEntity.ok("Investment successful.");
        } catch (Exception e) {
            // Gérez d'autres exceptions ici, par exemple, des erreurs de base de données
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the request.");
        }
    }
    @Override
    public double calculateInterestRateForShareholder(double investment, TypeShareholder type) {
        double interestRate;

        switch (type) {
            case SUPPLIER:
                if (investment >= 10000) {
                    interestRate = 0.06; // 6%
                } else if (investment >= 5000) {
                    interestRate = 0.055; // 5.5%
                } else {
                    interestRate = 0.05; // 5%
                }
                break;
            case ASSOCIATION:
                interestRate = 0.03; // 3%
                break;
            case BANK:
                interestRate = 0.02; // 2%
                break;
            default:
                interestRate = 0.01; // 1% (default)
                break;
        }

        return interestRate;
    }




}

