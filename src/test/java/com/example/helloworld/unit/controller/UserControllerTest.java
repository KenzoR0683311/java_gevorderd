package com.example.helloworld.unit.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.helloworld.controller.UserController;
import com.example.helloworld.domain.User;
import com.example.helloworld.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    public UserControllerTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllWithoutPartOfName() {
        List<User> mockUsers = new ArrayList<>();
        when(userService.findAll(Optional.empty())).thenReturn(mockUsers);
        ResponseEntity<List<User>> response = userController.findAll(Optional.empty());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUsers, response.getBody());
    }

    @Test
    public void findAllWithPartOfName() {
        String partOfName = "John";
        List<User> mockFilteredUsers = new ArrayList<>();
        when(userService.findAll(Optional.of(partOfName))).thenReturn(mockFilteredUsers);
        ResponseEntity<List<User>> response = userController.findAll(Optional.of(partOfName));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockFilteredUsers, response.getBody());
    }

    @Test
    public void findByIdExistingUser() {
        int userId = 1;
        User mockUser = new User();
        when(userService.findById(userId)).thenReturn(Optional.of(mockUser));
        ResponseEntity<?> response = userController.findById(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
    }

    @Test
    public void findtByIdNonExistingUser() {
        int userId = 2;
        when(userService.findById(userId)).thenReturn(Optional.empty());
        ResponseEntity<?> response = userController.findById(userId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No user with ID: " + userId + " found.", response.getBody());
    }

    @Test
    public void saveSuccess() {
        User newUser = new User();
        when(userService.save(newUser)).thenReturn(newUser);
        ResponseEntity<User> response = userController.save(newUser);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newUser, response.getBody());
    }
}

