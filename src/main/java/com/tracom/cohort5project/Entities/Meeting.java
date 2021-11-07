package com.tracom.cohort5project.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int meetingId;
    private String meetingName;
    private String meetingDescription;

    /** --- Date and Time -----**/
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date meetingDate;

    @DateTimeFormat(pattern = "HH:mm:ss")
    @Temporal(TemporalType.TIME)
    private Date startTime;


//    public Time startTime;
//    public Time endTime;

    private int capacity;
    private int ownerId;

    /** --- Owners and Co-owners (TO-DO) -----**/
    @ManyToMany
    @JoinTable(
            name = "users_meetings",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;

    /** --- Other Relationships -----**/
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

}
