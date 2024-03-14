package com.example.helloworld.unit.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.helloworld.controller.RoomController;
import com.example.helloworld.domain.Reservation;
import com.example.helloworld.domain.Room;
import com.example.helloworld.service.RoomService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RoomControllerTest {

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;
    
    public RoomControllerTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllSuccess() {
        String campusId = "campus1";
        Date availableFrom = new Date();
        Date availableUntil = new Date();
        Integer minNumberOfSeats = 10;

        List<Room> mockRooms = new ArrayList<>();
        mockRooms.add(new Room()); 
        when(roomService.findById(campusId, availableFrom, availableUntil, minNumberOfSeats))
                .thenReturn(mockRooms);

        ResponseEntity<List<Room>> responseEntity = roomController.findAll(campusId, availableFrom, availableUntil,
                minNumberOfSeats);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockRooms, responseEntity.getBody());
    }

     @Test
    public void findByIdSuccess() {
        String campusId = "campus1";
        int roomId = 1;

        Room mockRoom = new Room(); 
        when(roomService.findById(campusId, roomId)).thenReturn(Optional.of(mockRoom));

        ResponseEntity<?> responseEntity = roomController.findById(campusId, roomId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockRoom, responseEntity.getBody());
    }

    @Test
    public void findByIdFailedRoomNotFound() {
        String campusId = "campus1";
        int roomId = 1;

        when(roomService.findById(campusId, roomId)).thenReturn(Optional.empty());
        ResponseEntity<?> responseEntity = roomController.findById(campusId, roomId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Room not found", responseEntity.getBody());
    }
      
    @Test
    public void findRoomReservationsSuccess() {
        String campusId = "campus1";
        int roomId = 1;
        Date availableFrom = new Date(System.currentTimeMillis());
        Date availableUntil = new Date(System.currentTimeMillis() + 1000);
        Integer minNumberOfSeats = 10;
        Reservation r = new Reservation(availableFrom, availableUntil, "comment");
        
        Room mockRoom = new Room();
        List<Reservation> mockReservations = new ArrayList<>();
        mockReservations.add(r); 
        mockRoom.addReservation(r); 
        when(roomService.findById(campusId, roomId, availableFrom, availableUntil, minNumberOfSeats))
                .thenReturn(Optional.of(mockRoom));

        ResponseEntity<?> responseEntity = roomController.findRoomReservations(campusId, roomId, availableFrom,
                availableUntil, minNumberOfSeats);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockReservations, responseEntity.getBody());
    }
    

    @Test
    public void findRoomReservationsFailedRoomNotFound() {
        String campusId = "campus1";
        int roomId = 1;
        Date availableFrom = new Date();
        Date availableUntil = new Date();
        Integer minNumberOfSeats = 10;

        // Mock response from RoomService
        when(roomService.findById(campusId, roomId, availableFrom, availableUntil, minNumberOfSeats))
                .thenReturn(Optional.empty());

        // Call the method
        ResponseEntity<?> responseEntity = roomController.findRoomReservations(campusId, roomId, availableFrom,
                availableUntil, minNumberOfSeats);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Room not found", responseEntity.getBody());
    }

    @Test
    public void saveRoomSuccess() {
      String campusId = "campus1";
      Room room = new Room();

      when(roomService.save(campusId, room)).thenReturn(Optional.of(room));
      ResponseEntity<?> responseEntity = roomController.save(campusId, room);

      assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
      assertEquals(room, responseEntity.getBody());
    }

    @Test
    public void saveRoomFailedCampusNotFound() {
      String campusId = "campus1";
      Room room = new Room();

      when(roomService.save(campusId, room)).thenReturn(Optional.empty());
      ResponseEntity<?> responseEntity = roomController.save(campusId, room);

      assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
      assertEquals("Cannot add room, campus with id: campus1 does not exsits.", responseEntity.getBody());
    }
}

