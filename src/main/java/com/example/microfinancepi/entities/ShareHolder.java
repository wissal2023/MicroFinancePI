package com.example.microfinancepi.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class ShareHolder implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idShareholder;
    @NotBlank(message = "Last name is required")
    private String lastNameShareholder;

    @NotBlank(message = "First name is required")
    private String firstNameShareholder;

    @NotNull(message = "Investment amount is required")
    @Min(value = 1000, message = "Investment amount must be at least 1000")
    private Double investment;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Phone number is required")
    @Pattern(regexp = "\\d{8}", message = "Phone number must be 8 digits")
    private int numTel;

    @NotNull(message = "Type of shareholder is required")
    @Enumerated(EnumType.STRING)
    private TypeShareholder partner;
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private Event event;



}
