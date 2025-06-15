package com.example.movieratingsystem.service;

import com.example.movieratingsystem.model.User;
import com.example.movieratingsystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void shouldLoadUserByUsername() {
        UserRepository userRepository = mock(UserRepository.class);
        UserDetailsServiceImpl userService = new UserDetailsServiceImpl(userRepository);

        User user = new User("john", "john", "password");
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername("john");

        assertNotNull(result);
        assertEquals("john", result.getUsername());
    }
}
