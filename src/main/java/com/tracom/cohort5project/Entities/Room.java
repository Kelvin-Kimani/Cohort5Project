package com.tracom.cohort5project.Entities;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Entity
@Table(name = "room")
public class Room{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomId;
    private String roomName;
    private int roomCapacity;
    private boolean roomWhiteboardPresent;
    private boolean roomTVPresent;
    private boolean roomPhonePresent;

}
