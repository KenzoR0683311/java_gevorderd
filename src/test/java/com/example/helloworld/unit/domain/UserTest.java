package com.example.helloworld.unit.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.example.helloworld.domain.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


class UserTest {
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @InjectMocks
    private User user;
    
    public UserTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createdSuccess() {
      User user = new User("bob", new Date(), "bob.test@email.com");
      assertEquals(user.getName(), "bob");
      assertEquals(user.getEmail(), "bob.test@email.com");
    }
    
    @Test
    public void createdFailedNameMayNotBeBlank() {
        User user = new User("", new Date(), "bob.test@email.com");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Name is required", violations.iterator().next().getMessage());
    }

    @Test
    public void createdFailedBirthDateMayNotBeNull() {
        User user = new User("bob", null, "bob.test@email.com");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("birthDate is required", violations.iterator().next().getMessage());
    }

    @Test
    public void createdFailedEmailIsNotValid() {
        User user = new User("bob", new Date(), "bob.teemail.com");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Email is not valid", violations.iterator().next().getMessage());
    }
}
