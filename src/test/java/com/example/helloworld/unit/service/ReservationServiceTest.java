package com.example.helloworld.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.helloworld.domain.Reservation;
import com.example.helloworld.domain.Room;
import com.example.helloworld.domain.User;
import com.example.helloworld.repository.ReservationRepository;
import com.example.helloworld.repository.RoomRepository;
import com.example.helloworld.repository.UserRepository;
import com.example.helloworld.service.ReservationService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

//@SpringBootTest
class ReservationServiceTest {
    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoomRepository roomRepository;
    
    public ReservationServiceTest() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void FindAllSuccess() {
        int userId = 1;
        Reservation reservation1 = new Reservation(); // Assuming you have a Reservation class
        Reservation reservation2 = new Reservation();
        List<Reservation> mockReservations = new ArrayList<>();
        mockReservations.add(reservation1);
        mockReservations.add(reservation2);

        when(reservationRepository.findByUserId(userId)).thenReturn(mockReservations);
        List<Reservation> foundReservations = reservationService.findAll(userId);

        assertEquals(2, foundReservations.size());
        assertEquals(reservation1, foundReservations.get(0));
        assertEquals(reservation2, foundReservations.get(1));
    }

    @Test
    public void findByIdSuccess() {
        int userId = 1;
        int reservationId = 1;
        Reservation mockReservation = new Reservation(); 
        Optional<Reservation> mockOptionalReservation = Optional.of(mockReservation);

        when(reservationRepository.findByUserIdAndId(userId, reservationId)).thenReturn(mockOptionalReservation);

        Optional<Reservation> foundReservation = reservationService.findById(reservationId, userId);

        assertTrue(foundReservation.isPresent());
        assertEquals(mockReservation, foundReservation.get());
    }

    @Test
    public void findByIdFailedReservationNotFound() {
      int userId = 1;
      int reservationId = 1;

      when(reservationRepository.findByUserIdAndId(userId, reservationId)).thenReturn(Optional.empty());
      Optional<Reservation> foundReservation = reservationService.findById(reservationId, userId);
       assertTrue(foundReservation.isEmpty());
    }

    @Test
    public void saveSuccess() {
      long userId = 1;
      Reservation reservation = new Reservation(); 
      User user = new User(); 
      Optional<User> optionalUser = Optional.of(user);
      Reservation savedReservation = new Reservation(); 
      savedReservation.setUser(user);

      when(userRepository.findById(userId)).thenReturn(optionalUser);
      when(reservationRepository.save(reservation)).thenReturn(savedReservation);

      Optional<Reservation> result = reservationService.save(userId, reservation);

      assertTrue(result.isPresent());
      assertEquals(savedReservation, result.get());
      verify(userRepository, times(1)).findById(userId);
      verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    public void SaveFailedUserNotFound() {
      long userId = 1;
      Reservation reservation = new Reservation(); 
      Optional<User> optionalUser = Optional.empty();

      when(userRepository.findById(userId)).thenReturn(optionalUser);
      Optional<Reservation> savedReservation = reservationService.save(userId, reservation);

      assertTrue(savedReservation.isEmpty());
      verify(userRepository, times(1)).findById(userId);
      verify(reservationRepository, never()).save(any());
    }

    @Test
    public void addRoomFailedReservationOrRoomNotFound() {
      int userId = 1;
      int reservationId = 1;
      int roomId = 1;
      Optional<Reservation> optionalReservation = Optional.empty();
      Optional<Room> optionalRoom = Optional.empty();

      when(reservationRepository.findByUserIdAndId(userId, reservationId)).thenReturn(optionalReservation);
      when(roomRepository.findById(roomId)).thenReturn(optionalRoom);

      Optional<Reservation> result = reservationService.addRoom(userId, reservationId, roomId);

      assertTrue(result.isEmpty());
      verify(reservationRepository, times(1)).findByUserIdAndId(userId, reservationId);
      verify(roomRepository, times(1)).findById(roomId);
      verify(reservationRepository, never()).save(any());
    }
    
    @Test
    public void addRoomFailedOverlappingBooking() {
      int userId = 1;
      int reservationId = 1;
      int roomId = 1;
      Room room = new Room(); 

      Reservation reservation = new Reservation(new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000), "test"); 
      reservation.addRoom(room);
      Optional<Reservation> optionalReservation = Optional.of(reservation);
      Optional<Room> optionalRoom = Optional.of(room);

      when(reservationRepository.findByUserIdAndId(userId, reservationId)).thenReturn(optionalReservation);
      when(roomRepository.findById(roomId)).thenReturn(optionalRoom);
      when(reservationRepository.findAll()).thenReturn(List.of(reservation));

      Optional<Reservation> result = reservationService.addRoom(userId, reservationId, roomId);

      assertTrue(result.isEmpty());
    }
    
    @Test
    public void addRoomSuccess() {
      int userId = 1;
      int reservationId = 1;
      int roomId = 1;
      Room room = new Room(); 
      Reservation reservation = new Reservation(); 
      Optional<Reservation> optionalReservation = Optional.of(reservation);
      Optional<Room> optionalRoom = Optional.of(room);

      when(reservationRepository.findByUserIdAndId(userId, reservationId)).thenReturn(optionalReservation);
      when(roomRepository.findById(roomId)).thenReturn(optionalRoom);
      when(reservationRepository.findAll()).thenReturn(List.of());

      Optional<Reservation> result = reservationService.addRoom(userId, reservationId, roomId);

      assertTrue(result.isPresent());
      assertEquals(reservation, result.get());
      assertTrue(reservation.getRooms().contains(room));
    }
}

