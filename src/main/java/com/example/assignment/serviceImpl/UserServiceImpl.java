package com.example.assignment.serviceImpl;

import com.example.assignment.model.User;
import com.example.assignment.repository.UserRepository;
import com.example.assignment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private static final PasswordEncoder passwordencoder=new BCryptPasswordEncoder();


    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Add User Implementation
    @Override
    public User addUser(User user) {
        String password=user.getPassword();
        user.setPassword(passwordencoder.encode(password));
        return userRepository.save(user);
    }

    // Update User Implementation
    @Override
    public User updateUser(Integer id, User user) {
        Optional<User> existingUserOptional = userRepository.findById(id);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setUsername(user.getUsername());
            existingUser.setDateTimeFormat(user.getDateTimeFormat());
            existingUser.setFirstName(user.getFirstName());
            existingUser.setMiddleName(user.getMiddleName());
            existingUser.setLastName(user.getLastName());
            existingUser.setContact(user.getContact());
            existingUser.setExpirationDate(user.getExpirationDate());
            existingUser.setDescription(user.getDescription());
            existingUser.setTimeout(user.getTimeout());
            existingUser.setRoleNameList(user.getRoleNameList());
            existingUser.setScopeNameList(user.getScopeNameList());
            existingUser.setPrimaryGroupName(user.getPrimaryGroupName());
            existingUser.setSecondaryGroupNameList(user.getSecondaryGroupNameList());
            existingUser.setLanguage(user.getLanguage());
            existingUser.setOrganisation(user.getOrganisation());
            existingUser.setTimezone(user.getTimezone());
            existingUser.setMemo(user.getMemo());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());

            return userRepository.save(existingUser);
        } else {
            throw new RuntimeException("User with ID " + id + " not found.");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }



    public boolean authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

}
