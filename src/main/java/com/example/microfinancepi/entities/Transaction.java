package com.example.microfinancepi.entities;



import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_transaction;
    private Integer sender;
    private Integer receiver;
    private Date date_transaction;

    @Enumerated(EnumType.STRING)
    private Type_transaction type;
    private Float value;
    @Enumerated(EnumType.STRING)
    private Status_Tr status;
    @ManyToOne
    private Investment investment;
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

}
