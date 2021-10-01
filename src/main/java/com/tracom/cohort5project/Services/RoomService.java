package com.tracom.cohort5project.Services;

import com.tracom.cohort5project.Entities.Room;
import com.tracom.cohort5project.Repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class RoomService {

    private RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository){
        this.roomRepository = roomRepository;
    }

    /*CREATE*/
    public void createRoom(Room room){
        roomRepository.save(room);
    }

    /*READ*/
    public void showRooms(){
        roomRepository.findAll();
    }
}
