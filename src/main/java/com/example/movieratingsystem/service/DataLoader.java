package com.example.movieratingsystem.service;

import com.example.movieratingsystem.model.Movie;
import com.example.movieratingsystem.model.Rating;
import com.example.movieratingsystem.model.User;
import com.example.movieratingsystem.repository.MovieRepository;
import com.example.movieratingsystem.repository.RatingRepository;
import com.example.movieratingsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final PasswordEncoder passwordEncoder;

    // Pattern to extract year from movie title like "Toy Story (1995)"
    private final Pattern yearPattern = Pattern.compile("\\((\\d{4})\\)");

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (movieRepository.count() > 0) {
            log.info("Database already populated. Skipping data load.");
            return;
        }

        log.info("Starting data load from MovieLens dataset...");

        Map<Long, Movie> movies = loadMovies();
        Map<Long, User> users = loadRatingsAndCreateUsers(movies);

        log.info("Successfully loaded {} movies, {} users, and {} ratings.",
                movies.size(), users.size(), ratingRepository.count());
    }

    private Map<Long, Movie> loadMovies() throws Exception {
        Map<Long, Movie> movieMap = new HashMap<>();
        List<Movie> movieList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("data/movies.csv").getInputStream()))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); // handle commas inside quotes
                if (values.length >= 3) {
                    try {
                        long movieId = Long.parseLong(values[0]);
                        String titleWithYear = values[1];
                        String genres = values[2];

                        Movie movie = new Movie();
                        movie.setTitle(extractTitle(titleWithYear));
                        movie.setReleaseDate(extractReleaseDate(titleWithYear));
                        // You'd need to add a genres field to the Movie entity for this
                        // movie.setGenres(genres.replace("|", ", "));
                        movieList.add(movie);
                        movieMap.put(movieId, movie);
                    } catch (NumberFormatException e) {
                        log.warn("Skipping movie due to invalid ID: {}", values[0]);
                    }
                }
            }
        }
        movieRepository.saveAll(movieList);
        return movieMap;
    }

    private Map<Long, User> loadRatingsAndCreateUsers(Map<Long, Movie> movies) throws Exception {
        Map<Long, User> userMap = new HashMap<>();
        List<Rating> ratingList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("data/ratings.csv").getInputStream()))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                long userId = Long.parseLong(values[0]);
                long movieId = Long.parseLong(values[1]);
                int score = (int) Double.parseDouble(values[2]);

                User user = userMap.computeIfAbsent(userId, id -> {
                    User newUser = new User();
                    newUser.setUsername("user" + id);
                    newUser.setEmail("user" + id + "@example.com");
                    newUser.setPassword(passwordEncoder.encode("password"));
                    return userRepository.save(newUser);
                });

                Movie movie = movies.get(movieId);
                if (movie != null) {
                    Rating rating = new Rating();
                    rating.setUser(user);
                    rating.setMovie(movie);
                    rating.setScore(score);
                    ratingList.add(rating);
                }
            }
        }
        ratingRepository.saveAll(ratingList);
        return userMap;
    }

    private String extractTitle(String titleWithYear) {
        Matcher matcher = yearPattern.matcher(titleWithYear);
        if (matcher.find()) {
            return titleWithYear.substring(0, matcher.start()).trim();
        }
        return titleWithYear;
    }

    private LocalDate extractReleaseDate(String titleWithYear) {
        Matcher matcher = yearPattern.matcher(titleWithYear);
        if (matcher.find()) {
            try {
                int year = Integer.parseInt(matcher.group(1));
                return Year.of(year).atDay(1); // Default to Jan 1st of that year
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
