package com.tracom.cohort5project.Repositories;

import com.tracom.cohort5project.Entities.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Integer> {

}
