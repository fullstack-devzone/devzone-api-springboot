package com.sivalabs.devzone.config.security;

import static com.sivalabs.devzone.users.entities.Role.ROLE_ADMIN;
import static com.sivalabs.devzone.users.entities.Role.ROLE_MODERATOR;
import static com.sivalabs.devzone.users.entities.Role.ROLE_USER;

import com.sivalabs.devzone.users.entities.Role;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final TokenAuthenticationFilter tokenAuthFilter;

    @Bean
    RoleHierarchy roleHierarchy() {
        List<Role> hierarchy = List.of(ROLE_ADMIN, ROLE_MODERATOR, ROLE_USER);
        String hierarchyStr = hierarchy.stream().map(Role::name).collect(Collectors.joining(" > "));
        return RoleHierarchyImpl.fromHierarchy(hierarchyStr);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable);
        http.cors(CorsConfigurer::disable);

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/actuator/**", "/api/auth/**", "/error")
                .permitAll()
                .anyRequest()
                .permitAll());

        http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(tokenAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
