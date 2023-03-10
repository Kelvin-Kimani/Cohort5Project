package com.tracom.cohort5project.Entities;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomId;

    private String roomName;

    private int roomCapacity;

    private String whiteboard;

    private String displayScreen;

    private String conferencePhone;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    @ToString.Exclude
    private Organization organization;

    @OneToMany(mappedBy = "room")
    @ToString.Exclude
    private List<Meeting> meeting;
}
