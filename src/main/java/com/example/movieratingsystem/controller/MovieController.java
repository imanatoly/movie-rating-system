package com.example.movieratingsystem.controller;

import com.example.movieratingsystem.model.Movie;
import com.example.movieratingsystem.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@AllArgsConstructor
@Tag(name = "Movies", description = "Endpoints for managing movies") // ✨ Bonus
public class MovieController {

    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

    private final MovieService movieService;


    /**
     * PUBLIC ENDPOINT.
     * Anyone can access this because of our rule in SecurityConfig.
     * No annotation is needed here.
     */
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a movie by its ID")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        logger.error("Fetching movie with ID: {}", id);
        logger.trace("This is a TRACE message. Very detailed, rarely seen.");
        logger.debug("This is a DEBUG message. For development and troubleshooting.");
        logger.info("This is an INFO message. The application is running as expected.");
        logger.warn("This is a WARN message. Something unexpected happened, but it's not critical.");
        logger.error("This is an ERROR message. A serious error occurred.");
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    /**
     * PROTECTED ENDPOINT.
     * Only users with the 'ADMIN' role can access this.
     * The @PreAuthorize annotation enforces this. If a regular user or
     * an unauthenticated user tries to call this, they will get a 403 Forbidden error.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        Movie savedMovie = movieService.createMovie(movie);
        return ResponseEntity.ok(savedMovie);
    }
}
