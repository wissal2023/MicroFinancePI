package com.example.microfinancepi.repositories;

import com.example.microfinancepi.entities.User;
import com.example.microfinancepi.entities.User_role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    List<User> findUserByRole(User_role role);
    ///
    Optional<User> findUserByEmail(String username);

}
