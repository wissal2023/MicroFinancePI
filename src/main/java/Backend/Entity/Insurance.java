package Backend.Entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Insurance {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
     Long insurance_ID ;

    String insurance_Name ;
    String insurance_Discription;
    Category insurance_Category;

    @ManyToOne
    User  user;


    @OneToMany(mappedBy = "insuranceR",cascade = CascadeType.ALL)
    Set<InsuranceRequest> InsuranceRequest;
}
