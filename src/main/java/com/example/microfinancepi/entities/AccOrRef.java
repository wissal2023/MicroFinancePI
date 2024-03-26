package com.example.microfinancepi.entities;


import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class AccOrRef implements Serializable{
     static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Integer id_AccOrRef;
     String check_loan;


    @OneToOne
    @JoinColumn(name = "id_request")
     RequestLoan request;



}
