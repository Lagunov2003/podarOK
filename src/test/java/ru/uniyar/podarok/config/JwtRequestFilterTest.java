package ru.uniyar.podarok.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.uniyar.podarok.utils.JwtTokenUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    private String validJwt = "valid.jwt.token";
    private String email = "test@example.com";
    @Test
    void JwtRequestFilter_DoFilterInternal_VerifiesDoFilter() throws Exception {
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
    void JwtRequestFilter_DoFilterInternal_VerifiesDoNotFilter_WithoutAuthorizationHeader() throws Exception {
        Mockito.when(request.getHeader("Authorization")).thenReturn(null);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        Mockito.verify(filterChain, Mockito.never()).doFilter(request, response);
        printWriter.flush();
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void JwtRequestFilter_DoFilterInternal_VerifiesDoNotFilter_WithExpiredToken() throws Exception {
        String expiredJwt = "expiredJwtToken";
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + expiredJwt);
        Mockito.when(jwtTokenUtils.getUserEmail(expiredJwt)).thenThrow(new ExpiredJwtException(null, null, "Токен устарел!"));
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);
        Mockito.verify(filterChain, Mockito.never()).doFilter(request, response);
        Mockito.verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        printWriter.flush();
        assertTrue(stringWriter.toString().contains("Токен устарел!"));
    }

    @Test
    void JwtRequestFilter_DoFilterInternal_VerifiesDoNotFilter_WithInvalidTokenSignature() throws Exception {
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + validJwt);
        Mockito.when(jwtTokenUtils.getUserEmail(validJwt)).thenThrow(new SignatureException("Некорректный токен!"));
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);
        Mockito.verify(filterChain, Mockito.never()).doFilter(request, response);
        Mockito.verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        printWriter.flush();
        assertTrue(stringWriter.toString().contains("Некорректный токен!"));

    }

}
