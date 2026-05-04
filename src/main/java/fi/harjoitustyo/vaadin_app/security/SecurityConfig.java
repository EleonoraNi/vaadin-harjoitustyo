package fi.harjoitustyo.vaadin_app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/VAADIN/**",
                                "/frontend/**",
                                "/images/**",
                                "/icons/**",
                                "/manifest.webmanifest",
                                "/sw.js",
                                "/offline.html")
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .defaultSuccessUrl("/", true)
                        .permitAll())
                .logout(logout -> logout.permitAll())
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied"));

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin")
                        .password(passwordEncoder().encode("admin123"))
                        .roles("ADMIN")
                        .build(),
                User.withUsername("user")
                        .password(passwordEncoder().encode("user123"))
                        .roles("USER")
                        .build());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}