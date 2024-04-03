package Backend.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;


    private Category category;


    private Date signDate;


    private Date deadLineDate;


    private double payedAmount;


    private double reminingAmount;


    private double netPremiuim;


    private double totalPemium;


    private double NetMangamentFees;


    private ContractStatus status;


    private double discount;


    private double tax;


    private double reInsurancePart;


    private Date LastUpdate;

    @OneToOne(mappedBy ="contract",cascade = CascadeType.ALL )
    User Usercontract;
}
