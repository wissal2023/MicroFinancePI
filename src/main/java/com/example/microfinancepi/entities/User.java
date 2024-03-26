package com.example.microfinancepi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User implements Serializable , UserDetails {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_user;
    private String user_firstname;
    private String user_lastname;
    private String email;
    private String user_password;
    @Enumerated
    private User_role role;
    private String profile_picture;
    private String user_phone;
    private String Fn_rapport;
    private String badge;
    private String Sector_activite;
    private Float amount;

    @ManyToMany(mappedBy = "userSet")
    @JsonIgnore
    private Set<Event> eventSet;
    @OneToMany(mappedBy = "user")
    Set<OfferLoan> offerLoans;

    public void setUser_password(String password) {
        this.user_password = new BCryptPasswordEncoder().encode(password);
    }

    @JsonIgnore
    @Transient
    private List<GrantedAuthority> authorities;
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities = Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return user_password;
    }


    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

}
