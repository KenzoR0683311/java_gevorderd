package com.example.helloworld.unit.service;

import com.example.helloworld.domain.User;
import com.example.helloworld.repository.UserRepository;
import com.example.helloworld.service.UserService;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    public UserServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAllSuccess() {
        List<User> users = new ArrayList<>();
        users.add(new User("John", new Date(), "john@example.com"));
        users.add(new User("Jane", new Date(), "jane@example.com"));

        when(userRepository.findAll()).thenReturn(users);
        List<User> result = userService.findAll(Optional.empty());

        assertEquals(users, result);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void findAllWithPartOfName() {
        List<User> users = new ArrayList<>();
        users.add(new User("John", new Date(), "john@example.com"));
        when(userRepository.findByNameContaining("John")).thenReturn(users);
        List<User> result = userService.findAll(Optional.of("John"));

        assertEquals(users, result);
        verify(userRepository, times(1)).findByNameContaining("John");
    }

    @Test
    public void findByIdSuccess() {
        User user = new User("John", new Date(), "john@example.com");
        when(userRepository.findById(1l)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById(1);
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1)).findById(1l);
    }

    @Test
    public void findByIdFailedUserNotFound() {
        long userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        Optional<User> result = userService.findById(userId);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void saveUserSuccess() {
        User userToSave = new User("John", new Date(), "john@example.com");
        userService.save(userToSave);
        verify(userRepository, times(1)).save(userToSave);
    }
  }

