package com.example.helloworld.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.example.helloworld.domain.User;
import com.example.helloworld.repository.UserRepository;

/**
* Service class for managing User entities.
*
* This class provides methods to interact with user-related data, such as
* retrieving all users, finding a user by ID, and saving a new user.
*/
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    /**
    * Retrieve a list of all users or filter users by a part of their name.
    *
    * If a part of the name is provided, the method returns a list of users
    * whose names contain the specified part. If no part of the name is provided,
    * the method returns a list of all users.
    *
    * @param partOfName Optional parameter for filtering users by a part of their name.
    * @return List of users matching the specified criteria.
    */
    public List<User> findAll(Optional<String> partOfName) {  
      if(partOfName.isPresent()) {
         return userRepository.findByNameContaining(partOfName.get()); 
      } 
      
      return this.userRepository.findAll();
    }
    
    /**
    * Find a user by their ID.
    *
    * This method retrieves a user from the repository based on the provided user ID.
    *
    * @param id The ID of the user to be retrieved.
    * @return Optional containing the retrieved user, or empty if no user is found.
    */
    public Optional<User> findById(long id) {
      return this.userRepository.findById(id);
    }

    /**
    * Save a new user.
    *
    * This method saves a new user to the repository.
    *
    * @param user The user to be saved.
    */
    public User save(User user) {
      return this.userRepository.save(user);
    }
}
