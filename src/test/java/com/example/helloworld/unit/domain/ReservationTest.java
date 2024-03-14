package com.example.helloworld.unit.domain;

import org.junit.jupiter.api.Test;

import com.example.helloworld.domain.Reservation;
import com.example.helloworld.domain.Room;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ReservationTest {    
    @Test
    public void createdSuccess() {
      Date startTime = new Date();
      Date endTime = new Date(startTime.getTime() + 1000);
      Reservation reservation = new Reservation(startTime, endTime, "this is a comment");
      assertEquals(reservation.getComment(), "this is a comment");
      assertEquals(reservation.getStartTime(), startTime);
      assertEquals(reservation.getEndTime(), endTime);
    }

    @Test
    public void reservationSetEndTimeIsInThePast() {
      Reservation reservation = new Reservation();
      Date startTime = new Date();

      Date pastEndTime = new Date(startTime.getTime() - 1000);
        assertThatThrownBy(() -> reservation.setEndTime(pastEndTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Reservation end date cannot be in the past");
    }
        
    @Test
    public void reservationSetStartTimeInvalidPeriod() {
      Reservation reservation = new Reservation();
      Date startTime = new Date();
      Date endTime = new Date(startTime.getTime() + 3600000); // Adding 1 hour to startTime
      
      assertThatThrownBy(() -> reservation.setStartTime(new Date(endTime.getTime() + 1000)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Invalid reservation period");
    }

     @Test
    public void addRoomSuccess() {
        Room mockRoom = mock(Room.class);
        Reservation reservation = new Reservation();
        reservation.addRoom(mockRoom);
        List<Room> rooms = reservation.getRooms();
        assertTrue(rooms.contains(mockRoom));
        verify(mockRoom, times(1)).addReservation(reservation);
    }

    @Test
    public void hasRoomReturnsTrue() {
        Room room = new Room("Room 1", "Type 1", 10, 1);
        Reservation reservation = new Reservation();
        reservation.addRoom(room);
        assertTrue(reservation.hasRoom(room));
    }

    @Test
    public void hasRoomReturnsFalse() {
        Room room = new Room("Room 1", "Type 1", 10, 1);
        Reservation reservation = new Reservation();
        assertFalse(reservation.hasRoom(room));
    }
}
