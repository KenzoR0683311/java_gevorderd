package com.example.helloworld.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.helloworld.service.*;

import io.swagger.annotations.ApiOperation;

import com.example.helloworld.domain.*;

@RestController 
public class ReservationController {
  @Autowired
  private ReservationService reservationService;

  @GetMapping("users/{userId}/reservations")
  @ApiOperation( value = "Get all reservations" )
  public ResponseEntity<List<Reservation>> findAll(@PathVariable long userId) {
    List<Reservation> reservations =  this.reservationService.findAll(userId);
    return new ResponseEntity<List<Reservation>>(reservations, HttpStatus.OK); 
  }

  @GetMapping("users/{userId}/reservations/{reservationId}")
  @ApiOperation( value = "Get a reservation by id" )
  public ResponseEntity<?> findById(@PathVariable long userId, @PathVariable long reservationId) {
      Optional<Reservation> optionalReservation = this.reservationService.findById(reservationId, userId);
      
      if (optionalReservation.isEmpty()) {
        return new ResponseEntity<String>("Reservation with user id: " + userId + " and reservation id: " + reservationId + " not found", HttpStatus.NOT_FOUND);
      }

      Reservation reservation = optionalReservation.get();
      return new ResponseEntity<Reservation>(reservation, HttpStatus.OK);
  } 

  @PostMapping("users/{userId}/reservations")
  @ApiOperation( value = "add reservation" )
  public ResponseEntity<?> save(@PathVariable long userId, @RequestBody Reservation reservation) {
    Optional<Reservation> optionalReservation = this.reservationService.save(userId, reservation);

    if(optionalReservation.isEmpty()) {
      return new ResponseEntity<String>("Cannot add the reservation, user with id: " + userId + " Does not exsists", HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<Reservation>(reservation, HttpStatus.CREATED);
  } 
  
  @PutMapping("users/{userId}/reservations/{reservationId}/rooms/{roomId}")
  @ApiOperation( value = "add a room to a reservation")
  public ResponseEntity<String> addRoom(@PathVariable long userId, @PathVariable long reservationId, @PathVariable long roomId) {
    Optional<Reservation> addedReservation = this.reservationService.addRoom(userId, reservationId, roomId);
    
    if (addedReservation.isEmpty()) {
      return new ResponseEntity<String>("Failed to add room to reservation. Check if the reservation id, room id and user id exist or if there is any overlapping booking.", HttpStatus.NOT_FOUND);
    } 

    return new ResponseEntity<String>("Room added to reservation successfully", HttpStatus.CREATED);
  } 
}
