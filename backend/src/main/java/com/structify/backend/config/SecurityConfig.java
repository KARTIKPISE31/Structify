package com.structify.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.structify.backend.entity.User;
import com.structify.backend.repository.UserRepository;

@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserDetailsService userDetailsService,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // AUTH PROVIDER
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    // LOGIN SUCCESS HANDLER
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {

            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElseThrow();

            switch (user.getRole()) {
                case "ROLE_ADMIN" -> response.sendRedirect("/admin/dashboard");
                case "ROLE_PENDING_BUILDER" -> response.sendRedirect("/builder/pending");
                case "ROLE_BUILDER" -> response.sendRedirect("/builder/dashboard");
                default -> response.sendRedirect("/user/dashboard");
            }
        };
    }

    // SECURITY FILTER CHAIN (FINAL & STABLE)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // ✅ REQUIRED for fetch POST (AI)
            .csrf(csrf -> csrf.disable())

            .authenticationProvider(authenticationProvider())

            .authorizeHttpRequests(auth -> auth
                // PUBLIC
                .requestMatchers("/login", "/signup", "/css/**", "/js/**").permitAll()

                // ✅ AI endpoints (HTML + POST)
                .requestMatchers("/ai/**").permitAll()

                // ROLE BASED
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/builder/**").hasAnyAuthority("ROLE_BUILDER", "ROLE_PENDING_BUILDER")
                .requestMatchers("/user/**").hasAuthority("ROLE_USER")

                // EVERYTHING ELSE
                .anyRequest().authenticated()
            )

            // ✅ Prevent AI calls from redirecting to /login
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    if (request.getRequestURI().startsWith("/ai/")) {
                        response.setStatus(401);
                        response.setContentType("application/json");
                        response.getWriter().write("{\"error\":\"Unauthorized API access\"}");
                    } else {
                        response.sendRedirect("/login");
                    }
                })
            )

            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(authenticationSuccessHandler())
                .failureUrl("/login?error")
                .permitAll()
            )

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }

    // AUTH MANAGER
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
