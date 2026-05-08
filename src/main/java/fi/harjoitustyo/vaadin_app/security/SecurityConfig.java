package fi.harjoitustyo.vaadin_app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/login",
                                                                "/logout",
                                                                "/access-denied",
                                                                "/register",
                                                                "/register/**",
                                                                "/",
                                                                "/VAADIN/**")
                                                .permitAll()
                                                .requestMatchers("/ilmoittautuminen",
                                                                "/ilmoittautuminen/**")
                                                .hasAnyRole("USER")
                                                .requestMatchers(

                                                                "/tuntihaku",
                                                                "/tuntihaku/**",
                                                                "/liikkujat",
                                                                "/liikkujat/**",
                                                                "/liikkujaForm",
                                                                "/liikkujaForm/**",
                                                                "/jasenyydet",
                                                                "/jasenyydet/**")
                                                .hasAnyRole("USER", "SUPER", "ADMIN")
                                                .requestMatchers(
                                                                "/liikuntatunnit",
                                                                "/liikuntatunnit/**",
                                                                "/liikuntatuntiForm",
                                                                "/liikuntatuntiForm/**")
                                                .hasAnyRole("SUPER")
                                                .requestMatchers(
                                                                "/ohjaajat",
                                                                "/ohjaajat/**",
                                                                "/ohjaajaForm",
                                                                "/ohjaajaForm/**",
                                                                "/quill",
                                                                "/quill/**")
                                                .hasRole("ADMIN")
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
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }
}