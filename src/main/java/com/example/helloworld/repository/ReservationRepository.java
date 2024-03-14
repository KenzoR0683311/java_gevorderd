package com.example.helloworld.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.helloworld.domain.Reservation;

/**
 * Repository interface for managing Reservation entities in the database.
 * Extends CrudRepository to provide basic CRUD operations.
 */
@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {
  /**
  * Retrieve all reservations from the database.
  *
  * @return A list of all reservations in the database.
  */
  List<Reservation> findAll();
  
  /**
  * Retrieve a list of reservations associated with a specific user ID.
  *
  * @param userId The ID of the user for whom to retrieve reservations.
  * @return A list of reservations associated with the specified user ID.
  */
  List<Reservation> findByUserId(long userId);
  
  /**
  * Retrieve a reservation with a specific ID associated with a specific user.
  *
  * @param userId        The ID of the user associated with the reservation.
  * @param reservationId The ID of the reservation to retrieve.
  * @return An Optional containing the reservation with the specified ID and user ID, if found.
  */
  Optional<Reservation> findByUserIdAndId(long userId, long ReservationtId);
}
