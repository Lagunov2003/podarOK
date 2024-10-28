package ru.uniyar.podarok.config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.uniyar.podarok.utils.JwtTokenUtils;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JwtRequestFilterTest {
    @Mock
    private JwtTokenUtils jwtTokenUtils;
    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp(){
        SecurityContextHolder.clearContext();
    }

    private String validJwt = "valid.jwt.token";
    private String email = "test@example.com";
    @Test
    void doFilterInternal_WithValidToken() throws Exception {
        List<String> roles = List.of("ROLE_USER");


        Mockito.when(jwtTokenUtils.getUserEmail(validJwt)).thenReturn(email);
        Mockito.when(jwtTokenUtils.getRoles(validJwt)).thenReturn(roles);
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + validJwt);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(email, authentication.getPrincipal());
        assertTrue(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));

        Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithoutAuthorizationHeader() throws Exception {
        Mockito.when(request.getHeader("Authorization")).thenReturn(null);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        Mockito.verify(filterChain).doFilter(request, response);
        printWriter.flush();
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_WithExpiredToken() throws Exception {
        String expiredJwt = "expiredJwtToken";

        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + expiredJwt);
        Mockito.when(jwtTokenUtils.getUserEmail(expiredJwt)).thenThrow(new ExpiredJwtException(null, null, "Токен устарел!"));
        //Mockito.doThrow(new ExpiredJwtException(null, null, "Токен устарел!")).when(jwtTokenUtils).getUserEmail(expiredJwt);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        Mockito.verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        printWriter.flush();
        assertTrue(stringWriter.toString().contains("Токен устарел!"));
    }

    @Test
    void doFilterInternal_WithInvalidTokenSignature() throws Exception {
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + validJwt);
        Mockito.when(jwtTokenUtils.getUserEmail(validJwt)).thenThrow(new SignatureException("Некорректный токен!"));
        //Mockito.doThrow(new SignatureException("Invalid token signature")).when(jwtTokenUtils).getUserEmail(validJwt);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        Mockito.verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        printWriter.flush();
        assertTrue(stringWriter.toString().contains("Некорректный токен!"));

    }

    @Test
    void doFilterInternal_WithNullToken() throws Exception {
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + validJwt);
        Mockito.when(jwtTokenUtils.getUserEmail(validJwt)).thenReturn(null);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        Mockito.verify(filterChain).doFilter(request, response);
    }
}
