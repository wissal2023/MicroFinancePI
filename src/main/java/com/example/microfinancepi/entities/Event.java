package com.example.microfinancepi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEvent;
    private String nameEvent;
    private String descriptionEvent;
    private String domain;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateEvent;

    private int likes;
    private int dislikes;
    @Enumerated(EnumType.STRING)
    private TypeEvent type;
    @OneToMany(mappedBy ="event" )
    @JsonIgnore
    private List<ShareHolder> shareHolders;
    @ManyToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<User> userSet;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;


    public void addLike() {
        this.likes++;
    }

    public void addDislike() {
        this.dislikes++;
    }

    public void cancelEvent() {
        this.eventStatus = EventStatus.CANCELLED;
    }

  /*  public int getIdEvent() {
        return idEvent;
    }*/


  /* public void setIndiceRentabilite(double indiceRentabilite) {
    }*/
}
