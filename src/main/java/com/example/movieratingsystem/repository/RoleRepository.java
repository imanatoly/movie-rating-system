package com.example.movieratingsystem.repository;

import com.example.movieratingsystem.model.ERole;
import com.example.movieratingsystem.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}
