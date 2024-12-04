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
import ru.uniyar.podarok.dtos.CartDto;
import ru.uniyar.podarok.dtos.GiftDto;
import ru.uniyar.podarok.dtos.OrderItemDto;
import ru.uniyar.podarok.dtos.OrderRequestDto;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.services.CartService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WithMockUser
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Test
    public void CartController_ShowCart_ReturnsEmptyCart() throws Exception {
        when(cartService.getCart()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(content().string("Корзина пуста!"));
    }

    @Test
    public void CartController_ShowCart_ReturnsStatusIsOk() throws Exception {
        GiftDto giftDto = new GiftDto();
        giftDto.setId(1L);
        CartDto cartDto = new CartDto();
        cartDto.setItemCount(2);
        cartDto.setGift(giftDto);
        when(cartService.getCart()).thenReturn(List.of(cartDto));

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gift").value(cartDto.getGift()))
                .andExpect(jsonPath("$[0].itemCount").value(cartDto.getItemCount()));
    }
    @Test
    public void CartController_ShowCart_ReturnsStatusIsBadRequest() throws Exception {
        mockMvc.perform(get("/cart")
                .param("page", "0"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void CartController_ShowCart_ReturnsStatusIsUnauthorized() throws Exception {
        doThrow(new UserNotAuthorizedException("Пользователь не авторизован!")).when(cartService).getCart();
        mockMvc.perform(get("/cart"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }

    @Test
    public void CartController_ShowCart_ReturnsStatusIsNotFound() throws Exception {
        doThrow(new UserNotFoundException("Пользователь не найден!")).when(cartService).getCart();
        mockMvc.perform(get("/cart"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден!"));
    }

    @Test
    public void CartController_AddItem_ReturnsStatusIsOk() throws Exception {
        GiftDto giftDto = new GiftDto();
        giftDto.setId(1L);
        CartDto cartDto = new CartDto();
        cartDto.setGift(giftDto);
        cartDto.setItemCount(2);

        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1,\"itemCount\":2}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Подарок успешно добавлен в корзину!"));

        verify(cartService, times(1)).addGifts(1L, 2);
    }

    @Test
    public void CartController_AddItem_ReturnsStatusIsUnauthorized() throws Exception {
        doThrow(new UserNotAuthorizedException("Пользователь не авторизован!")).when(cartService).addGifts(anyLong(), anyInt());
        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1,\"itemCount\":2}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }

    @Test
    public void CartController_AddItem_ReturnsStatusIsNotFound() throws Exception {
        doThrow(new UserNotFoundException("Пользователь не найден!")).when(cartService).addGifts(anyLong(), anyInt());
        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1,\"itemCount\":2}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден!"));
    }

    @Test
    public void CartController_AddItem_ReturnsStatusIsBadRequest_WithNegativeItemCount() throws Exception {
        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1,\"itemCount\":-1}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Количество подарков должно быть не меньше 1!"));

        verify(cartService, never()).addGifts(anyLong(), anyInt());
    }

    @Test
    public void CartController_UpdateCartItem_ReturnsStatusIsOk_VerifiesItemIsDeleted() throws Exception {
        GiftDto giftDto = new GiftDto();
        giftDto.setId(1L);
        CartDto cartDto = new CartDto();
        cartDto.setGift(giftDto);
        cartDto.setItemCount(0);

        mockMvc.perform(put("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1,\"itemCount\":0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.giftId").doesNotExist())
                .andExpect(jsonPath("$.itemCount").doesNotExist());

        verify(cartService, times(1)).deleteGifts(1L);
    }

    @Test
    public void CartController_UpdateCartItem_ReturnsStatusIsOk_VerifiesAmountChanged() throws Exception {
        GiftDto giftDto = new GiftDto();
        giftDto.setId(1L);
        CartDto cartDto = new CartDto();
        cartDto.setGift(giftDto);
        cartDto.setItemCount(5);
        doNothing().when(cartService).changeGiftsAmount(1L, 5);
        when(cartService.getCart()).thenReturn(List.of(cartDto));

        mockMvc.perform(put("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1,\"itemCount\":5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gift").value(cartDto.getGift()))
                .andExpect(jsonPath("$[0].itemCount").value(cartDto.getItemCount()));

        verify(cartService, times(1)).changeGiftsAmount(1L, 5);
    }

    @Test
    public void CartController_UpdateCartItem_ReturnsStatusIsBadRequest() throws Exception {
        mockMvc.perform(put("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1,\"itemCount\":-1}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Количество подарков должно быть не меньше 0!"));
    }

    @Test
    public void CartController_UpdateCartItem_ReturnsStatusIsUnauthorized() throws Exception {
        doThrow(new UserNotAuthorizedException("Пользователь не авторизован!")).when(cartService).changeGiftsAmount(anyLong(), anyInt());
        mockMvc.perform(put("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1,\"itemCount\":2}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }

    @Test
    public void CartController_UpdateCartItem_ReturnsStatusIsNotFound() throws Exception {
        doThrow(new UserNotFoundException("Пользователь не найден!")).when(cartService).changeGiftsAmount(anyLong(), anyInt());
        mockMvc.perform(put("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1,\"itemCount\":2}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден!"));
    }

    @Test
    public void CartController_CleanCart_ReturnsStatusIsOk() throws Exception {
        mockMvc.perform(delete("/cart"))
                .andExpect(status().isOk())
                .andExpect(content().string("Корзина очищена!"));

        verify(cartService, times(1)).cleanCart();
    }

    @Test
    public void CartController_CleanCart_ReturnsStatusIsUnauthorized() throws Exception {
        doThrow(new UserNotAuthorizedException("Пользователь не авторизован!")).when(cartService).cleanCart();
        mockMvc.perform(delete("/cart"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }

    @Test
    public void CartController_CleanCart_ReturnsStatusIsNotFound() throws Exception {
        doThrow(new UserNotFoundException("Пользователь не найден!")).when(cartService).cleanCart();
        mockMvc.perform(delete("/cart"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден!"));
    }

    @Test
    public void CartController_AddOrder_ReturnsStatusIsOk() throws Exception {
        List<OrderItemDto> itemDtos = List.of(new OrderItemDto(1, 1L));
        OrderRequestDto orderRequestDto = new OrderRequestDto(itemDtos);

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Заказ успешно оформлен!"));

        verify(cartService, times(1)).placeOrder(orderRequestDto);
    }

    @Test
    public void CartController_AddOrder_ReturnsStatusIsBadRequest() throws Exception {
        List<OrderItemDto> itemDtos = Collections.emptyList();
        OrderRequestDto orderRequestDto = new OrderRequestDto(itemDtos);
        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(orderRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Количество подарков должно быть не меньше 1!"));
    }

    @Test
    void CartController_AddOrder_ReturnsStatusIsUnauthorized() throws Exception {
        OrderRequestDto requestDto = new OrderRequestDto(List.of(new OrderItemDto(1, 2L)));
        doThrow(new UserNotAuthorizedException("Пользователь не авторизован!"))
                .when(cartService).placeOrder(any(OrderRequestDto.class));

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));

        verify(cartService, times(1)).placeOrder(any(OrderRequestDto.class));
    }

    @Test
    void CartController_AddOrder_ReturnsStatusIsNotFound() throws Exception {
        OrderRequestDto requestDto = new OrderRequestDto(List.of(new OrderItemDto(1, 2L)));
        doThrow(new UserNotFoundException("Пользователь не найден!"))
                .when(cartService).placeOrder(any(OrderRequestDto.class));

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден!"));

        verify(cartService, times(1)).placeOrder(any(OrderRequestDto.class));
    }
}
