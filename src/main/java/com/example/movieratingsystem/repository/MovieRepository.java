package com.example.movieratingsystem.repository;

import com.example.movieratingsystem.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {}
