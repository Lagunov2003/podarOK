package ru.uniyar.podarok.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
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
import ru.uniyar.podarok.dtos.*;
import ru.uniyar.podarok.exceptions.OrderNotFoundException;
import ru.uniyar.podarok.services.AdminService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@WithMockUser(roles = {"ADMIN"})
public class AdminPanelControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AdminService adminService;

    @Test
    void AdminController_GetOrders_ReturnsOrders() throws Exception {
        String status = "Исполняется";
        List<OrderDto> orders = List.of(new OrderDto(1L, LocalDate.now(), LocalTime.now(), LocalTime.now(), "Исполняется", "test", "card",
                BigDecimal.valueOf(100), "user", "test@example.com", "8800", List.of(new GiftDto())));
        when(adminService.getOrders(status)).thenReturn(orders);

        mockMvc.perform(get("/getOrders")
                        .param("status", status))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].status").value("Исполняется"));

        verify(adminService, times(1)).getOrders(status);
    }

    @Test
    void AdminController_ChangeOrderStatus_ReturnsStatusIsOk() throws Exception {
        OrderDataDto orderDataDto = new OrderDataDto(1L, "Completed");
        doNothing().when(adminService).changeOrderStatus(orderDataDto);

        mockMvc.perform(put("/changeOrderStatus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderDataDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Статус заказа успешно изменён!"));

        verify(adminService, times(1)).changeOrderStatus(orderDataDto);
    }

    @Test
    void AdminController_ChangeOrderStatus_ReturnsStatusIsNotFound() throws Exception {
        OrderDataDto orderDataDto = new OrderDataDto(1L, "Completed");
        doThrow(new OrderNotFoundException("Заказ не найден!")).when(adminService).changeOrderStatus(orderDataDto);

        mockMvc.perform(put("/changeOrderStatus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderDataDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Заказ не найден!"));

        verify(adminService, times(1)).changeOrderStatus(orderDataDto);
    }

    @Test
    void AdminController_DeleteGift_ReturnsStatusIsOk() throws Exception {
        Long giftId = 1L;
        doNothing().when(adminService).deleteGift(giftId);

        mockMvc.perform(delete("/deleteGift")
                        .param("id", giftId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Подарок успешно удалён!"));

        verify(adminService, times(1)).deleteGift(giftId);
    }

    @Test
    void AdminController_DeleteGift_ReturnsStatusIsNotFound() throws Exception {
        Long giftId = 1L;
        doThrow(new EntityNotFoundException("Подарок не найден!")).when(adminService).deleteGift(giftId);

        mockMvc.perform(delete("/deleteGift")
                        .param("id", giftId.toString()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Подарок не найден!"));
        verify(adminService, times(1)).deleteGift(giftId);
    }

    @Test
    void AdminController_ChangeGift_ReturnsStatusIsOk() throws Exception {
        ChangeGiftDto changeGiftDto = new ChangeGiftDto(1L, "GiftName", BigDecimal.valueOf(100),
                List.of("Photo1", "Photo2"), List.of(1L, 2L), 3L, false, 18L, 30L, 2L,  "Description", new HashMap<>(Map.of("key", "value")));
        doNothing().when(adminService).changeGift(changeGiftDto);

        mockMvc.perform(put("/changeGift")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(changeGiftDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Подарок успешно изменён!"));
        verify(adminService, times(1)).changeGift(changeGiftDto);
    }

    @Test
    void AdminController_ChangeGift_ReturnsStatusIsNotFound() throws Exception {
        ChangeGiftDto changeGiftDto = new ChangeGiftDto(1L, "GiftName", BigDecimal.valueOf(100),
                List.of("Photo1", "Photo2"), List.of(1L, 2L), 3L, false, 18L, 30L, 2L,  "Description", new HashMap<>(Map.of("key", "value")));
        doThrow(new EntityNotFoundException("Подарок не найден!")).when(adminService).changeGift(changeGiftDto);

        mockMvc.perform(put("/changeGift")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(changeGiftDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Подарок не найден!"));
        verify(adminService, times(1)).changeGift(changeGiftDto);
    }

    @Test
    void AdminController_AddGift_ReturnsStatusIsOk() throws Exception {
        AddGiftDto addGiftDto = new AddGiftDto("GiftName", BigDecimal.valueOf(100),
                List.of("Photo1", "Photo2"), List.of(1L, 2L), 3L, false, 18L, 30L, 2L,  "Description", new HashMap<>(Map.of("key", "value")));
        doNothing().when(adminService).addGift(addGiftDto);

        mockMvc.perform(post("/addGift")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addGiftDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Подарок успешно добавлен!"));
        verify(adminService, times(1)).addGift(addGiftDto);
    }

    @Test
    void AdminController_AddGroup_ReturnsStatusIsOk() throws Exception {
        AddGroupDto addGroupDto = new AddGroupDto(1L);
        doNothing().when(adminService).addGroup(addGroupDto);

        mockMvc.perform(post("/addGroup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addGroupDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Группа подарков успешно добавлена!"));
        verify(adminService, times(1)).addGroup(addGroupDto);
    }

    @Test
    void AdminController_GetAcceptedSiteReviews_ReturnStatusIsOk() throws Exception {
        List<SiteReviewsDto> siteReviews = List.of(
                new SiteReviewsDto(1L, "user", "Review 1", 5),
                new SiteReviewsDto(2L, "user", "Review 2", 4)
        );

        when(adminService.getSiteReviews(true)).thenReturn(siteReviews);

        mockMvc.perform(get("/getAcceptedSiteReviews")
                        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].review").value("Review 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].review").value("Review 2"));

        verify(adminService).getSiteReviews(true);
    }

    @Test
    void AdminController_GetNotAcceptedSiteReviews_ReturnsStatusIsOk() throws Exception {
        List<SiteReviewsDto> siteReviews = List.of(
                new SiteReviewsDto(3L, "user", "Not Accepted Review", 3)
        );

        when(adminService.getSiteReviews(false)).thenReturn(siteReviews);

        mockMvc.perform(get("/getNotAcceptedSiteReviews")
                        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].review").value("Not Accepted Review"));

        verify(adminService).getSiteReviews(false);
    }

    @Test
    void AdminController_ChangeAcceptedStatusSiteReviews_ReturnsStatusIsOk() throws Exception {
        Long reviewId = 1L;

        mockMvc.perform(put("/changeAcceptedStatusSiteReviews")
                        .param("id", String.valueOf(reviewId))
                        )
                .andExpect(status().isOk())
                .andExpect(content().string("Отзыв подтверждён!"));

        verify(adminService).changeAcceptedStatusSiteReviews(reviewId);
    }

    @Test
    void AdminController_ChangeAcceptedStatusSiteReviews_ReturnsStatusIsNotFound() throws Exception {
        Long reviewId = 1L;

        doThrow(new EntityNotFoundException("Отзыв с id " + reviewId + " не найден!"))
                .when(adminService).changeAcceptedStatusSiteReviews(reviewId);

        mockMvc.perform(put("/changeAcceptedStatusSiteReviews")
                        .param("id", String.valueOf(reviewId))
                        )
                .andExpect(status().isNotFound())
                .andExpect(content().string("Отзыв с id 1 не найден!"));

        verify(adminService).changeAcceptedStatusSiteReviews(reviewId);
    }

    @Test
    void AdminController_DeleteNotAcceptedSiteReviews_ReturnsStatusIsOk() throws Exception {
        Long reviewId = 1L;

        mockMvc.perform(delete("/deleteNotAcceptedSiteReviews")
                        .param("id", String.valueOf(reviewId))
                        )
                .andExpect(status().isOk())
                .andExpect(content().string("Отзыв о сайте отклонён!"));

        verify(adminService).deleteNotAcceptedSiteReviews(reviewId);
    }
}
