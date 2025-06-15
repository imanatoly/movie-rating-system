package com.example.movieratingsystem.repository;

import com.example.movieratingsystem.model.Movie;
import com.example.movieratingsystem.model.Rating;
import com.example.movieratingsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByUser(User user);
    Optional<Rating> findByUserAndMovie(User user, Movie movie);
}
