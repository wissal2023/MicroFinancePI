package Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    @OneToMany (mappedBy = "user",cascade = CascadeType.ALL)
    Set<Insurance> insurences;
    @OneToOne(cascade = CascadeType.ALL)
    Contract contract;
}
