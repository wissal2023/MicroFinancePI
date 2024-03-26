package com.example.microfinancepi.services;

import com.example.microfinancepi.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    User addUser(User user);
    List<User> getallUsers();
    User updateUser(User user );
    void deleteUser(int id);
    User getUserByid(int id);
    boolean isCurrentUser(int id) ;

    UserDetails loadUserByUsername(String username);
}
