package com.tracom.cohort5project.Repositories;

import com.tracom.cohort5project.Entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    Room findByRoomId(int roomId);

    @Query("SELECT COUNT(r.roomId) FROM Room r")
    int numberOfRooms();

    @Override
    void deleteById(Integer roomId);
}
