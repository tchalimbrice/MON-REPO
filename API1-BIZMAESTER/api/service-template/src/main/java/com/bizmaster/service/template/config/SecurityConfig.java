package com.bizmaster.service.template.config;

import com.bizmaster.service.template.config.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${service.base-path:/api/template}")
    private String serviceBasePath;

    @Value("${service.security.permit-all:true}")
    private boolean permitAll;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers(HttpMethod.GET, "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/api-docs/**").permitAll();
                auth.requestMatchers(HttpMethod.GET, "/actuator/**").permitAll();
                auth.requestMatchers(serviceBasePath + "/health").permitAll();
                auth.requestMatchers(serviceBasePath + "/invites/**").permitAll();
                auth.requestMatchers(serviceBasePath + "/sse/**").permitAll();
                // Allow invitation endpoints for public access (batch email sending, token lookup, acceptance)
                auth.requestMatchers("/api/invitations/send").permitAll();
                auth.requestMatchers("/api/invitations/token/**").permitAll();
                auth.requestMatchers("/api/invitations/accept").permitAll();
                if (permitAll) {
                    auth.requestMatchers(serviceBasePath + "/**").permitAll();
                } else {
                    auth.requestMatchers(serviceBasePath + "/**").authenticated();
                }
                auth.anyRequest().authenticated();
            })
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
