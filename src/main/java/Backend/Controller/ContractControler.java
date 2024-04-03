package Backend.Controller;

import Backend.Entity.Category;
import Backend.Entity.Contract;
import Backend.Entity.ContractStatus;
import Backend.Entity.User;
import Backend.Service.IContractService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class ContractControler {
    IContractService ContractService;

    @GetMapping("/retrieve-all-Contracts")
    public List<Contract> getContracts() {
        List<Contract> listContracts = ContractService.retriveAllContract();
        return listContracts;
    }

    @GetMapping("/retrieve-Contract/{Contract-id}")
    public Contract retriveContract(@PathVariable("Contract-id") Long chId) {
        Contract Contract = ContractService.retriveContract(chId);
        return Contract;
    }

    @PostMapping("/add-Contract")
    public Contract addContract(@RequestBody Contract c) {
        Contract Contract = ContractService.addContract(c);
        return Contract;
    }


    @DeleteMapping("/remove-Contract/{Contract-id}")
    public void removeContract(@PathVariable("Contract-id") Long chId) {
        ContractService.removeContract(chId);
    }


    @PutMapping("/modify-Contract")
    public Contract modifyContract(@RequestBody Contract c) {
        Contract Contract = ContractService.modifyContract(c);
        return Contract;
    }
    @PostMapping("/Sgin-Contract")
    public Contract signContract(@RequestBody User u , Contract c) {
        Contract Contract = ContractService.signContract(u,c);
        return Contract;
    }
    @GetMapping("/retrieve-ContractByCategory/{Category}")
    public  List<Contract>  viewContractsByCategory(@PathVariable("Category") Category cat) {
        List<Contract> listContracts = ContractService.viewContractsByCategory(cat);
        return listContracts;
    }
    @GetMapping("/retrieve-ContractByStatut/{Statut}")
    public  List<Contract>  viewContractsByStatus(@PathVariable("Statut") ContractStatus stat) {
        List<Contract> listContracts = ContractService.viewContractsByStatus(stat);
        return listContracts;
    }
    @GetMapping("/{id}/generate-pdf")
    public String generatePDF(@PathVariable Long id) {
        try {
            Contract contract = ContractService.retriveContract(id); // Assuming you have a method to retrieve a Contract by ID
            ContractService.generatePDFversion(contract);
            return "PDF generated successfully";
        } catch (EntityNotFoundException e) {
            return "Contract not found";
        } catch (IOException | DocumentException e) {
            return "Failed to generate PDF: " + e.getMessage();
        }
    }

}