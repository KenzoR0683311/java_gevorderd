package com.example.helloworld.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.*;

/**
* Entity class representing a campus.
*
* This class is annotated with JPA annotations to map it to a corresponding database table.
* @Table(name="campus_tbl") Specifies the name of the mapped database table as "canpus_tbl."
*/
@Entity
@Table(name="campus_tbl")
public class Campus {
    @Id
    @NotBlank(message = "id is required")
    private String id;
    @NotBlank(message = "address is required")
    private String address;
    @Min(value = 0L, message = "negative amount of parking spaces is not allowed")
    private int parkingSpaces;
    
    @OneToMany(mappedBy = "campus", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Room> rooms;
    
    @Transient
    @JsonIgnore
    private int totalAmountOfRooms;

    public Campus() {}

    /**
    * Parameterized constructor to create a new campus.
    *
    * @param id            The unique identifier for the campus.
    * @param address       The address of the campus.
    * @param parkingSpaces The number of parking spaces available at the campus.
    */
    public Campus(String id, String address, int parkingSpaces) {
        this.id = id;
        this.address = address;
        this.parkingSpaces = parkingSpaces;
    }

    public String getId() {
        return this.id;
    }

    public String getAddress() {
        return this.address;
    }

    public int getParkingSpaces() {
        return this.parkingSpaces;
    }
    
    /**
    * Get the dynamically calculated total amount of rooms at the campus.
    *
    * @return The total amount of rooms at the campus.
    */
    public int getTotalAmountOfRooms() {
        return rooms != null ? rooms.size() : 0;
    }

    public void setId(String id) {
      this.id = id;
    }

    public void setAddress(String address) {
      this.address = address;
    }

    public void setParkingSpaces(int parkingSpaces) {
      this.parkingSpaces = parkingSpaces;
    }

    public void setRooms(List<Room> asList) {
      this.rooms = asList;
    }
}

