package com.example.helloworld.unit.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.helloworld.controller.CampusController;
import com.example.helloworld.domain.Campus;
import com.example.helloworld.service.CampusService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CampusControllerTest {

    @Mock
    private CampusService campusService;

    @InjectMocks
    private CampusController campusController;
    
    public CampusControllerTest() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void findAllSuccess() {
        List<Campus> campuses = Arrays.asList(new Campus(), new Campus());
        when(campusService.findAll()).thenReturn(campuses);
        ResponseEntity<List<Campus>> response = campusController.findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(campuses, response.getBody());
    }

    @Test
    public void findByIdExisting() {
        String campusId = "123";
        Campus mockCampus = new Campus(campusId, "Test Address", 10);
        when(campusService.findById(campusId)).thenReturn(Optional.of(mockCampus));
        ResponseEntity<?> response = campusController.findById(campusId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCampus, response.getBody());
    }

    @Test
    public void findByIdNonExisting() {
        String campusId = "123";
        when(campusService.findById(campusId)).thenReturn(Optional.empty());
        ResponseEntity<?> response = campusController.findById(campusId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No campus with ID: " + campusId + " found.", response.getBody());
    }

    @Test
    public void saveSuccess() {
        Campus newCampus = new Campus("456", "New Campus Address", 20);
        when(campusService.findById(newCampus.getId())).thenReturn(Optional.empty());
        ResponseEntity<?> response = campusController.save(newCampus);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newCampus, response.getBody());
    }

    @Test
    public void saveFailedExistingCampus() {
        Campus existingCampus = new Campus("789", "Existing Campus Address", 30);
        when(campusService.findById(existingCampus.getId())).thenReturn(Optional.of(existingCampus));
        ResponseEntity<?> response = campusController.save(existingCampus);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("campus with given name already exsists.", response.getBody());
    }
}

