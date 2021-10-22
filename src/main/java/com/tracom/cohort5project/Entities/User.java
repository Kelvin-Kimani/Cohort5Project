package com.tracom.cohort5project.Entities;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String employeeFirstName;
    private String employeeLastName;
    private String userRole;

    //User defined
    private String employeeDepartment;
    //private String employeeGender;
    private String employeeEmailAddress;
    private String employeePhoneNumber;
    private String password;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
}
