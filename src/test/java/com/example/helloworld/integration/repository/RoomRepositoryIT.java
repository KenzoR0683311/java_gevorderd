package com.example.helloworld.integration.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.helloworld.domain.Campus;
import com.example.helloworld.domain.Room;
import com.example.helloworld.repository.CampusRepository;
import com.example.helloworld.repository.RoomRepository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class RoomRepositoryIT {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private CampusRepository campusRepository;
    
    @Test
    public void findByIdSuccess() {
        Room room = new Room("Room 101", "test", 50, 3); 
        roomRepository.save(room);

        Optional<Room> foundRoomOptional = roomRepository.findById(room.getId());

        assertTrue(foundRoomOptional.isPresent());
        assertEquals(room.getName(), foundRoomOptional.get().getName());
        assertEquals(room.getCapacity(), foundRoomOptional.get().getCapacity());
    }

    @Test
    public void findByIdAndCampusIdSuccess() {
      String campusId = "campus_1"; 
      Campus campus = new Campus(campusId, "Leuven", 55);
      Campus savedCampus = campusRepository.save(campus);

      Room room = new Room("Room 201", "test", 75, 3); 
      room.setCampus(savedCampus);
      roomRepository.save(room);

      Optional<Room> foundRoomOptional = roomRepository.findByIdAndCampusId(room.getId(), campusId);

      assertTrue(foundRoomOptional.isPresent());
      assertEquals(room.getName(), foundRoomOptional.get().getName());
      assertEquals(room.getCapacity(), foundRoomOptional.get().getCapacity());
    }

    @Test
    public void existsByNameAndCampus_Success() {
        String campusId = "campus_3";

        Campus campus = new Campus(campusId, "London", 80);
        campusRepository.save(campus);

        Room room = new Room("Room 501", "test", 120, 6);
        room.setCampus(campus);
        roomRepository.save(room);

        boolean roomExists = roomRepository.existsByNameAndCampus(room.getName(), campus);

        assertTrue(roomExists);
    }
}   
    

