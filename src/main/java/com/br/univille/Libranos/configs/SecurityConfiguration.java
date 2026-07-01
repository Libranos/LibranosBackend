package com.br.univille.Libranos.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                        // Preflight CORS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Rotas públicas de autenticação
                        .requestMatchers("/auth/**").permitAll()

                        // Escrita em módulos: somente TEACHER
                        .requestMatchers(HttpMethod.POST,   "/api/modulos").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.PUT,    "/api/modulos/**").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.DELETE, "/api/modulos/**").hasRole("TEACHER")

                        // Escrita em aulas: somente TEACHER
                        .requestMatchers(HttpMethod.POST,   "/api/aulas").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.PUT,    "/api/aulas/**").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.DELETE, "/api/aulas/**").hasRole("TEACHER")

                        // Escrita em atividades: somente TEACHER
                        .requestMatchers(HttpMethod.POST,   "/api/atividades").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.PUT,    "/api/atividades/**").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.DELETE, "/api/atividades/**").hasRole("TEACHER")

                        // Demais rotas da API: qualquer usuário autenticado
                        .requestMatchers("/api/**").authenticated()

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
