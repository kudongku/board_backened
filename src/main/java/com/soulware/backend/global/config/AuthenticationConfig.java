package com.soulware.backend.global.config;

import com.soulware.backend.global.filter.JwtFilter;
import com.soulware.backend.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class AuthenticationConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers(
                                        "/docs",
                                        "/v3/api-docs/**",       // OpenAPI 3 문서 관련 엔드포인트
                                        "/swagger-ui/**",        // Swagger UI 관련 엔드포인트
                                        "/swagger-ui.html",      // Swagger UI 메인 페이지
                                        "/swagger-resources/**", // Swagger 리소스
                                        "/webjars/**",           // 웹자바 리소스
                                        "/configuration/ui",     // Swagger UI 설정
                                        "/configuration/security"// Swagger 보안 설정
                                ).permitAll()
                                .requestMatchers("/health").permitAll()
                                .requestMatchers("/api/users/**").permitAll()
                                .requestMatchers("/stomp", "/stomp/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/posts").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:3001"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(
                List.of("Authorization", "content-type", "x-auth-token", "Content-Disposition")
        );
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        configuration.setExposedHeaders(List.of(
                "Access-Control-Allow-Headers", "Authorization", "x-xsrf-token",
                "Access-Control-Allow-Headers", "Origin", "Accept", "X-Requested-With", "Content-Type",
                "Access-Control-Request-Method", "Access-Control-Request-Headers", "Content-Disposition"
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return new CorsFilter(source);
    }

}
