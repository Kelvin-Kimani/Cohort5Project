package com.tracom.cohort5project.Entities;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String userRole;

    //User defined
    private String employeeFirstName;
    private String employeeLastName;
    private String employeeDepartment;
    private String employeeEmailAddress;
    private String employeePhoneNumber;
    private String password;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

}
