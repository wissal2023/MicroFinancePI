package com.example.microfinancepi.services;

import com.example.microfinancepi.entities.Event;
import com.example.microfinancepi.entities.ShareHolder;
import com.example.microfinancepi.entities.TypeShareholder;
import com.example.microfinancepi.repositories.IEventRepository;
import com.example.microfinancepi.repositories.IShareholderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class ShareHolderServiceImpl implements ShareHolderService {
    private IShareholderRepository Ishareholderrepository;
    private IEventRepository iEventRepository;
    @Override
    public List<ShareHolder> retrieveAllShareHolder()  {
        return Ishareholderrepository.findAll();
    }

    @Override
    public ShareHolder AddShareHolder(ShareHolder shareholder) {
        return Ishareholderrepository.save(shareholder);
    }

    @Override
    public void removeShareHolder(Integer numShareholder) {
        Ishareholderrepository.deleteById(numShareholder);
    }

    @Override
    public ShareHolder retrieveShareHolder(Integer numShareholder) {
        return Ishareholderrepository.findById(numShareholder).orElse(null);
    }

    @Override
    public ShareHolder updateShareHolder(ShareHolder shareholder) {
        return Ishareholderrepository.save(shareholder);
    }
    @Override
    public ShareHolder assignShareHolderToEvent(Integer idShareHolder, Integer idEvent){
        ShareHolder shareholder=Ishareholderrepository.findById(idShareHolder).orElse(null);
        Event event=iEventRepository.findById(idEvent).orElse(null);
        shareholder.setEvent(event);
        return Ishareholderrepository.save(shareholder);
    }
    @Override
    public List<ShareHolder> findShareholdersWithMoreThanOneEvent() {
        List<ShareHolder> shareholders = Ishareholderrepository.findAll();
        Map<Integer, Integer> countMap = new HashMap<>();
        for (ShareHolder shareholder : shareholders) {
            int count = shareholder.getEvent().getShareHolders().size();
            countMap.put(shareholder.getIdShareholder(), count);
        }
        List<ShareHolder> result = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > 1) {
                Optional<ShareHolder> optionalShareholder = Ishareholderrepository.findById(entry.getKey());
                if (optionalShareholder.isPresent()) {
                    result.add(optionalShareholder.get());
                }
            }
        }
        return result;
    }

    public int getEventYear(ShareHolder shareHolder) {
        Event event = shareHolder.getEvent();
        return event.getDateEvent().getYear();
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
        Long nbPartnersWithEvents =Ishareholderrepository.countPartnersWithEvents();
        Long nbPartnersWithoutEvents = Ishareholderrepository.count() - nbPartnersWithEvents;
        Long totalPartners = Ishareholderrepository.count();
        double pourcentageWithoutEvents = (double) nbPartnersWithoutEvents / totalPartners * 100.0;
        return pourcentageWithoutEvents;
    }
    @Override
    public double getPartnersEventPercentages1(){
        Long nbPartnersWithEvents =Ishareholderrepository.countPartnersWithEvents();
        Long totalPartners = Ishareholderrepository.count();
        double pourcentageWithEvents = (double) nbPartnersWithEvents / totalPartners * 100.0;
        return pourcentageWithEvents;
    }
   @Override
   public ShareHolder findShareHolderByInvestment(double investment){
        return Ishareholderrepository.findShareHolderByInvestment(investment);
   }




}
