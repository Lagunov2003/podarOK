package ru.uniyar.podarok.config;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.uniyar.podarok.utils.JwtTokenUtils;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
class JwtRequestFilterTest {

    @MockBean
    private JwtTokenUtils jwtTokenUtils;

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void doFilterInternal_WithValidToken() throws Exception {
        String validJwt = "valid.jwt.token";
        String email = "test@example.com";
        List<String> roles = List.of("ROLE_USER");

        Mockito.when(jwtTokenUtils.getUserEmail(validJwt)).thenReturn(email);
        Mockito.when(jwtTokenUtils.getRoles(validJwt)).thenReturn(roles);

        mockMvc.perform(MockMvcRequestBuilders.get("/some-endpoint")
                        .header("Authorization", "Bearer " + validJwt))
                .andExpect(status().isOk());

        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(email, authentication.getPrincipal());
        assertTrue(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void doFilterInternal_WithoutAuthorizationHeader() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/some-endpoint"))
                .andExpect(status().isOk());

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_WithExpiredToken() throws Exception {
        String expiredJwt = "expired.jwt.token";

        Mockito.when(jwtTokenUtils.getUserEmail(expiredJwt)).thenThrow(new ExpiredJwtException(null, null, "Токе устарел"));

        mockMvc.perform(MockMvcRequestBuilders.get("/some-endpoint")
                        .header("Authorization", "Bearer " + expiredJwt))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Токен устарел!"));

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_WithInvalidTokenSignature() throws Exception {
        String invalidJwt = "invalid.jwt.token";

        Mockito.when(jwtTokenUtils.getUserEmail(invalidJwt)).thenThrow(new SignatureException("Недопустимая подпись токена"));

        mockMvc.perform(MockMvcRequestBuilders.get("/some-endpoint")
                        .header("Authorization", "Bearer " + invalidJwt))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Некорректный токен!"));

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_WithNullToken() throws Exception {
        String invalidJwt = "Bearer ";  // Некорректный заголовок

        mockMvc.perform(MockMvcRequestBuilders.get("/some-endpoint")
                        .header("Authorization", invalidJwt))
                .andExpect(status().isOk());

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
