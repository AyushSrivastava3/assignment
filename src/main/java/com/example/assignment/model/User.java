package com.example.assignment.model;

import com.example.assignment.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String username;
    private String dateTimeFormat;
    private String firstName;
    private String middleName;
    private String lastName;
    private String contact;
    private LocalDate expirationDate;
    private String description;
    private Integer timeout;
    private Role roleNameList;
    private String scopeNameList;
    private String primaryGroupName;
    private String secondaryGroupNameList;
    private String Language;
    private String organisation;
    private String timezone;
    private String memo;
    private String email;
    private String password;

}
