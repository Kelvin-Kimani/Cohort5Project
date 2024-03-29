package com.tracom.cohort5project.Repositories;

import com.tracom.cohort5project.Entities.Meeting;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MeetingRepository extends JpaRepository<Meeting, Integer> {

    @Query("SELECT m FROM Meeting m WHERE m.meetingId =?1")
    Meeting findMeetingByMeetingId(int meetingId);

    @Query("SELECT m FROM Meeting m WHERE m.organization.organizationId = ?1")
    List<Meeting> findMeetingByOrganization(int organizationId);

    @Query("SELECT m FROM Meeting m WHERE m.organization.organizationId = ?1 ORDER BY DATE(m.meetingDate) ASC, TIME(m.startTime) ASC")
    List<Meeting> findMeetingByOrganizationOrderByTime(int organizationId);

    @Query("SELECT m FROM Meeting m WHERE m.organization.organizationId = ?1 AND m.meetingDate = CURRENT_DATE")
    List<Meeting> findMeetingByOrganizationAndToday(int organizationId);

    @Query("SELECT m FROM Meeting m WHERE m.organization.organizationId = ?1 AND m.meetingDate >= CURRENT_DATE ORDER BY DATE(m.meetingDate) ASC, TIME(m.startTime) ASC")
    List<Meeting> findMeetingByOrganizationOrderByTimeAndLaterDate(int organizationId);

    @Query("SELECT m FROM Meeting m WHERE m.organization.organizationId = ?1 AND m.meetingDate < CURRENT_DATE ORDER BY DATE(m.meetingDate) ASC, TIME(m.startTime) ASC")
    List<Meeting> findMeetingByOrganizationOrderByTimeAndPastDate(int organizationId);

    @Query("SELECT COUNT (m.meetingId) FROM Meeting m WHERE m.organization.organizationId = ?1 AND m.meetingDate >= CURRENT_DATE")
    int numberOfMeetingsTobeAttendedByOrganization(int organizationId);

    @Query("SELECT COUNT (m.meetingId) FROM Meeting m WHERE m.organization.organizationId = ?1 AND m.meetingDate < CURRENT_DATE")
    int numberOfMeetingsAttendedByOrganization(int organizationId);

    void deleteById(Integer meetingId);
}
