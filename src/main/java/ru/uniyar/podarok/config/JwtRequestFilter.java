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

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    public JwtRequestFilter(JwtTokenUtils jwtTokenUtils) {
        this.jwtTokenUtils = jwtTokenUtils;
    }

    private static final List<String> PERMITTED_URLS = Arrays.asList(
            "/registration", "/login", "/forgot", "/resetPassword",
            "/catalog", "/gift/**", "/catalogSearch", "/getSiteReviews",
            "/sortCatalog"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
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

    private boolean isPermittedUrl(String requestURI) {
        for (String permittedUrl : PERMITTED_URLS) {
            if (antPathMatcher.match(permittedUrl, requestURI)) {
                return true;
            }
        }
        return false;
    }

    private boolean isTokenValid(String authHeader, HttpServletResponse response) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendErrorResponse(response, "Токен не предоставлен!");
            return false;
        }

        String jwt = authHeader.substring(7);
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