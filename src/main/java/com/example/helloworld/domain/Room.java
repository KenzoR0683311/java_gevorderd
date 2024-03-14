package com.example.helloworld.domain;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

/**
* Entity class representing a Room.
*
* This class is annotated with JPA annotations to map it to a corresponding database table.
*
* @Table(name="room_tbl") Specifies the name of the mapped database table as "room_tbl."
* @Entity Marks the class as a JPA entity, indicating its association with a database table.
* 
*/
@Entity
@Table(name="room_tbl")
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
  @ApiModelProperty(hidden = true)
	private long id;
  @NotBlank(message = "Name is required")
  private String name;
  @NotBlank(message = "Type is required")
	private String type;
  @Min(value = 0L, message = "you can not have a negative amount of capacity")
	private int capacity;
  private int floor;
  
  @ManyToOne
  @JoinColumn(name = "campus_fk") 
  @JsonIgnore
  Campus campus;
  
  @ManyToMany(mappedBy = "rooms", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Reservation> reservations = new ArrayList<Reservation>();

  public Room() {}

  /**
  * Parameterized constructor to create a new room.
  *
  * @param name          The name of the room.
  * @param type          The type of room.
  * @param capacity      The capacity of the room.
  * @param floor         The floor the room is in.
  */
	public Room(String name, String type, int capacity, int floor) {
		this.name = name;
		this.type = type;
		this.capacity = capacity;
		this.floor = floor;
	}

  public long getId() {
    return this.id;
  }

	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}

	public int getCapacity() {
		return this.capacity;
	}

	public int getFloor() {
		return this.floor;
	}
  
  public void setName(String name) {
    this.name = name;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public void setFloor(int floor) {
    this.floor = floor;
  }

  public void setCampus(Campus campus) {
    this.campus = campus;
  }

  public void addReservation(Reservation res) {
    this.reservations.add(res);
  }

  public List<Reservation> getReservations() {
    return this.reservations;
  }
}
