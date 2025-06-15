package com.example.movieratingsystem.service;

import com.example.movieratingsystem.dto.CreateRatingRequest;
import com.example.movieratingsystem.model.*;
import com.example.movieratingsystem.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private RatingService ratingService;

    @Test
    void testRateMovie_NewRating() {
        // Arrange
        String username = "testuser";
        Long movieId = 1L;
        CreateRatingRequest request = new CreateRatingRequest(movieId, 5);

        User user = new User();
        user.setUsername(username);

        Movie movie = new Movie();
        movie.setId(movieId);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(ratingRepository.findByUserAndMovie(user, movie)).thenReturn(Optional.empty());
        when(ratingRepository.save(any(Rating.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Rating result = ratingService.rateMovie(username, request);

        // Assert
        assertNotNull(result);
        assertEquals(5, result.getScore());
        assertEquals(user, result.getUser());
        assertEquals(movie, result.getMovie());
        verify(ratingRepository, times(1)).save(any(Rating.class));
    }
}
