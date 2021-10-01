package com.tracom.cohort5project.Entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int organizationId;
    private String organizationName;
    private String organizationDescription;

    @OneToMany(mappedBy = "organization")
    private List<User> user = new ArrayList<>();

    public Organization(String organizationName, List<User> user) {
        this.organizationName = organizationName;
        this.user = user;
    }
}
