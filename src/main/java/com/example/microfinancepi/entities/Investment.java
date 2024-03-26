package com.example.microfinancepi.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Investment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_invest;
    private Integer owner_id;
    private String project_name;
    private Float amount_inv;
    private String description;
    private Date date_debut;
    private Date date_fin;
    private Float invest_value;
    private Status_inv status;
    private Float income_by_day;
    @OneToMany(mappedBy = "investment")
    private List<Transaction> transactions;
}
