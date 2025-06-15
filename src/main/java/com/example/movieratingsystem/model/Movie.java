package com.example.movieratingsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "movies")
@Data
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String director;
    private LocalDate releaseDate;

    @OneToMany(mappedBy = "movie")
    private Set<Rating> ratings;
}
