package com.example.microfinancepi.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Amortization {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long idAmt;
    @NotBlank
    Long periode;
    @NotBlank
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    Date date;
    @NotBlank
    Long startAmount;
    @NotBlank
    Long intrest;
    @NotBlank
    Long amrt;
    @NotBlank
    Long annuity;
    Long frais;
    Long agio;

    @OneToOne(mappedBy = "amortization")
    @JsonIgnore
    RequestLoan requestloan;

    /*
     private Integer id_pret;
    private Float amount;
    private Integer investmentPeriodInMonths;
    private Date date_inv;
    private Date datefin;
    private Float Interest;

    private Integer id_user;
    @OneToMany(mappedBy = "pret")
    private List<Transaction> transactions;
     */

}
