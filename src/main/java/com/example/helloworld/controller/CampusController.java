package com.example.helloworld.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.*;
import com.example.helloworld.domain.Campus;
import com.example.helloworld.service.*;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/campus")
public class CampusController {
    @Autowired
    private CampusService campusService;
    
    @GetMapping()
    @ApiOperation( value = "Get all campuses" )
    public ResponseEntity<List<Campus>> findAll() {
      List<Campus> users = this.campusService.findAll();
      return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    @ApiOperation( value = "Get campuses by id" )
    public ResponseEntity<?> findById(@PathVariable String id) {
      Optional<Campus> optionalCampus = this.campusService.findById(id);

      if (optionalCampus.isEmpty()) {
        return new ResponseEntity<String>("No campus with ID: " + id +  " found.", HttpStatus.NOT_FOUND);
      }

      return new ResponseEntity<Campus>(optionalCampus.get(), HttpStatus.OK);
    }

    @PostMapping()
    @ApiOperation( value = "Add Campus" )
    public ResponseEntity<?> save(@Valid @RequestBody Campus campus) {
      Optional<Campus> optionalCampus = this.campusService.findById(campus.getId());

      if (optionalCampus.isPresent()) {
        return new ResponseEntity<String>("campus with given name already exsists.", HttpStatus.FORBIDDEN);
      }
      
      this.campusService.save(campus);
      return new ResponseEntity<Campus>(campus, HttpStatus.CREATED);
    }
}
