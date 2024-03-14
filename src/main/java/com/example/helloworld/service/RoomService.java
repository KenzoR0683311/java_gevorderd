package com.example.helloworld.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.helloworld.domain.*;
import com.example.helloworld.repository.CampusRepository;
import com.example.helloworld.repository.RoomRepository;

/**
* RoomService handles business logic related to Room entities.
*
* This service class provides methods to interact with room-related data, 
* such as saving a new room, retrieving rooms by various criteria, and retrieving a room by its ID.
*/
@Service
public class RoomService {
    private RoomRepository roomRepository;
    private CampusRepository campusRepository;
    
    @Autowired
    public RoomService(RoomRepository roomRepository, CampusRepository campusRepository) {
      this.roomRepository = roomRepository;
      this.campusRepository = campusRepository; 
    }
    
    /**
    * Find rooms for a specified campus.
    *
    * This method retrieves all rooms associated with the specified campus ID.
    *
    * @param campusId           ID of the campus for which rooms are to be retrieved.
    * @param availableFrom      Optional parameter for filtering rooms available from a specific date.
    * @param availableUntil     Optional parameter for filtering rooms available until a specific date.
    * @param minNumberOfSeats   Optional parameter for filtering rooms with a minimum number of seats.
    * @return List of rooms associated with the specified campus ID.
    */
    public List<Room> findById( String campusId, Date availableFrom, Date availableUntil, Integer minNumberOfSeats) {
      return roomRepository.findByCampusIdAndAvailability(campusId, availableFrom, availableUntil, minNumberOfSeats);
    }

    /**
    * Find a room by its ID within a specified campus.
    *
    * This method retrieves a room from the specified campus based on the room ID.
    *
    * @param campusId            ID of the campus for which the room is to be retrieved.
    * @param roomId              ID of the room to be retrieved.
    * @return Optional containing the retrieved room, or empty if no room is found.
    */
    public Optional<Room> findById(String campusId, long roomId) {
      return roomRepository.findByIdAndCampusId(roomId, campusId);
    }

    /**
    * Find a room by its ID within a specified campus.
    *
    * This method retrieves a room from the specified campus based on the room ID.
    *
    * @param campusId            ID of the campus for which the room is to be retrieved.
    * @param roomId              ID of the room to be retrieved.
    * @param availableFrom      Optional parameter for filtering rooms available from a specific date.
    * @param availableUntil     Optional parameter for filtering rooms available until a specific date.
    * @param minNumberOfSeats   Optional parameter for filtering rooms with a minimum number of seats.
    * @return Optional containing the retrieved room, or empty if no room is found.
    */
    public Optional<Room> findById( String campusId, long roomId,
                                    Date availableFrom,
                                    Date availableUntil,
                                    Integer minNumberOfSeats) {
      
      return this.roomRepository.findByIdAndCampusIdAndAvailability(campusId, roomId, availableFrom, availableUntil, minNumberOfSeats);
    }

    /**
    * Save a new room for a specified campus.
    *
    * This method checks if the specified campus exists, sets the campus for the room,
    * and then checks if a room with the same name already exists for the campus.
    * If the campus does not exist or if the room name is not unique, the method returns an empty Optional.
    * Otherwise, it saves the room and returns an Optional containing the saved room.
    *
    * @param campus_id ID of the campus where the room belongs.
    * @param room      The room to be saved.
    * @return Optional containing the saved room, or empty if the campus does not exist
    * or if a room with the same name already exists for the campus.
    */
    public Optional<Room> save(String campus_id, Room room) {
      Optional<Campus> optionalCampus = campusRepository.findById(campus_id);

      if(optionalCampus.isEmpty()) {
        return Optional.empty();
      }

      Campus campus = optionalCampus.get();
      room.setCampus(campus);

      if (roomRepository.existsByNameAndCampus(room.getName(), campus)) {
        return Optional.empty();
      }

      return Optional.of(roomRepository.save(room));
    }
 }

