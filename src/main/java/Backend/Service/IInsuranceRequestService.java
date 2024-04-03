package Backend.Service;

import Backend.Entity.InsuranceRequest;

import java.util.List;

public interface IInsuranceRequestService {
    public List<InsuranceRequest> retriveAllInsuranceRequest();
    public InsuranceRequest retriveInsuranceRequest(Long InsuranceRequestId);
    public InsuranceRequest addInsuranceRequest(InsuranceRequest f);
    public void removeInsuranceRequest(Long InsuranceRequestId);
    public InsuranceRequest modifyInsuranceRequest(InsuranceRequest request);
}
