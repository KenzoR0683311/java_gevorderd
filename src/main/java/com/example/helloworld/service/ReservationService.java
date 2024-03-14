package com.example.helloworld.service;

import java.util.List;
import java.util.Optional;

import com.example.helloworld.domain.Reservation;
import com.example.helloworld.domain.Room;
import com.example.helloworld.domain.User;
import com.example.helloworld.repository.ReservationRepository;
import com.example.helloworld.repository.RoomRepository;
import com.example.helloworld.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* RoomService handles business logic related to Room entities.
*
* This service class provides methods to interact with room-related data, 
* such as saving a new room, retrieving rooms by various criteria, and retrieving a room by its ID.
*/

@Service
public class ReservationService {
    private ReservationRepository reservationRepository;
    private UserRepository userRepository;
    private RoomRepository roomRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository, RoomRepository roomRepository) {
      this.reservationRepository = reservationRepository;
      this.userRepository = userRepository;
      this.roomRepository = roomRepository;
    }

    /**
    * Retrieve a list of all reservations from a certain user.
    *
    * @param user_id
    * @return List of reservations.
    */
    public List<Reservation> findAll(long user_id) {
      return reservationRepository.findByUserId(user_id);
    } 

    /**
    * Find a reservation by their ID from a given user.
    *
    * This method retrieves a user from the repository based on the provided user ID.
    *
    * @param id The ID of the user to be retrieved.
    * @return Optional containing the retrieved user, or empty if no user is found.
    */
    public Optional<Reservation> findById(long reservationId, long userId) { 
      return reservationRepository.findByUserIdAndId(userId, reservationId);
    }
    
    /**
    * Save a new reservation associated with a user.
    *
    * This method saves a new reservation to the repository, associating it with the user specified
    * by the given user ID. If the user with the provided ID is not found, the method returns an empty
    * Optional. Otherwise, the reservation is saved to the repository and an Optional containing the
    * saved reservation is returned.
    *
    * @param userId      The ID of the user associated with the reservation.
    * @param reservation The reservation to be saved.
    * @return An Optional containing the saved reservation if the user is found and the reservation is
    *         successfully saved, or an empty Optional if the user is not found.
    */
    public Optional<Reservation> save(long userId, Reservation reservation) {
      Optional<User> optionalUser = this.userRepository.findById(userId);

      if (optionalUser.isEmpty()) {
        return Optional.empty();
      }

      User user = optionalUser.get();
      reservation.setUser(user);
      Reservation savedReservation = this.reservationRepository.save(reservation);
      return Optional.of(savedReservation);
    }

    /**
    * Adds a room to an existing reservation for a specific user.
    *
    * This method first checks if both the reservation and the room exist. If either the reservation
    * or the room is not found, the method returns an empty Optional. Otherwise, the room is added
    * to the reservation, and the updated reservation is persisted to the repository.
    *
    * @param userId         The ID of the user associated with the reservation.
    * @param reservationId  The ID of the reservation to which the room will be added.
    * @param roomId         The ID of the room to be added to the reservation.
    * @return               An Optional containing the updated Reservation if the addition is successful,
    *                       or an empty Optional if either the reservation or the room is not found.
    */   
    public Optional<Reservation> addRoom(long userId, long reservationId, long roomId) {
      Optional<Reservation> optionalReservation = reservationRepository.findByUserIdAndId(userId, reservationId);
      Optional<Room> optionalRoom = roomRepository.findById(roomId);
      
      if(optionalReservation.isEmpty() || optionalRoom.isEmpty()) {
        return Optional.empty();
      }

      Room room = optionalRoom.get();
      Reservation reservation = optionalReservation.get();
      
      if(checkForOverlappingBooking(reservation, room, userId)) {
        return Optional.empty();
      }
     
      reservation.addRoom(room);
      reservationRepository.save(reservation);
      return Optional.of(reservation);
    }
    
    /**
    * Checks if there is any overlapping booking for a given reservation and room.
    *
    * This method iterates through all reservations in the repository to check for overlapping time
    * intervals. If an overlap is found and the room is already booked.
    *
    * @param reservation The reservation for which to check overlapping bookings.
    * @param room        The room to be checked for overlapping bookings.
    * @param userId      The ID of the user associated with the reservation.
    * @return True if there is an overlapping booking with the provided room, false otherwise.
    */
    private boolean checkForOverlappingBooking(Reservation reservation, Room room, long userId) { 
      boolean bookingOverlaps = false;
      List<Reservation> reservations = reservationRepository.findAll();

      for(Reservation r : reservations) {

        if((reservation.getStartTime().compareTo(r.getStartTime()) >= 0 && reservation.getStartTime().compareTo(r.getEndTime()) <= 0) ||
           (reservation.getEndTime().compareTo(r.getStartTime()) >= 0 && reservation.getEndTime().compareTo(r.getEndTime()) <= 0)) {
          
          bookingOverlaps =  r.hasRoom(room);

          if(bookingOverlaps == true) {
            return true;
          }
        }      
      }

      return bookingOverlaps;
    }
}
