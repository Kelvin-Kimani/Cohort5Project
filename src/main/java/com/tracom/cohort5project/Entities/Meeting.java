package com.tracom.cohort5project.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

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
    private Date meetingDate;
    private LocalTime startTime;
//    public Time startTime;
//    public Time endTime;

    private int capacity;


    /** --- Owners and Co-owners (TO-DO) -----**/

    /** --- Relationships -----**/
    @OneToOne
    @JoinColumn(name = "room_id")
    private Room room;


}
