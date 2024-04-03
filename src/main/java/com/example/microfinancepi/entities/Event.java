package com.example.microfinancepi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
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
    @NotNull
    @NotBlank
    private String nameEvent;
    @NotNull
    @NotBlank
    private String descriptionEvent;
    @NotNull
    @NotBlank
    private String domain;
    @NotNull
    @FutureOrPresent(message = "The event date must be today or in the future.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateEvent;
    @JsonIgnore
    private int likes;
    @JsonIgnore
    private int dislikes;
    @Enumerated(EnumType.STRING)
    private TypeEvent type;
    @OneToMany(mappedBy ="event" )
    @JsonIgnore
    private List<ShareHolder> shareHolders;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "event_user_set",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private Set<User> userSet = new HashSet<>();
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;




    public void addLike() {
        this.likes++;
    }

    public void addDislike() {
        this.dislikes++;
    }

    public void cancelEvent() {
        this.eventStatus = EventStatus.REPORTED;
    }

  /*  public List<ShareHolder> getShareholders() {
        return shareHolders;
    }

    public void setShareholders(List<ShareHolder> shareholders) {
        this.shareHolders = shareholders;
    }*/


   public int getIdEvent() {
        return idEvent;
    }
    @Override
    public String toString() {
        return "Event{" +
                "id=" + idEvent +
                ", nameEvent='" + nameEvent + '\'' +
                // Ajoutez d'autres attributs ici
                '}';
    }


  /* public void setIndiceRentabilite(double indiceRentabilite) {
    }*/
}
