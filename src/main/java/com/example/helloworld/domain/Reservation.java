package com.example.helloworld.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

/**
* Entity class representing a Reservation.
*
* This class is annotated with JPA annotations to map it to a corresponding database table.
*
* @Table(name="reservation_tbl") Specifies the name of the mapped database table as "reservation_tbl."
* @Entity Marks the class as a JPA entity, indicating its association with a database table.
* 
*/
@Entity
@Table(name="reservation_tbl")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;    
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @ApiModelProperty(example = "this is a test comment")
    private String comment;
    
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "reservation_room_tbl",
        joinColumns = @JoinColumn(name = "reservation_id"),
        inverseJoinColumns = @JoinColumn(name = "room_id"))
    private List<Room> rooms = new ArrayList<Room>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_fk")
    User user;

    public Reservation() {}
    
    /**
    * Parameterized constructor to create a new Constructor.
    *
    * @param startTime The start time of the reservation.
    * @param endTime   The end time of the reservation.
    * @param comment   Additional comments or notes related to the reservation.
    */
    public Reservation(Date startTime, Date endTime, String comment) {
      this.setEndTime(endTime);
      this.setStartTime(startTime);
      this.comment = comment;
    } 

    public long getId() {
      return this.id;
    }
    
    public Date getStartTime() {
      return this.startTime;
    }

    public Date getEndTime() {
      return this.endTime;
    }
    
    public String getComment() {
      return this.comment;
    }

    public List<Room> getRooms() {
      return this.rooms;
    } 
    
    /**
    * Calculates and retrieves the maximum capacity of the reserved rooms.
    *
    * @return The maximum capacity.
    */
    public int getmaxCapacity() {
      int maxCapacity = 0;
      
      if(rooms != null) {
        for(Room room : rooms) {
          maxCapacity += room.getCapacity();
        }
      }
      
      return maxCapacity;  
    }

    public void setUser(User user) {
      this.user = user;
    }

    /**
    * Sets the start time of the reservation, performing validation checks.
    *
    * @param startTime The start time to be set.
    * @throws IllegalArgumentException If the start time is null, the end time is null,
    *                                  or the start time is after the end time.
    */
    public void setStartTime(Date startTime) {
      if (startTime == null || endTime == null || startTime.after(endTime)) {
        throw new IllegalArgumentException("Invalid reservation period");
      }

      this.startTime = startTime;
    }

    /**
    * Sets the end time of the reservation, performing validation checks.
    *
    * @param endTime The end time to be set.
    * @throws IllegalArgumentException If the end time is in the past.
    */
    public void setEndTime(Date endTime) {
      Date currentTime = new Date();

      if (endTime.before(currentTime)) {
        throw new IllegalArgumentException("Reservation end date cannot be in the past");
      }

      this.endTime = endTime;
    }

    public void setComment(String comment) {
      this.comment = comment;
    }

    public void addRoom(Room room) {
      this.rooms.add(room);
      room.addReservation(this);
    }

    public boolean hasRoom(Room room) {
      boolean hasRoom = false;

      for(Room r : this.rooms) {
        if(r.getName() == room.getName()) {
          hasRoom = true;
        }
      }

      return hasRoom;
    }
}
