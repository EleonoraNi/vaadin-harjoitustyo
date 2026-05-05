package fi.harjoitustyo.vaadin_app;

import fi.harjoitustyo.vaadin_app.entity.*;
import fi.harjoitustyo.vaadin_app.repository.RoleRepository;
import fi.harjoitustyo.vaadin_app.repository.UserRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class TestDataInitializer {

    @Bean
    CommandLineRunner initTestData(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            // Älä luo uudelleen jos dataa jo on
            if (!roleRepository.findAll().isEmpty()) {
                return;
            }

            /* =========================
               ROOLIT (3 kpl)
               ========================= */
            Role adminRole = new Role("ADMIN");
            Role superRole = new Role("SUPER");
            Role userRole = new Role("USER");

            adminRole = roleRepository.save(adminRole);
            superRole = roleRepository.save(superRole);
            userRole = roleRepository.save(userRole);

            /* =========================
               KÄYTTÄJÄT (3 kpl)
               ========================= */
            
            // Admin-käyttäjä
            User adminUser = new User(
                    "admin",
                    passwordEncoder.encode("admin123"),
                    "admin@liikuntakeskus.fi",
                    "Pekka",
                    "Hallinnoija"
            );
            adminUser.addRole(adminRole);
            userRepository.save(adminUser);
            
            //Super-käyttäjä
            User superUser = new User(
                    "super",
                    passwordEncoder.encode("super123"),
                    "super@liikuntakeskus.fi",
                    "Matti",
                    "Superkäyttäjä"
            );
            superUser.addRole(superRole);
            userRepository.save(superUser);

            // User-käyttäjä
            User normalUser = new User(
                    "user",
                    passwordEncoder.encode("user123"),
                    "user@liikuntakeskus.fi",
                    "Kari",
                    "Käyttäjä"
            );
            normalUser.addRole(userRole);
            userRepository.save(normalUser);
        };
    }
}