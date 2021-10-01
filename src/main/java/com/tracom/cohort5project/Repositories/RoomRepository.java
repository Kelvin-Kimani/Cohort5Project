package com.tracom.cohort5project.Repositories;

import com.tracom.cohort5project.Entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    Room findByRoomId(int roomId);
}
