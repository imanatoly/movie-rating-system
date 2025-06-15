package com.example.movieratingsystem.dto;

import java.time.LocalDate;

public record MovieDTO(Long id, String title, String director, LocalDate releaseDate) {}
