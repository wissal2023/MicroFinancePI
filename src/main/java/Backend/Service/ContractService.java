package Backend.Service;

import Backend.Entity.Category;
import Backend.Entity.Contract;
import Backend.Entity.ContractStatus;
import Backend.Entity.User;
import Backend.Repository.ContractRepository;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.persistence.EntityNotFoundException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ContractService implements IContractService{
    ContractRepository ContractRepository;
    public List<Contract> retrieveRepository;


    public List<Contract> retriveAllContract() {
        return ContractRepository.findAll();
    }


    public Contract retriveContract(Long ContractId) {
        return ContractRepository.findById(ContractId ).get();
    }


    public Contract addContract(Contract f) {
        return ContractRepository.save(f);
    }


    public void removeContract(Long ContractId) {
        ContractRepository.deleteById(ContractId);
    }


    public Contract modifyContract(Contract Contract) {
        return ContractRepository.save(Contract);
    }



    public List<Contract> findUserContracts(User inr) {
        List<Contract> ls = ContractRepository.findAll();
        List<Contract> retoure = new ArrayList<>();
        for (Contract C : ls) {
            if (C.getUsercontract().equals(inr))
                retoure.add(C);
        }
        return retoure;
    }



    public void updateContract(Contract c) {
        c.setLastUpdate(Date.from(Calendar.getInstance().toInstant()));
        ContractRepository.save(c);

    }

    public void unsignContract(Contract c) {
        c.setStatus(ContractStatus.Ended);
        c.setReminingAmount(0);
        updateContract(c);

    }


    public Contract signContract(User insured, Contract contract) {

        contract.setStatus(ContractStatus.Valid_Contract);
        contract.setReminingAmount(0);
        contract.setLastUpdate(Date.from(Calendar.getInstance().toInstant()));
        contract.setDeadLineDate(Date.from(Calendar.getInstance().toInstant()));
        contract.setSignDate(Date.from(Calendar.getInstance().toInstant()));
        contract.setUsercontract(insured);

        return contract;
    }

    public List<Contract> viewContractsByCategory(Category cat) {
        List<Contract> ls=ContractRepository.findAll();
        List<Contract> retoure = new ArrayList<>();
        for(Contract itr :ls)
        {

            if (itr.getCategory().equals(cat))
                retoure.add(itr);
        }



        return retoure;
    }


    public List<Contract> viewContractsByStatus(ContractStatus stat) {
        List<Contract> ls=ContractRepository.findAll();
        List<Contract> retoure = new ArrayList<Contract>();
        for(Contract C :ls)
        {

            if (C.getStatus().equals(stat))
                retoure.add(C);
        }

        return retoure;
    }



    public Document generatePDFversion(Contract c) throws IOException, DocumentException {

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("contract_" + c.getId() + ".pdf"));
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

            Paragraph title = new Paragraph("Contract Details", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            //document.add(new Paragraph("Contract ID: " + c.getId(), normalFont));
            document.add(new Paragraph("Category: " + c.getCategory(), normalFont));
            document.add(new Paragraph("Sign Date: " + c.getSignDate(), normalFont));
            document.add(new Paragraph("Deadline Date: " + c.getDeadLineDate(), normalFont));
            document.add(new Paragraph("Payed Amount: " + c.getPayedAmount(), normalFont));
            document.add(new Paragraph("Remaining Amount: " + c.getReminingAmount(), normalFont));
            document.add(new Paragraph("Net Premium: " + c.getNetPremiuim(), normalFont));
            document.add(new Paragraph("Total Premium: " + c.getTotalPemium(), normalFont));
            document.add(new Paragraph("Net Management Fees: " + c.getNetMangamentFees(), normalFont));
            document.add(new Paragraph("Status: " + c.getStatus(), normalFont));
            document.add(new Paragraph("Discount: " + c.getDiscount(), normalFont));
            document.add(new Paragraph("Tax: " + c.getTax(), normalFont));
            document.add(new Paragraph("Re-Insurance Part: " + c.getReInsurancePart(), normalFont));
            document.add(new Paragraph("Last Update: " + c.getLastUpdate(), normalFont));

            document.close();
            return document;
        }

}
