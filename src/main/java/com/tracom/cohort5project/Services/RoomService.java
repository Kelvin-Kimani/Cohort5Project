package com.tracom.cohort5project.Services;

import com.tracom.cohort5project.Entities.Room;
import com.tracom.cohort5project.Repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
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
    public List<Room> showRooms(){
        return roomRepository.findAll();
    }
}
