package com.example.microfinancepi.entities;


import lombok.*;

import javax.validation.Valid;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Valid
public class LoginRequest {
    private String email;
    private String user_password;
}
