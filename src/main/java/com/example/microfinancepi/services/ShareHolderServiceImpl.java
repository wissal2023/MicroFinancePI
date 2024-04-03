package com.example.microfinancepi.services;

import com.example.microfinancepi.entities.*;
import com.example.microfinancepi.repositories.IEventRepository;
import com.example.microfinancepi.repositories.IShareholderRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.*;

@Service
@AllArgsConstructor
public class ShareHolderServiceImpl implements ShareHolderService {
    IShareholderRepository Ishareholderrepository;
    IEventRepository iEventRepository;

   // EmailSenderService emailSenderService;

    @Override
    public List<ShareHolder> retrieveAllShareHolder( User authentication) {
        User_role userRole = getCurrentUserRole(authentication);

        if (!userRole.equals(User_role.ADMIN)) {
            throw new AccessDeniedException("you're not authorized");
        }
        return Ishareholderrepository.findAll();
    }

    @Override
    public ShareHolder AddShareHolder(ShareHolder shareholder, User authentication) {
        User_role userRole = getCurrentUserRole(authentication);

        if (!userRole.equals(User_role.SHAREHOLDER)&&!userRole.equals(User_role.ADMIN)) {
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
    }}

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
    public List<ShareHolder> findShareholdersWithMoreThanOneEvent( User authentication) {
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
    }}

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


    }

