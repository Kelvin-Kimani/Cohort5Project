package com.tracom.cohort5project.Entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String employeeName;
    private String userRole;

    //User defined
    private String employeeDepartment;
    private String employeeGender;
    private String employeeEmailAddress;
    private String employeePhoneNumber;
    private String password;

    @ManyToOne
    private Organization organization;
}
