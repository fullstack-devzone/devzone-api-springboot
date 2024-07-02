package com.sivalabs.devzone.config;

import static com.sivalabs.devzone.users.domain.Role.ROLE_ADMIN;
import static com.sivalabs.devzone.users.domain.Role.ROLE_MODERATOR;
import static com.sivalabs.devzone.users.domain.Role.ROLE_USER;

import com.sivalabs.devzone.auth.TokenAuthenticationFilter;
import com.sivalabs.devzone.users.domain.Role;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class WebSecurityConfig {
    private final TokenAuthenticationFilter tokenAuthFilter;

    @Bean
    RoleHierarchy roleHierarchy() {
        List<Role> hierarchy = List.of(ROLE_ADMIN, ROLE_MODERATOR, ROLE_USER);
        String hierarchyStr = hierarchy.stream().map(Role::name).collect(Collectors.joining(" > "));
        return RoleHierarchyImpl.fromHierarchy(hierarchyStr);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable);

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/actuator/**", "/error")
                .permitAll()
                .requestMatchers("/api/login", "/api/users/**")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/api/posts/**")
                .permitAll()
                .requestMatchers("/api/**")
                .authenticated()
                .anyRequest()
                .authenticated());

        http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(tokenAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
