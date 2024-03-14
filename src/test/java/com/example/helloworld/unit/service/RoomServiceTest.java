package com.example.helloworld.unit.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.helloworld.domain.Campus;
import com.example.helloworld.domain.Room;
import com.example.helloworld.repository.CampusRepository;
import com.example.helloworld.repository.RoomRepository;
import com.example.helloworld.service.RoomService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomServiceTest {

    @InjectMocks
    private RoomService roomService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private CampusRepository campusRepository;

    public RoomServiceTest() {
        // Initialize the mocks before each test method
        MockitoAnnotations.initMocks(this);
    }
    
    
    @Test 
    public void findRoomsByCampusIdSuccess() {
      String campusId = "campusA";
      Date availableFrom = new Date();
      Date availableUntil = new Date();
      int minNumberOfSeats = 10;

      Campus campus = new Campus("CampusA", "AddressA", 50);
      
      List<Room> rooms = new ArrayList<>();

      Room room1 = new Room("RoomA", "Classroom", 5, 1);
      room1.setCampus(campus);
      rooms.add(room1);

      Room room2 = new Room("RoomB", "Classroom", 30, 2);
      room1.setCampus(campus);
      rooms.add(room2);

      when(roomRepository.findByCampusIdAndAvailability(campusId, availableFrom, availableUntil, minNumberOfSeats)).thenReturn(rooms);
      List<Room> foundRooms = roomService.findById(campusId, availableFrom, availableUntil, minNumberOfSeats);

      assertEquals(2, foundRooms.size());
    }

    @Test
    public void findByCanpusAndRoomIdSuccess() {
        Room room = new Room("Campus A", "Room 101", 5, 5);
        when(roomRepository.findByIdAndCampusId(1, "Campus A")).thenReturn(Optional.of(room));
        Optional<Room> foundRoom = roomService.findById("Campus A", 1);
        assertTrue(foundRoom.isPresent());
        assertEquals(room, foundRoom.get());
    }

    @Test
    public void findByCampusAndRoomIdFaileRoomDoesNotExist() {
        when(roomRepository.findByIdAndCampusId(1, "Campus A")).thenReturn(Optional.empty());
        Optional<Room> foundRoom = roomService.findById("Campus A", 1);
        assertFalse(foundRoom.isPresent());
    }

    @Test
    public void FindByCampusAndRoomIdFailedInvalidCampus() {
        when(roomRepository.findByIdAndCampusId(1, "Invalid Campus")).thenReturn(Optional.empty());
        Optional<Room> foundRoom = roomService.findById("Invalid Campus", 1);
        assertFalse(foundRoom.isPresent());
    }

    @Test
    public void saveRoomSuccess() {
        Campus campus = new Campus("CampusA", "AddressA", 50);
        Room room = new Room("RoomA", "Classroom", 30, 1);
        
        when(campusRepository.findById("CampusA")).thenReturn(Optional.of(campus));
        when(roomRepository.existsByNameAndCampus("RoomA", campus)).thenReturn(false);
        when(roomRepository.save(room)).thenReturn(room);

        Optional<Room> result = roomService.save("CampusA", room);

        assertTrue(result.isPresent());
        assertEquals(room, result.get());

        verify(roomRepository, times(1)).save(room);
    }

    @Test
    public void saveRoomFailedCampusNotFound() {
        Room room = new Room("RoomA", "Classroom", 30, 1);

        Optional<Room> result = roomService.save("NonExistentCampus", room);

        assertFalse(result.isPresent());
        verify(roomRepository, never()).save(room);
    }

    @Test
    public void saveRoomRoomFailedAlreadyExists() {
        Campus campus = new Campus("CampusA", "AddressA", 50);
        Room room = new Room("RoomA", "Classroom", 30, 1);
        
        when(campusRepository.findById("CampusA")).thenReturn(Optional.of(campus));
        when(roomRepository.existsByNameAndCampus("RoomA", campus)).thenReturn(true);

        Optional<Room> result = roomService.save("CampusA", room);
        assertFalse(result.isPresent());
        verify(roomRepository, never()).save(room);
    }
}
