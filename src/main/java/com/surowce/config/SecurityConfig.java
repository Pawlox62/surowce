package com.surowce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        var user = User.withUsername("user")
                .password("{noop}haslo")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Wyłączamy CSRF, by nie blokowało H2-console
                .csrf(AbstractHttpConfigurer::disable)

                // Zezwalamy, żeby konsola H2 (w iframe) się wczytała
                .headers(headers -> headers.frameOptions().disable())

                // Reguły dostępu:
                .authorizeHttpRequests(auth -> auth
                        // dostęp do H2 Console bez logowania
                        .requestMatchers("/h2-console/**").permitAll()
                        // REST API wymaga roli USER
                        .requestMatchers("/api/**").hasRole("USER")
                        // wszystkie pozostałe ścieżki (np. statyczne zasoby, Thymeleaf) dostępne dla każdego
                        .anyRequest().permitAll()
                )

                // Basic Auth dla /api/**
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
