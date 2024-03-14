package com.example.helloworld.unit.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.helloworld.domain.Campus;
import com.example.helloworld.repository.CampusRepository;
import com.example.helloworld.repository.RoomRepository;
import com.example.helloworld.service.CampusService;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CampusServiceTest {

    @InjectMocks
    private CampusService campusService;

    @Mock
    private CampusRepository campusRepository;

    @Mock
    private RoomRepository roomRepository;
    
    public CampusServiceTest() {
        // Initialize the mocks before each test method
        MockitoAnnotations.initMocks(this);
    }
 
    @Test
    public void findAllSuccess() {
      Campus campus1 = new Campus("1", "Campus One", 50);
      Campus campus2 = new Campus("2", "Campus Two", 50);
      List<Campus> campuses = Arrays.asList(campus1, campus2);

      when(campusRepository.findAll()).thenReturn(campuses);
      List<Campus> result = campusService.findAll();

      assertEquals(2, result.size());
      assertEquals(campus1, result.get(0));
      assertEquals(campus2, result.get(1));
      verify(campusRepository, times(1)).findAll();
    }


    @Test
    public void findByIdSuccess() {
        Campus campus = new Campus("CampusA", "AddressA", 50);
        when(campusRepository.findById("CampusA")).thenReturn(Optional.of(campus));
        Optional<Campus> result = campusService.findById("CampusA");

        assertTrue(result.isPresent());
        assertEquals(campus, result.get());
    }

    @Test
    public void findByIdFailedCampusDoesNotExsists() {
      Campus campus = new Campus("CampusA", "AddressA", 50);
      when(campusRepository.findById("CampusA")).thenReturn(Optional.of(campus));
      Optional<Campus> result = campusService.findById("CampusB");
      
      assertFalse(result.isPresent());
    }
    
    @Test
    public void saveCampusSuccess() {
        Campus campus = new Campus("CampusA", "AddressA", 50);

        when(campusRepository.existsById("CampusA")).thenReturn(false);
        when(campusRepository.save(campus)).thenReturn(campus);

        Campus result = campusService.save(campus);

        assertEquals(campus, result);
    }
}

