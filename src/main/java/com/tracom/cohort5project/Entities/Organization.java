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
    @ToString.Exclude
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "organization")
    @ToString.Exclude
    private List<Room> rooms = new ArrayList<>();

    @OneToMany(mappedBy = "organization")
    @ToString.Exclude
    private List<Meeting> meetings = new ArrayList<>();

    public Organization(String organizationName, List<User> users) {
        this.organizationName = organizationName;
        this.users = users;
    }
}
