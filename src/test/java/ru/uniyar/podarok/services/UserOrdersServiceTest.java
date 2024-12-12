package ru.uniyar.podarok.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.uniyar.podarok.dtos.GiftDto;
import ru.uniyar.podarok.dtos.OrderDto;
import ru.uniyar.podarok.entities.*;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.utils.GiftDtoConverter;
import ru.uniyar.podarok.utils.OrderDtoConverter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserOrdersServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private GiftDtoConverter giftDtoConverter;
    @Mock
    private OrderDtoConverter orderDtoConverter;
    @InjectMocks
    private UserOrdersService userOrdersService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
    }

    @Test
    void UserOrdersService_GetUsersNotifications_ReturnsNotifications() throws UserNotFoundException, UserNotAuthorizedException {
        Notification notification1 = new Notification();
        notification1.setId(1L);
        notification1.setItemValue("Item 1");
        Notification notification2 = new Notification();
        notification2.setId(2L);
        notification2.setItemValue("Item 2");
        List<Notification> notifications = List.of(
               notification1, notification2
        );
        mockUser.setNotifications(notifications);
        when(userService.getCurrentAuthenticationUser()).thenReturn(mockUser);

        List<Notification> result = userOrdersService.getUsersNotifications();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Item 1", result.get(0).getItemValue());
        assertEquals("Item 2", result.get(1).getItemValue());
        verify(userService, Mockito.times(1)).getCurrentAuthenticationUser();
    }

    @Test
    void UserOrdersService_GetUsersFavorites_ReturnsUserFavoriteGifts() throws UserNotFoundException, UserNotAuthorizedException {
        Gift gift1 = new Gift();
        gift1.setId(1L);
        gift1.setName("Gift 1");
        gift1.setPrice(BigDecimal.valueOf(100));
        GiftDto giftDto = new GiftDto();
        giftDto.setId(1L);
        giftDto.setName("Gift 1");
        giftDto.setPrice(BigDecimal.valueOf(100));
        mockUser.setFavorites(List.of(gift1));
        Mockito.when(userService.getCurrentAuthenticationUser()).thenReturn(mockUser);
        Mockito.when(giftDtoConverter.convertToGiftDtoList(List.of(gift1))).thenReturn(List.of(giftDto));

        List<GiftDto> result = userOrdersService.getUsersFavorites();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Gift 1", result.get(0).getName());
        assertEquals(BigDecimal.valueOf(100), result.get(0).getPrice());
        Mockito.verify(userService, Mockito.times(1)).getCurrentAuthenticationUser();
    }

    @Test
    void UserOrdersService_GetUsersOrdersHistory_ReturnsCompletedOrders() throws UserNotFoundException, UserNotAuthorizedException {
        GiftPhoto giftPhoto1 = new GiftPhoto();
        giftPhoto1.setPhotoUrl("photo1.png");
        Gift gift1 = new Gift();
        gift1.setId(1L);
        gift1.setName("Gift 1");
        gift1.setPrice(BigDecimal.valueOf(100));
        gift1.setPhotos(List.of(giftPhoto1));
        Order order1 = new Order();
        order1.setId(1L);
        order1.setDeliveryDate(LocalDate.now());
        order1.setStatus("Доставлен");
        order1.setInformation("ул. Союзная, д. 144");
        GiftOrder giftOrder1 = new GiftOrder();
        giftOrder1.setGift(gift1);
        giftOrder1.setOrder(order1);
        order1.setGiftOrders(Set.of(giftOrder1));
        Order order2 = new Order();
        order2.setId(2L);
        order2.setDeliveryDate(LocalDate.now());
        order2.setStatus("Исполняется");
        order2.setInformation("ул. Союзная, д. 144");
        GiftOrder giftOrder2 = new GiftOrder();
        giftOrder2.setGift(gift1);
        giftOrder2.setOrder(order2);
        order2.setGiftOrders(Set.of(giftOrder2));
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setDeliveryDate(LocalDate.now());
        orderDto.setStatus("Доставлен");
        orderDto.setInformation("ул. Союзная, д. 144");
        mockUser.setOrders(List.of(order1, order2));
        Mockito.when(userService.getCurrentAuthenticationUser()).thenReturn(mockUser);
        Mockito.when(orderDtoConverter.convertToOrderDto(order1)).thenReturn(orderDto);

        List<OrderDto> result = userOrdersService.getUsersOrdersHistory();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Доставлен", result.get(0).getStatus());
        assertEquals("ул. Союзная, д. 144", result.get(0).getInformation());
        Mockito.verify(userService, Mockito.times(1)).getCurrentAuthenticationUser();
    }

    @Test
    void UserOrdersService_GetUsersCurrentOrders_ReturnsCurrentOrders() throws UserNotFoundException, UserNotAuthorizedException {
        GiftPhoto giftPhoto1 = new GiftPhoto();
        giftPhoto1.setPhotoUrl("photo1.png");
        Gift gift1 = new Gift();
        gift1.setId(1L);
        gift1.setName("Gift 1");
        gift1.setPrice(BigDecimal.valueOf(100));
        gift1.setPhotos(List.of(giftPhoto1));
        Order order1 = new Order();
        order1.setId(1L);
        order1.setDeliveryDate(LocalDate.now());
        order1.setStatus("Выполнен");
        order1.setInformation("ул. Союзная, д. 144");
        GiftOrder giftOrder1 = new GiftOrder();
        giftOrder1.setGift(gift1);
        giftOrder1.setOrder(order1);
        order1.setGiftOrders(Set.of(giftOrder1));
        Order order2 = new Order();
        order2.setId(2L);
        order2.setDeliveryDate(LocalDate.now());
        order2.setStatus("В пути");
        order2.setInformation("ул. Союзная, д. 144");
        GiftOrder giftOrder2 = new GiftOrder();
        giftOrder2.setGift(gift1);
        giftOrder2.setOrder(order2);
        order2.setGiftOrders(Set.of(giftOrder2));
        OrderDto orderDto = new OrderDto();
        orderDto.setId(2L);
        orderDto.setDeliveryDate(LocalDate.now());
        orderDto.setStatus("В пути");
        orderDto.setInformation("ул. Союзная, д. 144");
        mockUser.setOrders(List.of(order1, order2));
        Mockito.when(userService.getCurrentAuthenticationUser()).thenReturn(mockUser);
        Mockito.when(orderDtoConverter.convertToOrderDto(order2)).thenReturn(orderDto);

        List<OrderDto> result = userOrdersService.getUsersCurrentOrders();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("В пути", result.get(0).getStatus());
        assertEquals("ул. Союзная, д. 144", result.get(0).getInformation());
        Mockito.verify(userService, Mockito.times(1)).getCurrentAuthenticationUser();
    }
}
