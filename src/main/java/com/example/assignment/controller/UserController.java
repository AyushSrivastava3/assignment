package com.example.assignment.controller;
import com.example.assignment.dtos.UserLoginDto;
import com.example.assignment.model.User;
import com.example.assignment.repository.UserRepository;
import com.example.assignment.service.UserDetailsServiceImpl;
import com.example.assignment.service.UserService;
import com.example.assignment.utilis.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j

public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtUtil jwtUtil;

    private static final PasswordEncoder passwordencoder=new BCryptPasswordEncoder();
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginDto user) {

        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }catch (Exception e){
            log.error("Exception occurred while createAuthenticationToken ", e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            //return new ResponseEntity<>("Username already exists",userRepository.findByUsername(user.getUsername()) );
            return new ResponseEntity<>(userRepository.findByUsername(user.getUsername()),HttpStatus.CONFLICT);
        }
        User savedUser = userService.addUser(user);
        return ResponseEntity.ok(savedUser);
    }

    // Update User API
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestParam User user){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUsername(authentication.getName());
        return new ResponseEntity<>("User deleted Successfully",HttpStatus.ACCEPTED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // API to check if the JWT token is expired
    @GetMapping("/api/check-token")
    public ResponseEntity<String> checkTokenExpiration(@RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Token is missing");
        }

        // Check if the token is expired
        if (jwtUtil.isTokenExpired(token)) {
            return ResponseEntity.status(401).body("Token has expired");
        }

        // If the token is valid and not expired
        return ResponseEntity.ok("Token is valid");
    }

}

