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
public class InsuranceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date requestedDate;

    private String reasonForInsurance;

    @Lob
    private byte[] File;

    @ManyToOne(cascade = CascadeType.ALL)
    Insurance insuranceR;



}
