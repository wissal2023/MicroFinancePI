package Backend.Controller;

import Backend.Entity.InsuranceRequest;
import Backend.Service.IInsuranceRequestService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
@RequestMapping("/RequestI")

public class InsuranceRequestControler {
    IInsuranceRequestService InsuranceRequestService;

    @GetMapping("/retrieve-all-InsuranceRequests")
    public List<InsuranceRequest> getInsuranceRequests() {
        List<InsuranceRequest> listInsuranceRequests = InsuranceRequestService.retriveAllInsuranceRequest();
        return listInsuranceRequests;
    }

    @GetMapping("/retrieve-InsuranceRequest/{InsuranceRequest-id}")
    public InsuranceRequest retriveInsuranceRequest(@PathVariable("InsuranceRequest-id") Long chId) {
        InsuranceRequest InsuranceRequest = InsuranceRequestService.retriveInsuranceRequest(chId);
        return InsuranceRequest;
    }

    @PostMapping("/add-InsuranceRequest")
    public InsuranceRequest addInsuranceRequest(@RequestBody InsuranceRequest c) {
        InsuranceRequest InsuranceRequest = InsuranceRequestService.addInsuranceRequest(c);
        return InsuranceRequest;
    }

    // http://localhost:8089/tpfoyer/InsuranceRequest/remove-InsuranceRequest/{InsuranceRequest-id}
    @DeleteMapping("/remove-InsuranceRequest/{InsuranceRequest-id}")
    public void removeInsuranceRequest(@PathVariable("InsuranceRequest-id") Long chId) {
        InsuranceRequestService.removeInsuranceRequest(chId);
    }

    // http://localhost:8089/tpfoyer/InsuranceRequest/modify-InsuranceRequest
    @PutMapping("/modify-InsuranceRequest")
    public InsuranceRequest modifyInsuranceRequest(@RequestBody InsuranceRequest c) {
        InsuranceRequest InsuranceRequest = InsuranceRequestService.modifyInsuranceRequest(c);
        return InsuranceRequest;
    }
}