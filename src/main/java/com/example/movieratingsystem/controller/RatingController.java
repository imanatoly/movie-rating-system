package com.example.movieratingsystem.controller;

import com.example.movieratingsystem.dto.CreateRatingRequest;
import com.example.movieratingsystem.dto.RatingDTO;
import com.example.movieratingsystem.model.Rating;
import com.example.movieratingsystem.model.User;
import com.example.movieratingsystem.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
@AllArgsConstructor
@Tag(name = "Ratings", description = "Endpoints for rating movies")
@SecurityRequirement(name = "bearerAuth") // ✨ Bonus: Indicate endpoint needs auth
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    @Operation(summary = "Rate a movie")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RatingDTO> rateMovie(@Valid @RequestBody CreateRatingRequest request,
                                               Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        Rating rating = ratingService.rateMovie(currentUser.getUsername(), request);
        return ResponseEntity.ok(toDto(rating));
    }

    @DeleteMapping("/{ratingId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteRating(@PathVariable Long ratingId) {
        ratingService.deleteRating(ratingId);
        return ResponseEntity.noContent().build();
    }

    // Helper to convert Entity to DTO
    private RatingDTO toDto(Rating rating) {
        return new RatingDTO(rating.getId(), rating.getScore(), rating.getMovie().getId(),
                rating.getMovie().getTitle(), rating.getUser().getId());
    }
}
