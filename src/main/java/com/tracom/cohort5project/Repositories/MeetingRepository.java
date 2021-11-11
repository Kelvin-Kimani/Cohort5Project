package com.tracom.cohort5project.Repositories;

import com.tracom.cohort5project.Entities.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Integer> {

    @Query("SELECT m FROM Meeting m WHERE m.meetingId =?1")
    Meeting findMeetingByMeetingId(int meetingId);

    @Query("SELECT m FROM Meeting m WHERE m.organization.organizationId = ?1")
    List<Meeting> findMeetingByOrganization(int organizationId);

    @Query("SELECT m FROM Meeting m WHERE m.organization.organizationId = ?1 ORDER BY DATE(m.meetingDate) ASC, TIME(m.startTime) ASC")
    List<Meeting> findMeetingByOrganizationOrderByTime(int organizationId);

    @Query("SELECT m FROM Meeting m WHERE m.organization.organizationId = ?1 AND m.meetingDate = CURRENT_DATE")
    List<Meeting> findMeetingByOrganizationAndToday(int organizationId);

}
