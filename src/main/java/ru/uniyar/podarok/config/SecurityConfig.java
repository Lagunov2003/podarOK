package ru.uniyar.podarok.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Конфигурация безопасности для Spring приложения с использованием JWT и настройки прав доступа для различных запросов.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
@EnableSpringDataWebSupport
public class SecurityConfig {
    private JwtRequestFilter jwtRequestFilter;
    /**
     * Кодировщик паролей, используемый для хэширования паролей пользователей перед их сохранением в базе данных.
     * Этот компонент используется для обеспечения безопасного хранения паролей.
     */
    private PasswordEncoder passwordEncoder;

    /**
     * Конфигурирует менеджер аутентификации.
     *
     * @param authenticationConfiguration объект AuthenticationConfiguration для получения AuthenticationManager.
     * @return экземпляр AuthenticationManager.
     * @throws Exception если произошла ошибка при создании менеджера аутентификации.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Конфигурирует цепочку фильтров безопасности для обработки запросов.
     *
     * @param http объект HttpSecurity для настройки фильтров безопасности.
     * @return настроенная цепочка фильтров безопасности.
     * @throws Exception если произошла ошибка при настройке безопасности.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/registration", "/login", "/forgot", "/resetPassword",
                                "/catalog", "/gift/**", "/getSiteReviews"
                        ).permitAll()
                        .requestMatchers(
                                "/profile", "/cart", "/notifications", "/favorites", "/ordersHistory",
                                "/currentOrders", "/order", "/addToFavorites", "/addSiteReview", "/sentMessages",
                                "/messages", "/newMessages", "/send", "/addGiftReview", "/deleteFromFavorites"
                        ).authenticated()
                        .requestMatchers(
                                "/changeOrderStatus", "/getOrders", "/deleteGift", "/changeGift", "/addGift",
                                "/addGroup", "/getAcceptedSiteReviews", "/getNotAcceptedSiteReviews",
                                "/changeAcceptedStatusSiteReviews", "/deleteNotAcceptedSiteReviews"
                        ).hasRole("ADMIN")
                        .anyRequest().permitAll())
                .formLogin(AbstractAuthenticationFilterConfigurer::disable)
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Конфигурирует источник CORS.
     *
     * @return объект CorsConfigurationSource для настройки CORS.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        corsConfiguration.setExposedHeaders(List.of("Authorization"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
