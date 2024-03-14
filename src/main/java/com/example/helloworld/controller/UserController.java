package com.example.helloworld.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.*;

import com.example.helloworld.domain.User;
import com.example.helloworld.service.UserService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

	  @GetMapping()
    @ApiOperation( value = "Get all users" )
    public ResponseEntity<List<User>> findAll(@RequestParam(value = "partOfName", required = false) Optional<String> partOfName) {
      List<User> users = this.userService.findAll(partOfName);
      return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation( value = "Get user by id" )
    public ResponseEntity<?> findById(@PathVariable long id) {
      Optional<User> optionalUser = userService.findById(id);

      if (optionalUser.isEmpty()) {
        return new ResponseEntity<String>("No user with ID: " + id +  " found.", HttpStatus.NOT_FOUND);
      }

      return new ResponseEntity<User>(optionalUser.get(), HttpStatus.OK);
    }

    @PostMapping()
    @ApiOperation( value = "Add user" )
    public ResponseEntity<User> save(@Valid @RequestBody User user) {
      this.userService.save(user);
      return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }
}
