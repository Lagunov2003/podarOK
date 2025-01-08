package ru.uniyar.podarok.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import ru.uniyar.podarok.dtos.*;
import ru.uniyar.podarok.exceptions.GiftNotFoundException;
import ru.uniyar.podarok.exceptions.OrderNotFoundException;
import ru.uniyar.podarok.exceptions.SiteReviewNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
    @Mock
    private OrderService orderService;

    @Mock
    private GiftService giftService;

    @Mock
    private GroupService groupService;

    @Mock
    private SiteReviewsService siteReviewsService;
    @InjectMocks
    @Spy
    private AdminService adminService;

    @Test
    void AdminService_ChangeOrderStatus_VerifiesStatusChanged()
            throws OrderNotFoundException {
        ChangeOrderStatusDto orderDataDto = new ChangeOrderStatusDto(1L, "Исполняется");

        adminService.changeOrderStatus(orderDataDto);

        verify(orderService, times(1)).changeOrderStatus(orderDataDto);
    }

    @Test
    void  AdminService_ChangeOrderStatus_ThrowsOrderNotFoundException()
            throws OrderNotFoundException {
        ChangeOrderStatusDto orderDataDto = new ChangeOrderStatusDto(1L, "Исполняется");
        doThrow(new OrderNotFoundException("Заказ с id " + 1L + " не найден!"))
                .when(adminService).changeOrderStatus(orderDataDto);


        OrderNotFoundException exception = assertThrows(OrderNotFoundException.class,
                () -> adminService.changeOrderStatus(orderDataDto));
        assertEquals("Заказ с id 1 не найден!", exception.getMessage());
    }

    @Test
    void AdminService_GetOrders_VerifiesOrdersGot() {
        String status = "Исполняется";
        List<OrderDto> orders = List.of(new OrderDto(
                1L,
                LocalDate.now(),
                LocalTime.now(),
                LocalTime.now(),
                "Исполняется",
                "test",
                "card",
                BigDecimal.valueOf(100),
                "user",
                "test@example.com",
                "8800",
                List.of(new GiftDto())));
        when(orderService.getOrders(status)).thenReturn(orders);

        List<OrderDto> result = adminService.getOrders(status);

        assertNotNull(result);
        assertEquals(orders, result);
        verify(orderService, times(1)).getOrders(status);
    }

    @Test
    @Deprecated
    void AdminService_DeleteGift_VerifiesGiftDeleted()
            throws GiftNotFoundException {
        Long giftId = 1L;

        adminService.deleteGift(giftId);

        verify(giftService, times(1)).deleteGift(giftId);
    }

    @Test
    @Deprecated
    void AdminService_DeleteGift_ThrowsGiftNotFoundException()
            throws GiftNotFoundException {
        Long giftId = 1L;
        doThrow(new GiftNotFoundException("Подарок с id " + giftId + " не найден!"))
                .when(adminService).deleteGift(giftId);

        GiftNotFoundException exception = assertThrows(GiftNotFoundException.class,
                () -> adminService.deleteGift(giftId));
        assertEquals("Подарок с id 1 не найден!", exception.getMessage());
    }

    @Test
    @Deprecated
    void AdminService_ChangeGift_VerifiesGiftChanged()
            throws GiftNotFoundException {
        ChangeGiftDto changeGiftDto = new ChangeGiftDto(
                1L,
                "GiftName",
                BigDecimal.valueOf(100),
                List.of("Photo1", "Photo2"),
                List.of(1L, 2L),
                3L,
                false,
                18L,
                30L,
                2L,
                "Description",
                new HashMap<>(Map.of("key", "value")));

        adminService.changeGift(changeGiftDto);

        verify(giftService, times(1)).updateGift(changeGiftDto);
    }

    @Test
    @Deprecated
    void AdminService_ChangeGift_ThrowsGiftNotFoundException()
            throws GiftNotFoundException {
        ChangeGiftDto changeGiftDto = new ChangeGiftDto(
                1L,
                "GiftName",
                BigDecimal.valueOf(100),
                List.of("Photo1", "Photo2"),
                List.of(1L, 2L),
                3L,
                false,
                18L,
                30L,
                2L,
                "Description",
                new HashMap<>(Map.of("key", "value")));
        doThrow(new GiftNotFoundException("Подарок с id " + 1L + " не найден!"))
                .when(adminService).changeGift(changeGiftDto);

        GiftNotFoundException exception = assertThrows(GiftNotFoundException.class,
                () -> adminService.changeGift(changeGiftDto));
        assertEquals("Подарок с id 1 не найден!", exception.getMessage());
    }

    @Test
    @Deprecated
    void AdminService_AddGift_VerifiesGiftAdded() {
        AddGiftDto addGiftDto = new AddGiftDto(
                "GiftName",
                BigDecimal.valueOf(100),
                List.of("Photo1", "Photo2"),
                List.of(1L, 2L),
                3L,
                false,
                18L,
                30L,
                2L,
                "Description",
                new HashMap<>(Map.of("key", "value")));

        adminService.addGift(addGiftDto);

        verify(giftService, times(1)).addGift(addGiftDto);
    }

    @Test
    @Deprecated
    void AdminService_AddGroup_VerifiesGroupAdded() {
        AddGroupDto addGroupDto = new AddGroupDto(1L);

        adminService.addGroup(addGroupDto);

        verify(groupService, times(1)).addGroup(addGroupDto);
    }

    @Test
    void  AdminService_GetSiteReviews_ShouldReturnNotAcceptedReviews() {
        List<SiteReviewsDto> siteReviewsDtos = List.of(
                new SiteReviewsDto(1L, 1L, "user", "Review 1", 5),
                new SiteReviewsDto(2L, 2L, "user", "Review 2", 4)
        );
        when(siteReviewsService.getSiteReviewsByAcceptedStatus(false)).thenReturn(siteReviewsDtos);

        List<SiteReviewsDto> result = adminService.getSiteReviews(false);

        assertEquals(2, result.size());
        assertEquals("Review 1", result.get(0).getReview());
        verify(siteReviewsService).getSiteReviewsByAcceptedStatus(false);
    }

    @Test
    void  AdminService_ChangeAcceptedStatusSiteReviews_VerifiesServiceMethodIsCalled()
            throws SiteReviewNotFoundException {
        Long reviewId = 1L;

        adminService.changeAcceptedStatusSiteReviews(reviewId);

        verify(siteReviewsService).changeAcceptedStatusSiteReviews(reviewId);
    }

    @Test
    void  AdminService_ChangeAcceptedStatusSiteReviews_ThrowsSiteReviewNotFoundException()
            throws SiteReviewNotFoundException {
        Long reviewId = 1L;
        doThrow(new SiteReviewNotFoundException("Отзыв с id " + reviewId + " не найден!"))
                .when(siteReviewsService).changeAcceptedStatusSiteReviews(reviewId);

        SiteReviewNotFoundException exception = assertThrows(SiteReviewNotFoundException.class, () ->
                adminService.changeAcceptedStatusSiteReviews(reviewId)
        );

        assertEquals("Отзыв с id 1 не найден!", exception.getMessage());
        verify(siteReviewsService).changeAcceptedStatusSiteReviews(reviewId);
    }

    @Test
    void  AdminService_DeleteNotAcceptedSiteReviews_VerifiesServiceMethodIsCalled()
            throws SiteReviewNotFoundException {
        Long reviewId = 1L;

        adminService.deleteNotAcceptedSiteReviews(reviewId);

        verify(siteReviewsService).deleteSiteReviews(reviewId);
    }

    @Test
    void  AdminService_DeleteNotAcceptedSiteReviews_ThrowsSiteReviewNotFoundException()
            throws SiteReviewNotFoundException {
        Long reviewId = 1L;
        doThrow(new SiteReviewNotFoundException("Отзыв с id " + reviewId + " не найден!"))
                .when(adminService).deleteNotAcceptedSiteReviews(reviewId);

        SiteReviewNotFoundException exception = assertThrows(SiteReviewNotFoundException.class, () ->
                adminService.deleteNotAcceptedSiteReviews(reviewId)
        );

        assertEquals("Отзыв с id 1 не найден!", exception.getMessage());
    }
}
