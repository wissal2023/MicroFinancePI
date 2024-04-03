package com.example.microfinancepi.services;

import com.example.microfinancepi.entities.Event;
import com.example.microfinancepi.entities.ShareHolder;
import com.example.microfinancepi.entities.TypeShareholder;
import com.example.microfinancepi.entities.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ShareHolderService {
    List<ShareHolder> retrieveAllShareHolder(User authentication);
    ShareHolder AddShareHolder(ShareHolder shareholder, User authentication);
    void removeShareHolder(Integer numShareholder, User authentication);
    ShareHolder retrieveShareHolder(Integer numShareholder, User authentication);
    ShareHolder updateShareHolder(ShareHolder shareholder, User authentication);
    ShareHolder assignShareHoldersToEvent(Integer idShareHolder, Integer idEvent);
    void saveShareHolder(ShareHolder shareHolder);

    List<ShareHolder> findShareholdersWithMoreThanOneEvent( User authentication);
    int getEventYear(ShareHolder shareHolder, User authentication);

    ShareHolder findMostFrequentPartner();
    double calculateInterestRateForShareholderInEvent(int shareholderId, int eventId);
    double estimateFinancialReturnForShareholderInEvent(int shareholderId, int eventId);
    ResponseEntity<String> investInEvent(int shareholderId, int eventId);
    double calculateInterestRateForShareholder(double investment, TypeShareholder type);



    ShareHolder findLessFrequentPartner();

    List<ShareHolder> findPartnersWithoutEvents();

    Long countPartnersWithEvents();

    double getPartnersEventPercentages();

    double getPartnersEventPercentages1();
    ShareHolder findShareHolderByInvestment(double investment);
}
