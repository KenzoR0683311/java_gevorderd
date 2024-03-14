package com.example.helloworld.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.*;
import com.example.helloworld.service.RoomService;
import io.swagger.annotations.ApiOperation;

import com.example.helloworld.domain.Reservation;
import com.example.helloworld.domain.Room;

@RestController
@RequestMapping
public class RoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping("campus/{campusId}/rooms")
    @ApiOperation( value = "Get all rooms" )
    public ResponseEntity<List<Room>> findAll(@PathVariable String campusId, 
                                             @RequestParam(value = "availableFrom", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date availableFrom,
                                             @RequestParam(value = "availableUntil", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date availableUntil,
                                             @RequestParam(value = "minNumberOfSeats", required = false) Integer minNumberOfSeats) {
      
      List<Room> rooms = this.roomService.findById(campusId, availableFrom, availableUntil, minNumberOfSeats);
      return new ResponseEntity<List<Room>>(rooms, HttpStatus.OK);
    } 
    
    @GetMapping("campus/{campusId}/rooms/{roomId}")
    @ApiOperation( value = "Get room by id" )
    public ResponseEntity<?> findById(@PathVariable String campusId, @PathVariable long roomId) {
      Optional<Room> optionalRoom = roomService.findById(campusId, roomId);
    
      if(optionalRoom.isEmpty()) {
        return new ResponseEntity<String>("Room not found", HttpStatus.NOT_FOUND);
      }
      
      Room room = optionalRoom.get();
      return new ResponseEntity<Room>(room, HttpStatus.OK);
    }

    @GetMapping("campus/{campusId}/rooms/{roomId}/reservations")
    @ApiOperation(value = "Get reservations for a room by id")
    public ResponseEntity<?> findRoomReservations(@PathVariable String campusId, @PathVariable long roomId, 
        @RequestParam(value = "availableFrom", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date availableFrom, 
        @RequestParam(value = "availableUntil", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date availableUntil, 
        @RequestParam(value = "minNumberOfSeats", required = false) Integer minNumberOfSeats) {

        Optional<Room> optionalRoom = roomService.findById(campusId, roomId, availableFrom, availableUntil, minNumberOfSeats);

        if (optionalRoom.isEmpty()) {
          return new ResponseEntity<>("Room not found", HttpStatus.NOT_FOUND);
        }

        Room room = optionalRoom.get();
        List<Reservation> reservations = room.getReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @PostMapping("campus/{campusId}/rooms")
    @ApiOperation( value = "add room by campusId" )
    public ResponseEntity<?> save(@PathVariable String campusId, @Valid @RequestBody Room room) {
      Optional<Room> optionalRoom = this.roomService.save(campusId, room);

      if(optionalRoom.isEmpty()) {
        return new ResponseEntity<String>("Cannot add room, campus with id: " + campusId + " does not exsits.", HttpStatus.NOT_FOUND);
      }

      return new ResponseEntity<Room>(room, HttpStatus.CREATED);
    }
}
