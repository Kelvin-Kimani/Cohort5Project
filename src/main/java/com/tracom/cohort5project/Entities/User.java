package com.tracom.cohort5project.Entities;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_details")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    private String userRole;

    private String employeeFirstName;

    private String employeeLastName;

    private String employeeDepartment;

    private String employeeEmailAddress;

    private String employeePhoneNumber;

    private String password;

    private String resetPasswordToken;

    private String setPasswordToken;

    private boolean accountNonLocked = true;

    private int failedAttempts;

    private Date lockTime;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    @ToString.Exclude
    private Organization organization;

    @ManyToMany(mappedBy = "users")
    @ToString.Exclude
    private List<Meeting> meetings;
}
