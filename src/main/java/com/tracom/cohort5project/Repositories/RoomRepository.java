package com.tracom.cohort5project.Repositories;

import com.tracom.cohort5project.Entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    @Query("SELECT r FROM Room r WHERE r.roomId = ?1")
    Room findByRoomId(int roomId);

    @Query("SELECT COUNT(r.roomId) FROM Room r")
    int numberOfRooms();

    @Query("SELECT COUNT(r.roomId) FROM Room r WHERE r.organization.organizationId =?1")
    int numberOfRoomsInOrganization(int organizationId);

    @Query("SELECT r FROM Room r WHERE r.organization.organizationId = ?1")
    List<Room> findAllByOrganization(int organization);

    @Override
    void deleteById(Integer roomId);
}
