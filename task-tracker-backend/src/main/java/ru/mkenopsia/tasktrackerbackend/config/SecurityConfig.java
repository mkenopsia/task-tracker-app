package ru.mkenopsia.tasktrackerbackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.mkenopsia.tasktrackerbackend.utils.JwtTokenUtils;
import ru.mkenopsia.tasktrackerbackend.utils.TokenCookieUtils;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenUtils jwtTokenUtils, TokenCookieUtils tokenCookieUtils, AuthenticationManager authenticationManager) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(request ->
                        request.requestMatchers("/api/auth/sign-up",
                                        "/api/auth/sign-in",
                                        "/api/auth/sign-out").permitAll()
                                .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter(jwtTokenUtils, userDetailsService, authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(handling ->
                        handling.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                        .sessionAuthenticationStrategy(sessionAuthenticationStrategy(jwtTokenUtils, tokenCookieUtils))
                )
//                .csrf(csrf -> csrf.csrfTokenRepository(new CookieCsrfTokenRepository())
//                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
//                        .sessionAuthenticationStrategy(((authentication, request, response) -> {
//                        }))
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/api/auth/sign-out")
//                        .deleteCookies("JSESSIONID", "__Host-auth-token")
//                        .logoutSuccessHandler((request, response, authentication)
//                                -> response.setStatus(204)))
                .build();
    }

    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy(JwtTokenUtils jwtTokenUtils, TokenCookieUtils cookieUtils) {
        return new JwtCookieSessionAuthenticationStrategy(jwtTokenUtils, cookieUtils);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenUtils jwtTokenUtils, UserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        return new JwtAuthenticationFilter(jwtTokenUtils, userDetailsService, authenticationManager);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
