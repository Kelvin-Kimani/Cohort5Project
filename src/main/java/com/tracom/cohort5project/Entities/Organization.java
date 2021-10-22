package com.tracom.cohort5project.Entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int organizationId;
    private String organizationName;
    private String organizationDescription;

    @OneToMany(mappedBy = "organization")
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "organization")
    private List<Room> rooms = new ArrayList<>();

    public Organization(String organizationName, List<User> users) {
        this.organizationName = organizationName;
        this.users = users;
    }
}
