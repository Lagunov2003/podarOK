package ru.uniyar.podarok.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.uniyar.podarok.dtos.JwtRequest;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private JwtRequestFilter jwtRequestFilter;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void publicEndpoints_statusIsOk_AccessWithoutAuthentication() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/registration"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void privateEndpoint_statusIsUnauthorized_AccessWithoutAuthentication() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/profile"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void privateEndpoint_statusIsOk_AccessWithAuthentication() throws Exception {
        Mockito.doNothing().when(jwtRequestFilter).doFilter(Mockito.any(), Mockito.any(), Mockito.any());

        mockMvc.perform(MockMvcRequestBuilders.get("/profile")
                        .header("Authorization", "Bearer valid_token"))
                .andExpect(status().isOk());
    }

    @Test
    void requiresAuthentication_statusIsOk_anyRequestSent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/any-other-endpoint"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void jwtRequestFilter_isApplied_requestSent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/profile"))
                .andExpect(status().isUnauthorized());

        Mockito.verify(jwtRequestFilter, Mockito.times(1)).doFilter(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void Csrf_isDisabled() throws Exception {
        JwtRequest authRequest = new JwtRequest();
        authRequest.setEmail("test_user");
        authRequest.setPassword("password123");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void FormLogin_isDisabled() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "user")
                        .param("password", "password"))
                .andExpect(status().isNotFound());
    }
}

