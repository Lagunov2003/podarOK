package ru.uniyar.podarok.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.uniyar.podarok.dtos.AddSiteReviewDto;
import ru.uniyar.podarok.dtos.SiteReviewsDto;
import ru.uniyar.podarok.exceptions.SiteReviewAlreadyExistException;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.services.SiteReviewsService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WithMockUser
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class SiteReviewsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SiteReviewsService siteReviewsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void SiteReviewsController_GetAcceptedSiteReviews_ReturnsStatusIsOk()
            throws Exception {
        List<SiteReviewsDto> mockReviews = List.of(
                new SiteReviewsDto(1L, 1L, "User1", "Great site!", 5),
                new SiteReviewsDto(2L, 2L, "User2", "Excellent service!", 4)
        );
        when(siteReviewsService.getSiteReviews()).thenReturn(mockReviews);

        mockMvc.perform(get("/getSiteReviews"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].review").value("Great site!"))
                .andExpect(jsonPath("$[0].userName").value("User1"))
                .andExpect(jsonPath("$[1].review").value("Excellent service!"))
                .andExpect(jsonPath("$[1].userName").value("User2"));
    }

    @Test
    void SiteReviewsController_AddSiteReview_ReturnsStatusIsOk()
            throws Exception {
        AddSiteReviewDto reviewDto = new AddSiteReviewDto(5, "Great site!");
        doNothing().when(siteReviewsService).addSiteReview(any(AddSiteReviewDto.class));

        mockMvc.perform(post("/addSiteReview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Отзыв о сайте отправлен на модерацию!"));
    }

    @Test
    void SiteReviewsController_AddSiteReview_ReturnsStatusIsNotAuthorized()
            throws Exception {
        AddSiteReviewDto reviewDto = new AddSiteReviewDto(5, "Great site!");
        doThrow(new UserNotAuthorizedException("Пользователь не авторизован!"))
                .when(siteReviewsService).addSiteReview(any(AddSiteReviewDto.class));

        mockMvc.perform(post("/addSiteReview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void SiteReviewsController_AddSiteReview_ReturnsStatusIsNotFound()
            throws Exception {
        AddSiteReviewDto reviewDto = new AddSiteReviewDto(5, "Great site!");
        doThrow(new UserNotFoundException("Пользователь не найден!"))
                .when(siteReviewsService).addSiteReview(any(AddSiteReviewDto.class));

        mockMvc.perform(post("/addSiteReview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void SiteReviewsController_AddSiteReview_ReturnsStatusIsConflict()
            throws Exception {
        AddSiteReviewDto reviewDto = new AddSiteReviewDto(5, "Great site!");
        doThrow(new SiteReviewAlreadyExistException("Отзыв уже существует"))
                .when(siteReviewsService).addSiteReview(any(AddSiteReviewDto.class));

        mockMvc.perform(post("/addSiteReview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Отзыв уже существует"));
    }
}
