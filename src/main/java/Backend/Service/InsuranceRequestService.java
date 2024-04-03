package Backend.Service;

import Backend.Entity.InsuranceRequest;
import Backend.Repository.InsuranceRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InsuranceRequestService implements IInsuranceRequestService{
   InsuranceRequestRepository InsuranceRequestRepository;
    public List<InsuranceRequest> retrieveRepository;


    public List<InsuranceRequest> retriveAllInsuranceRequest() {
        return InsuranceRequestRepository.findAll();
    }


    public InsuranceRequest retriveInsuranceRequest(Long InsuranceRequestId) {
        return InsuranceRequestRepository.findById(InsuranceRequestId ).get();
    }






    public InsuranceRequest addInsuranceRequest(InsuranceRequest f) {
        return InsuranceRequestRepository.save(f);
    }


    public void removeInsuranceRequest(Long InsuranceRequestId) {
        InsuranceRequestRepository.deleteById(InsuranceRequestId);
    }


    public InsuranceRequest modifyInsuranceRequest(InsuranceRequest InsuranceRequest) {
        return InsuranceRequestRepository.save(InsuranceRequest);
    }
}
