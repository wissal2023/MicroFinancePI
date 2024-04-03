package com.example.microfinancepi.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OfferLoan implements Serializable {
    static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long idOffer;

    String status ="AVAILABLE";// the offer is availabe or not
    @Enumerated(EnumType.STRING)
    LoanType typeLoan;
    @NotBlank
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    Date offrDate;
    Float tmm;
    @NotBlank(message = "the amount should be in decimal value")
    Long maxAmnt;// should be in %
    @NotBlank(message = "the amount should be in decimal value and better than 0")
    Long minAmnt;
    @Min(value = 3, message = "the repayment periode should not be lower than 3 month")
    Long minRepaymentPer;
    @NotBlank(message = "the interest rate should be grater than the TMM ")
    @Min(value = 0, message = "The interest rate should not be negative")
    Float intRate;

    @ManyToMany(cascade = CascadeType.ALL)
    Set<RequestLoan> requestloans;
    @ManyToOne
    User user;


}
