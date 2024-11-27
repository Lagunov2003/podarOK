package ru.uniyar.podarok.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.uniyar.podarok.dtos.CartDto;
import ru.uniyar.podarok.dtos.GiftDto;
import ru.uniyar.podarok.dtos.OrderItemDto;
import ru.uniyar.podarok.dtos.OrderRequestDto;
import ru.uniyar.podarok.entities.Cart;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.entities.Order;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.repositories.CartRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserService userService;

    @Mock
    private GiftService giftService;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private CartService cartService;

    private User user;
    private Gift gift;
    private Cart cart;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("user");

        gift = new Gift();
        gift.setId(1L);
        gift.setName("gift");

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setGift(gift);
        cart.setItemCount(2);
    }

    @Test
    public void CartService_AddGifts_VerifiesGiftIsSaved() throws UserNotFoundException, UserNotAuthorizedException {
        when(giftService.getGiftById(gift.getId())).thenReturn(gift);
        when(userService.getCurrentAuthenticationUser()).thenReturn(user);

        cartService.addGifts(gift.getId(), 3);

        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    public void CartService_DeleteGifts_VerifiesGiftIdDeleted() {
        when(cartRepository.findItemByGiftId(gift.getId())).thenReturn(Optional.of(cart));

        cartService.deleteGifts(gift.getId());

        verify(cartRepository, times(1)).delete(cart);
    }

    @Test
    public void CartService_ChangeGiftsAmount_ReturnsGiftItemAmount() {
        when(cartRepository.findItemByGiftId(gift.getId())).thenReturn(Optional.of(cart));

        cartService.changeGiftsAmount(gift.getId(), 5);

        verify(cartRepository, times(1)).save(cart);
        assertEquals(5, cart.getItemCount());
    }

    @Test
    public void CartService_CleanCart_ReturnsIsRepositoryEmpty() {
        cartService.cleanCart();

        verify(cartRepository, times(1)).deleteAll();
        assertTrue(cartRepository.findAll().isEmpty());
    }

    @Test
    public void CartService_GetCart_ReturnsCart() throws UserNotFoundException, UserNotAuthorizedException {
        GiftDto giftDto = new GiftDto();
        giftDto.setId(1L);
        giftDto.setName("gift");
        CartDto cartDto = new CartDto();
        cartDto.setItemCount(2);
        cartDto.setGift(giftDto);
        when(cartRepository.findAll()).thenReturn(List.of(cart));

        List<CartDto> cartList = cartService.getCart();

        assertEquals(1, cartList.size());
        assertEquals(cartDto, cartList.get(0));
    }

    @Test
    void CartService_PlaceOrder_VerifiesOrderIsPlaced() throws Exception {
        OrderRequestDto orderRequestDto = new OrderRequestDto(List.of(
                new OrderItemDto(1, 1L)
        ));
        Order order = new Order();
        order.setUser(user);
        order.setGift(gift);
        when(userService.getCurrentAuthenticationUser()).thenReturn(user);
        when(giftService.getGiftById(1L)).thenReturn(gift);
        when(cartRepository.findItemByGiftIdAndUserId(1L, 1L)).thenReturn(Optional.of(cart));

        cartService.placeOrder(orderRequestDto);

        verify(orderService).placeNewOrder(any(Order.class));
        verify(cartRepository).delete(cart);
    }

    @Test
    void CartService_PlaceOrder_ThrowsUserNotFoundException() throws Exception {
        OrderRequestDto orderRequestDto = new OrderRequestDto(List.of(
                new OrderItemDto(1, 1L)
        ));
        when(userService.getCurrentAuthenticationUser()).thenThrow(new UserNotFoundException("Пользователь не найден!"));

        assertThrows(UserNotFoundException.class, () -> cartService.placeOrder(orderRequestDto));
    }

    @Test
    void CartService_PlaceOrder_ThrowsEntityNotFoundException() throws Exception {
        OrderRequestDto orderRequestDto = new OrderRequestDto(List.of(
                new OrderItemDto(1, 1L)
        ));
        when(userService.getCurrentAuthenticationUser()).thenReturn(user);
        when(giftService.getGiftById(1L)).thenThrow(new EntityNotFoundException("Подарок не найден!"));

        assertThrows(EntityNotFoundException.class, () -> cartService.placeOrder(orderRequestDto));
    }
}
