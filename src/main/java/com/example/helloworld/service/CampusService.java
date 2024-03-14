package com.example.helloworld.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.example.helloworld.domain.Campus;
import com.example.helloworld.repository.CampusRepository;

/**
* Service class for managing Campus entities.
*
* This service class provides methods to interact with campus-related data, 
* such as saving a new campus or retrieving an existing campus by ID.
*/
@Service
public class CampusService {
  @Autowired
  private CampusRepository campusRepository;
  
  /**
  * Retrieves all campuses from the repository.
  *
  * @return A list of Campus objects
  *
  */
  public List<Campus> findAll() {
    return this.campusRepository.findAll();
  }

  /**
  * Find a campus by its ID.
  *
  * This method retrieves a campus from the repository based on the provided campus ID.
  *
  * @param campusId The ID of the campus to be retrieved.
  * @return Optional containing the retrieved campus, or empty if no campus is found.
  */
  public Optional<Campus> findById(String campusId) {
    return this.campusRepository.findById(campusId);
  }

  /**
  * Save a new campus.
  *
  * This method checks if a campus with the specified ID already exists in the repository.
  * If it exists, the method returns an empty Optional. If not, the campus is saved,
  * and an Optional containing the saved campus is returned.
  *
  * @param campus The campus to be saved.
  * @return Optional containing the saved campus, or empty if the campus already exists.
  */
  public Campus save(Campus campus) {
    return campusRepository.save(campus);
  }
}
