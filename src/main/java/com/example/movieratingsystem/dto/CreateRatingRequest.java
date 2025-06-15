package com.example.movieratingsystem.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateRatingRequest(
        @NotNull Long movieId,
        @Min(1) @Max(5) int score
) {}
