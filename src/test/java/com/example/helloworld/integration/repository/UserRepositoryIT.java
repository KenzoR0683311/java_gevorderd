package com.example.helloworld.integration.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.helloworld.domain.User;
import com.example.helloworld.repository.UserRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;
    
    @Test
    public void findAllSuccess() {
        User user1 = new User("Alice", new Date(), "alice@example.com");
        User user2 = new User("Bob", new Date(), "bob@example.com");

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();

        assertEquals(2, users.size());
    }

    @Test
    public void findAllByPartialName() {
        User user1 = new User("alice", new Date(), "alice@example.com");
        User user2 = new User("bob", new Date(), "bob@example.com");
        User user3 = new User("carol", new Date(), "carol@example.com");

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        List<User> usersContainingA = userRepository.findByNameContaining("a");

        assertEquals(2, usersContainingA.size());
        assertTrue(usersContainingA.contains(user1));
        assertTrue(usersContainingA.contains(user3));
    }
}


