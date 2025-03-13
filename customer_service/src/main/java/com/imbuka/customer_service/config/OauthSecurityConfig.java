package com.imbuka.customer_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class OauthSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET).permitAll() // Allow all GET requests
                        .requestMatchers("/api/v1/auth/**").authenticated() // Secure these endpoints
                        .requestMatchers("/api/v1/accounts/**").authenticated()
                        .requestMatchers("/v2/api-docs").authenticated()
                        .requestMatchers("/v3/api-docs").authenticated()
                        .requestMatchers("/v3/api-docs/**").authenticated()
                        .requestMatchers("/swagger-resources").authenticated()
                        .requestMatchers("/configuration/ui").authenticated()
                        .requestMatchers("/configuration/security").authenticated()
                        .requestMatchers("/swagger-ui/**").authenticated()
                        .requestMatchers("/webjars/**").authenticated()
                        .requestMatchers("/swagger-ui.html").authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults()) // Enable JWT authentication
                )
                .csrf(csrf -> csrf.disable()); // Disable CSRF protection

        return http.build();
    }
}
