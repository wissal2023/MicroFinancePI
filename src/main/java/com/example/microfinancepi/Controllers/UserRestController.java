package com.example.microfinancepi.Controllers;

import com.example.microfinancepi.entities.LoginRequest;
import com.example.microfinancepi.entities.User;
import com.example.microfinancepi.services.TokenBlacklistService;
import com.example.microfinancepi.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.example.microfinancepi.security.SecurityConstants.TOKEN_HEADER;
import static com.example.microfinancepi.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@AllArgsConstructor
@RequestMapping("api/user")
@SecurityRequirement(name = "bearerAuth")
public class UserRestController {
    @Autowired
    TokenBlacklistService tokenBlacklistService;
UserService userService;
    @PostMapping("/addUser")
    public void addUserByForm(@RequestBody User user){
        userService.addUser(user);
    }


    @GetMapping("/getallUsers/admin")
    public List<User> getallUsers(){
        return userService.getallUsers();
    }

    @PutMapping("/updateUser")
    public void updadeUser(@RequestBody User user) {
        userService.updateUser(user);
    }
    @DeleteMapping("/deleteUser/{id}")
    public void deleteUser(@PathVariable ("id")int id) {
        userService.deleteUser(id);
    }


    @PostMapping("/login")
    public void createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) throws Exception {}
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        String authorizationHeader = request.getHeader(TOKEN_HEADER);
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            token = authorizationHeader.substring(7);
            tokenBlacklistService.addToBlacklist(token);

        }
        return ResponseEntity.ok("Logout successful");
    }
}
