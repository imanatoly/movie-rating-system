package com.example.movieratingsystem.util;

import com.example.movieratingsystem.model.ERole;
import com.example.movieratingsystem.model.Role;
import com.example.movieratingsystem.model.User;
import com.example.movieratingsystem.repository.RoleRepository;
import com.example.movieratingsystem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * This component runs on startup and creates an initial admin user if it doesn't exist.
 * It is only enabled if the property 'app.security.initialize-admin' is set to 'true'.
 * This is useful for initial setup but should be disabled in production for security.
 */
@Component
@ConditionalOnProperty(name = "app.security.initialize-admin", havingValue = "true")
public class AdminInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AdminInitializer.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Read initial admin credentials from application.properties
    @Value("${app.security.admin.username}")
    private String adminUsername;

    @Value("${app.security.admin.password}")
    private String adminPassword;

    @Value("${app.security.admin.email}")
    private String adminEmail;

    public AdminInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Checking for initial admin user...");

        // Check if the ADMIN role exists, if not, something is very wrong with the DataSeeder
        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("CRITICAL: ROLE_ADMIN not found. Please run the DataSeeder."));

        // Check if a user with the admin username already exists
        if (userRepository.existsByUsername(adminUsername)) {
            logger.warn("Admin user '{}' already exists. Skipping creation.", adminUsername);
            return;
        }

        // Create the new admin user
        User adminUser = new User();
        adminUser.setUsername(adminUsername);
        adminUser.setPassword(passwordEncoder.encode(adminPassword)); // Hash the password!
        adminUser.setEmail(adminEmail);
        adminUser.setRoles(Set.of(adminRole));

        userRepository.save(adminUser);
        logger.info("Successfully created initial admin user '{}'.", adminUsername);
        logger.warn("IMPORTANT: Please disable 'app.security.initialize-admin' in your production environment properties for security reasons.");
    }
}
