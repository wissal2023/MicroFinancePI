package com.example.microfinancepi.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name="request_loans")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestLoan implements Serializable {
    static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long requestId;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)//timestamp for the hours
    Date reqDate;

    @Enumerated(EnumType.STRING)
    StatLoan status;
    @NotBlank
    @Min(value = 0, message = "The interest rate should not be negative")
    Float loanAmnt;
    @NotBlank
    @Min(value = 3, message = "the repayment periode should not be lower than 3 month")
    Long nbrMonth;
    @Min(value = 1, message = "the repayment periode should not be lower than 3 month")
    @NotBlank
    Long nbrYears;
    @Lob
    byte[] garantor;

    //association
    @JsonIgnore
    @ManyToMany(mappedBy = "requestloans")
    Set<OfferLoan> offerloan;
    @OneToOne(cascade = CascadeType.ALL)//affectation
    Amortization amortization;

    /*
    @ManyToOne
    @JoinColumn(name = "id_offer")
    private Offers_Credit offer;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
    @OneToOne
    private AccOrRef accOrRef;
     */

}