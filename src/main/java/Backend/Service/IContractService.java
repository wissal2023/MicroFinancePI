package Backend.Service;

import Backend.Entity.Category;
import Backend.Entity.Contract;
import Backend.Entity.ContractStatus;
import Backend.Entity.User;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.util.List;

public interface IContractService {
    public List<Contract> retriveAllContract();
    public Contract retriveContract(Long ContractId);
    public Contract addContract(Contract f);
    public void removeContract(Long ContractId);
    public Contract modifyContract(Contract C);


    List<Contract> findUserContracts(User Inr);
    void updateContract(Contract c);
    void unsignContract(Contract c);
    Contract signContract(User insured, Contract contract);

    List<Contract> viewContractsByCategory(Category cat);

    List<Contract> viewContractsByStatus(ContractStatus stat);


    Document generatePDFversion(Contract c) throws IOException, BadElementException, DocumentException;

}
