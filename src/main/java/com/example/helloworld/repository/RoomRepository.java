package com.example.helloworld.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.helloworld.domain.Campus;
import com.example.helloworld.domain.Room;

/**
 * Repository interface for managing {@code Room} entities.
 *
 * This repository provides CRUD (Create, Read, Update, Delete) operations for rooms
 * and additional custom queries to retrieve specific room information.
 * 
 */
@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {
  /**
  * Retrieves a room based on the specified room ID.
  *
  * @param roomId The ID of the room to be retrieved.
  * @return An {@code Optional<Room>} object representing the room with the specified ID, if found.
  */
  Optional<Room> findById(long roomId);

  /**
  * Retrieves a room based on the specified campus ID and room ID.
  *
  * @param campus_id The ID of the campus to which the room belongs.
  * @param roomId    The ID of the room to be retrieved.
  * @return An {@code Optional<Room>} object representing the room with the specified IDs, if found.
  */
  Optional<Room> findByIdAndCampusId(long roomId, String campusId);
  
  /**
  * Retrieves a list of rooms based on the specified campus ID and availability criteria.
  *
  * @param campusId           The ID of the campus for which rooms are to be retrieved.
  * @param availableFrom      Optional parameter specifying the minimum available time.
  * @param availableUntil     Optional parameter specifying the maximum available time.
  * @param minNumberOfSeats   Optional parameter specifying the minimum number of seats.
  * @return A list of {@code Room} objects meeting the specified availability criteria.
  */
  @Query("SELECT DISTINCT r FROM Room r JOIN r.campus c JOIN r.reservations res WHERE c.id = :campusId " +
       "AND (:availableFrom IS NULL OR res.endTime >= :availableFrom) " +
       "AND (:availableUntil IS NULL OR res.startTime <= :availableUntil) " +
       "AND (:minNumberOfSeats IS NULL OR r.capacity >= :minNumberOfSeats)")
  List<Room> findByCampusIdAndAvailability(@Param("campusId") String campusId,
                                           @Param("availableFrom") Date availableFrom,
                                           @Param("availableUntil") Date availableUntil,
                                           @Param("minNumberOfSeats") Integer minNumberOfSeats);
  
  /**
  * Retrieves a specific room based on the specified campus ID, Room ID and availability criteria.
  *
  * @param campusId           The ID of the campus for which rooms are to be retrieved.
  * @param availableFrom      Optional parameter specifying the minimum available time.
  * @param availableUntil     Optional parameter specifying the maximum available time.
  * @param minNumberOfSeats   Optional parameter specifying the minimum number of seats.
  * @return A Optional Room objects meeting the specified availability criteria.
  */
  @Query("SELECT DISTINCT r FROM Room r JOIN r.campus c JOIN r.reservations res WHERE c.id = :campusId " +
       "AND r.id = :roomId " + 
       "AND (:availableFrom IS NULL OR res.endTime >= :availableFrom) " +
       "AND (:availableUntil IS NULL OR res.startTime <= :availableUntil) " +
       "AND (:minNumberOfSeats IS NULL OR r.capacity >= :minNumberOfSeats)")
  Optional<Room> findByIdAndCampusIdAndAvailability(@Param("campusId") String campusId,
                                                    @Param("roomId") long roomId,
                                                    @Param("availableFrom") Date availableFrom,
                                                    @Param("availableUntil") Date availableUntil,
                                                    @Param("minNumberOfSeats") Integer minNumberOfSeats);

  /**
  * Checks if a room with the given name exists in the specified campus.
  *
  * @param name   The name of the room.
  * @param campus The campus to which the room belongs.
  * @return true if a room with the specified name exists in the given campus, otherwise false.
  */
  boolean existsByNameAndCampus(String name, Campus campus);
}
