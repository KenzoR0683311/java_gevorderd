package com.example.helloworld.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.*;

import io.swagger.annotations.ApiModelProperty;

/**
* Entity class representing a user.
*
* This class is annotated with JPA annotations to map it to a corresponding database table.
* @Table(name="user_tbl") Specifies the name of the mapped database table as "user_tbl."
*/
@Entity
@Table(name="user_tbl")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private long id;
    @NotBlank(message = "Name is required")
    @ApiModelProperty(example = "test")
    private String name;
    @NotNull(message = "birthDate is required")
    private Date birthDate;
    @NotNull(message = "Email is required")
    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @ApiModelProperty(example = "test@email.com")
    private String email;
    
    public User() {}
    
    /**
    * Parameterized constructor to create a new user.
    *
    * @param name      The first name of the user.
    * @param birthDate The birth date of the user.
    * @param email     The email address of the user.
    */
    public User(String name, Date birthDate, String email) {
        this.name = name;
        this.birthDate = birthDate;
        this.email = email;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Date getBirthDate() {
        return this.birthDate;
    }

    public String getEmail() {
        return this.email;
    }
    
    public void setName(String name) {
      this.name = name;
    }

    public void setBirthDate(Date birthDate) {
      this.birthDate = birthDate;
    }

    public void setEmail(String email) {
      this.email = email;
    }
}

