package com.example.assignment.service;

import com.example.assignment.model.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
@Component
@Service
public interface UserService {
    User addUser(User user);
    User updateUser(Integer id, User user);
    List<User> getAllUsers();
}
