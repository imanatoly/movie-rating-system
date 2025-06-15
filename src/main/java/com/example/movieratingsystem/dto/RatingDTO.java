package com.example.movieratingsystem.dto;

public record RatingDTO(Long id, int score, Long movieId, String movieTitle, Long userId) {}
