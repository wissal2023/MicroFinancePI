package com.example.microfinancepi.services;

import com.example.microfinancepi.entities.Event;
import com.example.microfinancepi.entities.ShareHolder;
import com.example.microfinancepi.entities.TypeShareholder;

import java.util.List;

public interface ShareHolderService {
    List<ShareHolder> retrieveAllShareHolder();
    ShareHolder AddShareHolder(ShareHolder shareholder);
    void removeShareHolder(Integer numShareholder);
    ShareHolder retrieveShareHolder(Integer numShareholder);
    ShareHolder updateShareHolder(ShareHolder shareholder);
    ShareHolder assignShareHolderToEvent(Integer idShareHolder, Integer idEvent);

    List<ShareHolder> findShareholdersWithMoreThanOneEvent();
    int getEventYear(ShareHolder shareHolder);

    ShareHolder findMostFrequentPartner();


    ShareHolder findLessFrequentPartner();

    List<ShareHolder> findPartnersWithoutEvents();

    Long countPartnersWithEvents();

    double getPartnersEventPercentages();

    double getPartnersEventPercentages1();
    ShareHolder findShareHolderByInvestment(double investment);
}
