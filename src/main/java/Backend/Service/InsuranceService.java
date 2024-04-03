package Backend.Service;

import Backend.Entity.Insurance;
import Backend.Repository.InsuranceRepository;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class InsuranceService implements IInsuranceService{
    InsuranceRepository InsuranceRepository;
    public List<Insurance> retrieveRepository;


    public List<Insurance> retriveAllInsurance() {
        return InsuranceRepository.findAll();
    }


    public Insurance retriveInsurance(Long InsuranceId) {
        return InsuranceRepository.findById(InsuranceId ).get();
    }






    public Insurance addInsurance(Insurance f) {
        return InsuranceRepository.save(f);
    }


    public void removeInsurance(Long InsuranceId) {
        InsuranceRepository.deleteById(InsuranceId);
    }


    public Insurance modifyInsurance(Insurance Insurance) {
        return InsuranceRepository.save(Insurance);
    }

    @Bean
    public SessionsClient sessionsClient() throws IOException {
        // Load credentials from the classpath
        // Load credentials from the file system
        InputStream credentialsStream = new FileInputStream("C:/Users/pc/Desktop/isurance-418404-741ece6fd389.json");

        // Create SessionsClient with credentials
        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);
        return SessionsClient.create(SessionsSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build());
    }
}
