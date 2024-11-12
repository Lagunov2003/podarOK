package ru.uniyar.podarok.controllers;

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
import ru.uniyar.podarok.entities.Cart;
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
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItemCount(2);
        when(cartService.getCart()).thenReturn(List.of(cart));

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(cart.getId()))
                .andExpect(jsonPath("$[0].itemCount").value(cart.getItemCount()));
    }

    @Test
    public void CartController_AddItem_ReturnsStatusIsOk() throws Exception {
        CartDto cartDto = new CartDto();
        cartDto.setGiftId(1L);
        cartDto.setItemCount(2);

        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1,\"itemCount\":2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.giftId").value(cartDto.getGiftId()))
                .andExpect(jsonPath("$.itemCount").value(cartDto.getItemCount()));

        verify(cartService, times(1)).addGifts(1L, 2);
    }

    @Test
    public void CartController_AddItem_ReturnsStatusIsBadRequest_WithNegativeItemCount() throws Exception {
        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1,\"itemCount\":-1}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Количество подарков должно быть не меньше 0!"));

        verify(cartService, never()).addGifts(anyLong(), anyInt());
    }

    @Test
    public void CartController_UpdateCartItem_VerifiesItemIsDeleted() throws Exception {
        CartDto cartDto = new CartDto();
        cartDto.setGiftId(1L);
        cartDto.setItemCount(0);

        mockMvc.perform(put("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1,\"itemCount\":0}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Подарок успешно удалён!"));

        verify(cartService, times(1)).deleteGifts(1L);
    }

    @Test
    public void CartController_UpdateCartItem_ReturnsStatusIsOk() throws Exception {
        CartDto cartDto = new CartDto();
        cartDto.setGiftId(1L);
        cartDto.setItemCount(5);

        mockMvc.perform(put("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1,\"itemCount\":5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.giftId").value(cartDto.getGiftId()))
                .andExpect(jsonPath("$.itemCount").value(cartDto.getItemCount()));

        verify(cartService, times(1)).changeGiftsAmount(1L, 5);
    }

    @Test
    public void CartController_CleanCart_ReturnsStatusIsOk() throws Exception {
        mockMvc.perform(delete("/cart"))
                .andExpect(status().isOk())
                .andExpect(content().string("Корзина очищена!"));

        verify(cartService, times(1)).cleanCart();
    }
}
