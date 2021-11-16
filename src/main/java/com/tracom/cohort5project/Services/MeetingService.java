package com.tracom.cohort5project.Services;

import com.tracom.cohort5project.Entities.Meeting;
import com.tracom.cohort5project.Entities.User;
import com.tracom.cohort5project.Repositories.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class MeetingService {

    private MeetingRepository meetingRepository;


    @Autowired
    public MeetingService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    /*CREATE*/
    public void createMeeting(Meeting meeting){
        meetingRepository.save(meeting);
    }

    /*READ*/
    public Meeting getMeeting(int meetingId){
        return meetingRepository.findMeetingByMeetingId(meetingId);
    }
    public List<Meeting> getOrganizationMeetings(int organizationId){
        return meetingRepository.findMeetingByOrganization(organizationId);
    }

    public List<Meeting> getOrganizationMeetingsOrderByTime(int organizationId){
        return meetingRepository.findMeetingByOrganizationOrderByTime(organizationId);
    }

    public List<Meeting> getOrganizationMeetingsToday(int organizationId){
        return meetingRepository.findMeetingByOrganizationAndToday(organizationId);
    }

    public List<Meeting> getOrganizationMeetingsForLaterDate(int organizationId){
        return meetingRepository.findMeetingByOrganizationOrderByTimeAndLaterDate(organizationId);
    }

    public List<Meeting> getOrganizationMeetingsForPastDate(int organizationId){
        return meetingRepository.findMeetingByOrganizationOrderByTimeAndPastDate(organizationId);
    }

    public int numberOfMeetingsToBeAttendedByOrganization(int organizationId){
        return meetingRepository.numberOfMeetingsTobeAttendedByOrganization(organizationId);
    }

    public int numberOfMeetingsAttendedByOrganization(int organizationId){
        return meetingRepository.numberOfMeetingsAttendedByOrganization(organizationId);
    }

}
