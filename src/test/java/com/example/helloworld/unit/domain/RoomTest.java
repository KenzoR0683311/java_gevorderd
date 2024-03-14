package com.example.helloworld.unit.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.example.helloworld.domain.Reservation;
import com.example.helloworld.domain.Room;

import javax.validation.Validation; import javax.validation.Validator;
import javax.validation.ValidatorFactory;


class RoomTest {
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @InjectMocks
    private Room room;
    
    public RoomTest() {
        // Initialize the mocks before each test method
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createdSuccess() {
      Room room = new Room("RoomA", "Yay", 25, 3);
      assertEquals(room.getName(), "RoomA");
      assertEquals(room.getType(), "Yay");
      assertEquals(room.getCapacity(), 25);
      assertEquals(room.getFloor(), 3);
    }

    
    @Test
    public void addReservationSuccess() {
      Reservation mockReservation = mock(Reservation.class);
      Room room = new Room("Room 1", "Type 1", 10, 1);
      room.addReservation(mockReservation);
      List<Reservation> reservations = room.getReservations();
      assertEquals(1, reservations.size());
    }
  

    @Test
    public void createdFailedNameMayNotBeBlank() {
      Room room = new Room("", "Yay", 25, 3);
      
      Set<ConstraintViolation<Room>> violations = validator.validate(room);
      assertEquals(1, violations.size());
      assertEquals("Name is required", violations.iterator().next().getMessage());
    }

    @Test
    public void createdFailedTypeMayNotBeBlank() {
      Room room = new Room("roomA", "", 25, 3);
      
      Set<ConstraintViolation<Room>> violations = validator.validate(room);
      assertEquals(1, violations.size());
      assertEquals("Type is required", violations.iterator().next().getMessage());
    }

    @Test
    public void createdFailedCapacityMayNotBeNegative() {
      Room room = new Room("roomA", "yay", -5, 3);
      
      Set<ConstraintViolation<Room>> violations = validator.validate(room);
      assertEquals(1, violations.size());
      assertEquals("you can not have a negative amount of capacity", violations.iterator().next().getMessage());
    }
}
