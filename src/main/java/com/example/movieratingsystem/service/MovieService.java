package com.example.movieratingsystem.service;

import com.example.movieratingsystem.exception.ResourceNotFoundException;
import com.example.movieratingsystem.model.Movie;
import com.example.movieratingsystem.repository.MovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable; // ✨ Bonus
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    @Cacheable("movies") // ✨ Bonus: Cache the result of this method
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie createMovie(Movie movie) {
        // Add validation if needed
        return movieRepository.save(movie);
    }
}
