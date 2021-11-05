package com.tracom.cohort5project.Services;

import com.tracom.cohort5project.Entities.Meeting;
import com.tracom.cohort5project.Repositories.MeetingRepository;
import com.tracom.cohort5project.Repositories.OrganizationRepository;
import com.tracom.cohort5project.Repositories.RoomRepository;
import com.tracom.cohort5project.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
