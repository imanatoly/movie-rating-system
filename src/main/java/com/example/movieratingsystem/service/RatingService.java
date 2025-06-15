package com.example.movieratingsystem.service;

import com.example.movieratingsystem.dto.CreateRatingRequest;
import com.example.movieratingsystem.exception.ResourceNotFoundException;
import com.example.movieratingsystem.model.*;
import com.example.movieratingsystem.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    @Transactional
    public Rating rateMovie(String username, CreateRatingRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Movie movie = movieRepository.findById(request.movieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        // Check if a rating already exists and update it, or create a new one
        Rating rating = ratingRepository.findByUserAndMovie(user, movie)
                .orElse(new Rating());

        rating.setUser(user);
        rating.setMovie(movie);
        rating.setScore(request.score());
        return ratingRepository.save(rating);
    }

    @Transactional
    public void deleteRating(Long ratingId) {
        ratingRepository.deleteById(ratingId);
    }

    public List<Rating> getRatingsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return ratingRepository.findByUser(user);
    }
}
