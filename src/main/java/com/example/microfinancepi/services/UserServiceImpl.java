package com.example.microfinancepi.services;


import com.example.microfinancepi.entities.User;
import com.example.microfinancepi.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService , UserDetailsService {

    private UserRepository iUserRepository;
    @Override
    public User addUser(User user) {
        return iUserRepository.save(user);
    }

    @Override
    public List<User> getallUsers() {
        return iUserRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        User user1=iUserRepository.findById(user.getId_user()).orElse(null);
        if (user1!=null){
        return
                iUserRepository.save(user1);}
        else return null;

    }

    @Override
    public void deleteUser(int id) {
        iUserRepository.deleteById(id);
    }

    @Override
    public User getUserByid(int id) {
        User user=iUserRepository.findById(id).get();
        return user;
    }
    public boolean isCurrentUser(int id) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            User user = (User) principal;
            return user.getId_user()==id;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> user=iUserRepository.findUserByEmail(username);
        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
        return user.get();
    }
}
