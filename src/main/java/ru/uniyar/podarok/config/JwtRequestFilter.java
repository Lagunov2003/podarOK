package ru.uniyar.podarok.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.uniyar.podarok.utils.JwtTokenUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Фильтр для обработки JWT токенов в каждом HTTP запросе.
 * Фильтр проверяет наличие и корректность JWT токена в запросе,
 * валидирует его, извлекает роли пользователя и устанавливает
 * данные аутентификации в контекст безопасности.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;
    /**
     * Объект для проверки, какой путь совпадает с запросом с использованием паттернов.
     */
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * Конструктор фильтра для внедрения зависимостей.
     *
     * @param jwtTokenUtilsParam Утилита для работы с JWT токенами.
     */
    @Autowired
    public JwtRequestFilter(JwtTokenUtils jwtTokenUtilsParam) {
        this.jwtTokenUtils = jwtTokenUtilsParam;
    }

    /**
     * Список URL-адресов, которые не требуют проверки JWT токена.
     */
    private static final List<String> PERMITTED_URLS = Arrays.asList(
            "/registration", "/login", "/forgot", "/resetPassword",
            "/catalog", "/gift/**", "/getSiteReviews"
    );

    /**
     * Метод фильтрации запроса, выполняющий обработку JWT токена.
     *
     * @param request текущий HTTP запрос.
     * @param response текущий HTTP ответ.
     * @param filterChain цепочка фильтров.
     * @throws ServletException если возникает ошибка в фильтре.
     * @throws IOException если возникает ошибка ввода-вывода.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String requestURI = request.getRequestURI();

        if (isPermittedUrl(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!isTokenValid(authHeader, response)) {
            return;
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Проверяет, совпадает ли URI запроса с одним из разрешённых URL.
     *
     * @param requestURI URI запроса.
     * @return true, если URI совпадает с разрешёнными, иначе false.
     */
    private boolean isPermittedUrl(String requestURI) {
        for (String permittedUrl : PERMITTED_URLS) {
            if (antPathMatcher.match(permittedUrl, requestURI)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Проверяет валидность JWT токена.
     *
     * @param authHeader заголовок авторизации с токеном.
     * @param response HTTP ответ.
     * @return true, если токен валиден, иначе false.
     */
    private boolean isTokenValid(String authHeader, HttpServletResponse response) {
        final int beginIndex = 7;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendErrorResponse(response, "Токен не предоставлен!");
            return false;
        }

        String jwt = authHeader.substring(beginIndex);

        try {
            processToken(jwt);
            return true;
        } catch (ExpiredJwtException e) {
            sendErrorResponse(response, "Токен устарел!");
        } catch (SignatureException e) {
            sendErrorResponse(response, "Некорректный токен!");
        } catch (MalformedJwtException | IllegalArgumentException e) {
            sendErrorResponse(response, "Неправильный формат токена!");
        }

        return false;
    }

    /**
     * Обрабатывает JWT токен, извлекая email пользователя и роли.
     *
     * @param jwt токен для обработки.
     * @throws ExpiredJwtException если токен устарел.
     * @throws SignatureException если подпись токена некорректна.
     * @throws MalformedJwtException если токен имеет неправильный формат.
     */
    private void processToken(String jwt) throws ExpiredJwtException, SignatureException, MalformedJwtException {
        String email = jwtTokenUtils.getUserEmail(jwt);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            List<GrantedAuthority> authorities = jwtTokenUtils.getRoles(jwt).stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    authorities
            );

            SecurityContextHolder.getContext().setAuthentication(token);
        }
    }

    /**
     * Отправляет HTTP ответ с ошибкой и указанным сообщением.
     *
     * @param response текущий HTTP ответ.
     * @param message сообщение об ошибке.
     */
    @SneakyThrows
    private void sendErrorResponse(HttpServletResponse response, String message) {
        if (!response.isCommitted()) {
            response.reset();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(message);
            response.flushBuffer();
        }
    }
}
