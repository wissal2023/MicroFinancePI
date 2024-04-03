package Backend.Controller;

import Backend.Entity.Insurance;
import Backend.Service.IInsuranceService;
import com.google.api.Logging;
import java.util.logging.Logger;
import com.google.cloud.dialogflow.v2.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class InsuranceControler {
    IInsuranceService InsuranceService;

    @Autowired
    private SessionsClient sessionsClient;

    @GetMapping("/retrieve-all-Insurances")
    public List<Insurance> getInsurances() {
        List<Insurance> listInsurances = InsuranceService.retriveAllInsurance();
        return listInsurances;
    }

    @GetMapping("/retrieve-Insurance/{Insurance-id}")
    public Insurance retriveInsurance(@PathVariable("Insurance-id") Long chId) {
        Insurance Insurance = InsuranceService.retriveInsurance(chId);
        return Insurance;
    }

    @PostMapping("/add-Insurance")
    public Insurance addInsurance(@RequestBody Insurance c) {
        Insurance Insurance = InsuranceService.addInsurance(c);
        return Insurance;
    }

    // http://localhost:8089/tpfoyer/Insurance/remove-Insurance/{Insurance-id}
    @DeleteMapping("/remove-Insurance/{Insurance-id}")
    public void removeInsurance(@PathVariable("Insurance-id") Long chId) {
        InsuranceService.removeInsurance(chId);
    }

    // http://localhost:8089/tpfoyer/Insurance/modify-Insurance
    @PutMapping("/modify-Insurance")
    public Insurance modifyInsurance(@RequestBody Insurance c) {
        Insurance Insurance = InsuranceService.modifyInsurance(c);
        return Insurance;
    }
    @PostMapping("/chatbot/message")
    public String handleUserMessage(@RequestBody String userMessage) throws IOException {
        SessionName session = SessionName.of("isurance-418404", "your-session-id");
        TextInput.Builder textInput = TextInput.newBuilder().setText(userMessage).setLanguageCode("fr");
        QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

        try {
            DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);
            if (response.getQueryResult().getFulfillmentText() != null) {
                java.util.logging.Logger.getLogger(InsuranceControler.class.getName()).info("Received response from Dialogflow: " + response.getQueryResult().getFulfillmentText().toString());
                return response.getQueryResult().getFulfillmentText();
            } else {
                java.util.logging.Logger.getLogger(InsuranceControler.class.getName()).warning("Dialogflow response did not contain a fulfillment message.");
                return "Sorry, I couldn't understand your request. Can you please rephrase it?";
            }
        } catch (Exception e) {
            java.util.logging.Logger.getLogger(InsuranceControler.class.getName()).severe("Error while calling Dialogflow API: " + e.getMessage());
            return "Sorry, there was an error processing your request. Please try again later.";
        }
    }
    }

