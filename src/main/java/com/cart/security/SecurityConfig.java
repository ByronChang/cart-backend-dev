package com.cart.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtAuthEntryPoint jwtAuthEntryPoint, JwtTokenProvider jwtTokenProvider) {
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource())) // Habilitar CORS
                .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF porque usas JWT
                .exceptionHandling(handling -> handling.authenticationEntryPoint(jwtAuthEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**").permitAll() // Permitir
                                                                                                // autenticación pública
                        .requestMatchers("/api/products").permitAll() // Permitir listado de productos
                        .requestMatchers("/api/products/**").authenticated() // Protección para detalles de productos
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**")
                        .permitAll() // Permitir Swagger
                        .anyRequest().authenticated() // Todos los demás endpoints requieren autenticación
                ).addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Definir los orígenes permitidos (desarrollo y producción)
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:8081"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

// package com.cart.security;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import
// org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import
// org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import
// org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @Configuration
// public class SecurityConfig {

// private final JwtAuthEntryPoint jwtAuthEntryPoint;

// public SecurityConfig(JwtAuthEntryPoint jwtAuthEntryPoint) {
// this.jwtAuthEntryPoint = jwtAuthEntryPoint;
// }

// @Bean
// public SecurityFilterChain securityFilterChain(HttpSecurity http,
// JwtTokenProvider jwtTokenProvider)
// throws Exception {
// http
// .csrf(csrf -> csrf.disable())
// .exceptionHandling(handling ->
// handling.authenticationEntryPoint(jwtAuthEntryPoint))
// .sessionManagement(session ->
// session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
// .authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**",
// "/api/products").permitAll()
// .requestMatchers("/api/products/**").authenticated()
// .requestMatchers("/swagger-ui/**", "/v3/api-docs/**",
// "/swagger-resources/**", "/webjars/**")
// .permitAll() // Permitir acceso a Swagger
// .anyRequest().authenticated() // Todos los demás endpoints requieren
// autenticación
// )
// .addFilterBefore(jwtAuthenticationFilter(jwtTokenProvider),
// UsernamePasswordAuthenticationFilter.class);

// return http.build();
// }

// @Bean
// public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider
// jwtTokenProvider) {
// return new JwtAuthenticationFilter(jwtTokenProvider);
// }

// @Bean
// public AuthenticationManager
// authenticationManager(AuthenticationConfiguration
// authenticationConfiguration)
// throws Exception {
// return authenticationConfiguration.getAuthenticationManager();
// }

// @Bean
// public PasswordEncoder passwordEncoder() {
// return new BCryptPasswordEncoder();
// }
// }