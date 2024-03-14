package com.example.helloworld.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.helloworld.domain.User;

/**
* Repository interface for managing User entities in the database.
* Extends CrudRepository to provide basic CRUD operations.
*/
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
  /**
  * Retrieve all users from the database.
  *
  * @return A list of all users in the database.
  */ 
  List<User> findAll();
  
  /**
  * Retrieve a list of users whose names contain the specified partial name.
  *
  * @param partOfName The partial name to search for.
  * @return A list of users matching the search criteria.
  */
  List<User> findByNameContaining(String partOfName);
}

