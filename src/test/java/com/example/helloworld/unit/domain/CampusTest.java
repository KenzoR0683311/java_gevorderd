package com.example.helloworld.unit.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.helloworld.domain.Campus;
import com.example.helloworld.domain.Room;

import javax.validation.Validation; import javax.validation.Validator;
import javax.validation.ValidatorFactory;

class CampusTest {
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @InjectMocks
    private Campus campus;

    @Mock
    private Room room1;

    @Mock
    private Room room2;

    public CampusTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createdSuccess() {
      Campus campus = new Campus("campusA", "Leuven", 50);
      assertEquals(campus.getId(), "campusA");
      assertEquals(campus.getAddress(), "Leuven");
      assertEquals(campus.getParkingSpaces(), 50);
    }

    @Test
    public void calculateTotalAmountOfRoomsSuccess() {
        Campus campus = new Campus("campusA", "Leuven", 50);
        
        Room mockRoom1 = mock(Room.class);
        Room mockRoom2 = mock(Room.class);
        campus.setRooms(Arrays.asList(mockRoom1, mockRoom2));
        
        int totalAmountOfRooms = campus.getTotalAmountOfRooms();
        assertEquals(2, totalAmountOfRooms);
    }
    
    @Test
    public void createFailedIdMayNotBeBlank() {
        Campus campus = new Campus("", "123 Main St", 50);
        Set<ConstraintViolation<Campus>> violations = validator.validate(campus);
        assertEquals(1, violations.size());
        assertEquals("id is required", violations.iterator().next().getMessage());
    }
    
    @Test
    public void createdFailedAddressMayNotBeBlank() {
        Campus campus = new Campus("KUL", "", 20);
        Set<ConstraintViolation<Campus>> violations = validator.validate(campus);
        assertEquals(1, violations.size());
        assertEquals("address is required", violations.iterator().next().getMessage());
    }

    @Test public void createdFailedParkingSpacesMayNotBeNegative() {
      Campus campus = new Campus("KUL", "123 Main ST", -20);
      Set<ConstraintViolation<Campus>> violations = validator.validate(campus);
      assertEquals(1, violations.size());
      assertEquals("negative amount of parking spaces is not allowed", violations.iterator().next().getMessage());
    }
}

