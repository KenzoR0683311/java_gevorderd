package com.example.helloworld.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.helloworld.domain.Campus;

/**
* Repository interface for managing Campus entities in the database.
* Extends CrudRepository to provide basic CRUD operations.
*/
@Repository
public interface CampusRepository extends JpaRepository<Campus, String> {}
