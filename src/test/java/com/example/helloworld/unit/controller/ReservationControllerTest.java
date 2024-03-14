package com.example.helloworld.unit.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.helloworld.controller.ReservationController;
import com.example.helloworld.domain.Reservation;
import com.example.helloworld.service.ReservationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    public ReservationControllerTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findAllSuccess() {
        int userId = 1;       
        List<Reservation> mockReservations = new ArrayList<>();
        when(reservationService.findAll(userId)).thenReturn(mockReservations);
        ResponseEntity<List<Reservation>> response = reservationController.findAll(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockReservations, response.getBody());
    }
    
    @Test
    void findByIdSuccess() {
        int userId = 1;
        int reservationId = 2;
        Reservation mockReservation = new Reservation();
        when(reservationService.findById(reservationId, userId)).thenReturn(Optional.of(mockReservation));
        ResponseEntity<?> response = reservationController.findById(userId, reservationId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockReservation, response.getBody());
    }
    
    @Test
    void findByIdFailedNotFound() {
        int userId = 1;
        int reservationId = 2;
        when(reservationService.findById(reservationId, userId)).thenReturn(Optional.empty());
        ResponseEntity<?> response = reservationController.findById(userId, reservationId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Reservation with user id: " + userId + " and reservation id: " + reservationId + " not found", response.getBody());
    }
    
    @Test
    void saveSuccess() {
        int userId = 1;
        Reservation newReservation = new Reservation();
        reservationController.save(userId, newReservation);
        verify(reservationService, times(1)).save(userId, newReservation);
    }
    
    
    @Test
    void addRoomSuccess() {
        int userId = 1;
        int reservationId = 2;
        int roomId = 3;
        reservationController.addRoom(userId, reservationId, roomId);
        verify(reservationService, times(1)).addRoom(userId, reservationId, roomId);
    }

    @Test
    public void addRoomFailure() {
        when(reservationService.addRoom(anyInt(), anyInt(), anyInt())).thenReturn(Optional.empty());
        ResponseEntity<String> response = reservationController.addRoom(1, 1, 1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Failed to add room to reservation. Check if the reservation id, room id and user id exist or if there is any overlapping booking.", response.getBody());
    }
}

