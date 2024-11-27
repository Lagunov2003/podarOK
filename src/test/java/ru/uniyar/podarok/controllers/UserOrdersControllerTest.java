package ru.uniyar.podarok.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.uniyar.podarok.dtos.GiftDto;
import ru.uniyar.podarok.dtos.OrderDto;
import ru.uniyar.podarok.entities.Notification;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.services.UserOrdersService;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@SpringBootTest
@WithMockUser
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserOrdersControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserOrdersService userOrdersService;

    @Test
    void UserOrdersController_GetNotifications_ReturnsNotifications() throws Exception {
        Notification notification1 = new Notification();
        notification1.setId(1L);
        Notification notification2 = new Notification();
        notification2.setId(2L);
        List<Notification> notifications = List.of(notification1, notification2);
        when(userOrdersService.getUsersNotifications()).thenReturn(notifications);

        mockMvc.perform(MockMvcRequestBuilders.get("/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"));
    }

    @Test
    void UserOrdersController_GetFavorites_ReturnsFavorties() throws Exception {
        GiftDto giftDto1 = new GiftDto();
        giftDto1.setId(1L);
        GiftDto giftDto2 = new GiftDto();
        giftDto2.setId(2L);
        List<GiftDto> favorites = List.of(giftDto1, giftDto2);
        when(userOrdersService.getUsersFavorites()).thenReturn(favorites);

        mockMvc.perform(MockMvcRequestBuilders.get("/favorites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"));
    }

    @Test
    void UserOrdersController_GetOrdersHistory_ReturnsOrders() throws Exception {
        OrderDto orderDto1 = new OrderDto();
        orderDto1.setId(1L);
        OrderDto orderDto2 = new OrderDto();
        orderDto2.setId(2L);

        List<OrderDto> ordersHistory = List.of(orderDto1, orderDto2);
        when(userOrdersService.getUsersOrdersHistory()).thenReturn(ordersHistory);

        mockMvc.perform(MockMvcRequestBuilders.get("/ordersHistory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"));
    }

    @Test
    void UserOrdersController_GetCurrentOrders_ReturnsOrders() throws Exception {
        OrderDto orderDto1 = new OrderDto();
        orderDto1.setId(1L);
        OrderDto orderDto2 = new OrderDto();
        orderDto2.setId(2L);
        List<OrderDto> currentOrders = List.of(orderDto1, orderDto2);
        when(userOrdersService.getUsersCurrentOrders()).thenReturn(currentOrders);

        mockMvc.perform(MockMvcRequestBuilders.get("/currentOrders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"));
    }

    @Test
    void UserOrdersController_GetNotifications_ThrowsUserNotAuthorizedException() throws Exception {
        doThrow(new UserNotAuthorizedException("Пользователь не авторизован!"))
                .when(userOrdersService).getUsersNotifications();

        mockMvc.perform(MockMvcRequestBuilders.get("/notifications"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }

    @Test
    void UserOrdersController_GetNotifications_ThrowsUserNotFoundException() throws Exception {
        doThrow(new UserNotFoundException("Пользователь не найден!"))
                .when(userOrdersService).getUsersFavorites();

        mockMvc.perform(MockMvcRequestBuilders.get("/favorites"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден!"));
    }
}
