package com.tracom.cohort5project.Services;

import com.tracom.cohort5project.Entities.Room;
import com.tracom.cohort5project.Repositories.RoomRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /*CREATE*/
    public void createRoom(Room room) {
        roomRepository.save(room);
    }

    /*READ*/
    public List<Room> showRooms() {
        return roomRepository.findAll();
    }

    public Room getRoom(int roomId) {
        return roomRepository.findByRoomId(roomId);
    }

    public List<Room> showRoomsInOrganization(int organizationId) {
        return roomRepository.findAllByOrganization(organizationId);
    }

    public int noOfRooms() {
        return roomRepository.numberOfRooms();
    }

    public int noOfRoomsInOrganization(int organizationId) {
        return roomRepository.numberOfRoomsInOrganization(organizationId);
    }

    /******************                         DELETE                                          ****************/

    public void deleteRoom(int roomId) {
        roomRepository.deleteById(roomId);
    }
}
