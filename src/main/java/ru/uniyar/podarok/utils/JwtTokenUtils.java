package ru.uniyar.podarok.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Утилитный класс для генерации и обработки JWT токенов.
 */
@Component
public class JwtTokenUtils {
    /**
     * Секретный ключ для подписи JWT токенов.
     * Значение считывается из конфигурации с помощью аннотации @Value.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Время жизни JWT токенов.
     * Значение считывается из конфигурации с помощью аннотации @Value.
     */
    @Value("${jwt.lifetime}")
    private Duration jwtLifeTime;

    /**
     * Секретный ключ, использующийся для подписи токенов.
     */
    private SecretKey secretKey;

    /**
     * Инициализация секретного ключа на основе конфигурации.
     * Вызывается после инъекции зависимостей через аннотацию @PostConstruct.
     */
    @PostConstruct
    private void init() {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    /**
     * Генерация JWT токена для пользователя.
     * Этот токен включает в себя данные о ролях пользователя и срок действия токена.
     *
     * @param userDetails объект, содержащий информацию о пользователе, включая его роли.
     * @return сгенерированный JWT токен в виде строки.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        List<String> rolesList  = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", rolesList);

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifeTime.toMillis());

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Генерация JWT токена для сброса пароля пользователя.
     * Этот токен имеет ограниченный срок действия и используется для подтверждения изменения пароля.
     *
     * @param email адрес электронной почты пользователя.
     * @return сгенерированный токен для изменения пароля.
     */
    public String generatePasswordResetToken(String email) {
        final long duration = 15;

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + Duration.ofMinutes(duration).toMillis());

        return Jwts.builder()
                .subject(email)
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Извлечение всех данных (claims) из JWT токена.
     *
     * @param token JWT токен в виде строки.
     * @return объект Claims, содержащий все данные из токена.
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Извлечение email пользователя из JWT токена.
     *
     * @param token JWT токен в виде строки.
     * @return email пользователя, извлеченный из токена.
     */
    public String getUserEmail(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    /**
     * Извлечение ролей пользователя из JWT токена.
     *
     * @param token JWT токен в виде строки.
     * @return список ролей пользователя.
     */
    public List<String> getRoles(String token) {
        List<?> roles = getAllClaimsFromToken(token).get("roles", List.class);
        return roles.stream()
                .map(role -> (String) role)
                .collect(Collectors.toList());
    }
}
