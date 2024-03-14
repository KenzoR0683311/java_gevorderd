package com.example.helloworld.integration.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.helloworld.domain.Reservation;
import com.example.helloworld.domain.User;
import com.example.helloworld.repository.ReservationRepository;
import com.example.helloworld.repository.UserRepository;

@DataJpaTest
public class ReservationRepositoryIT {

    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private UserRepository userRepository; 


    @Test
    public void findAllSuccess() {
        Reservation reservation1 = new Reservation(new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000), "Reservation 1");
        Reservation reservation2 = new Reservation(new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000), "Reservation 2");

        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);

        List<Reservation> reservations = reservationRepository.findAll();

        assertEquals(2, reservations.size());
    }

    @Test
    public void findByUserIdSuccess() {
        User user = new User("John Doe", new Date(), "john@example.com");
        userRepository.save(user);

        Date startTime = new Date();
        Date endTime = new Date(startTime.getTime() + 3600 * 1000);
        Reservation reservation = new Reservation(startTime, endTime, "Test reservation");
        reservation.setUser(user);
        Reservation savedReservation = reservationRepository.save(reservation);

        Optional<Reservation> foundReservation = reservationRepository.findById((long) savedReservation.getId());

        assertTrue(foundReservation.isPresent());
        assertEquals(savedReservation, foundReservation.get()); 
    }

    @Test
    public void findByUserIdAndIdSuccess() {
        User user = new User("John Doe", new Date(), "john@example.com");
        userRepository.save(user);

        Date startTime = new Date();
        Date endTime = new Date(startTime.getTime() + 3600 * 1000);
        Reservation reservation1 = new Reservation(startTime, endTime, "Test reservation 1");
        reservation1.setUser(user);
        reservationRepository.save(reservation1);

        Reservation reservation2 = new Reservation(new Date(), new Date(), "Test reservation 2");
        reservation2.setUser(user);
        reservationRepository.save(reservation2);

        List<Reservation> userReservations = reservationRepository.findByUserId(user.getId());

        assertEquals(2, userReservations.size());
    }
}

