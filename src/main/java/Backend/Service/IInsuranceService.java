package Backend.Service;

import Backend.Entity.Insurance;
import com.google.cloud.dialogflow.v2.SessionsClient;

import java.io.IOException;
import java.util.List;

public interface IInsuranceService {
    public List<Insurance> retriveAllInsurance();
    public Insurance retriveInsurance(Long InsuranceId);
    public Insurance addInsurance(Insurance f);
    public void removeInsurance(Long InsuranceId);
    public Insurance modifyInsurance(Insurance Insurance);
    public SessionsClient sessionsClient() throws IOException;
}
