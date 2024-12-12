package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.dtos.*;
import ru.uniyar.podarok.dtos.AddGiftDto;
import ru.uniyar.podarok.dtos.ChangeGiftDto;
import ru.uniyar.podarok.dtos.OrderDataDto;
import ru.uniyar.podarok.dtos.OrderDto;
import ru.uniyar.podarok.exceptions.GiftNotFoundException;
import ru.uniyar.podarok.exceptions.OrderNotFoundException;
import ru.uniyar.podarok.exceptions.SiteReviewNotFoundException;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {
    private OrderService orderService;
    private GiftService giftService;
    private GroupService groupService;
    private SiteReviewsService siteReviewsService;

    public void changeOrderStatus(OrderDataDto orderDataDto) throws OrderNotFoundException {
        orderService.changeOrderStatus(orderDataDto);
    }

    public List<OrderDto> getOrders(String status) {
        return orderService.getOrders(status);
    }

    public void deleteGift(Long id) throws GiftNotFoundException {
        giftService.deleteGift(id);
    }

    public void changeGift(ChangeGiftDto changeGiftDto) throws GiftNotFoundException {
        giftService.updateGift(changeGiftDto);
    }

    public void addGift(AddGiftDto addGiftDto) {
        giftService.addGift(addGiftDto);
    }

    public void addGroup(AddGroupDto addGroupDto) {
        groupService.addGroup(addGroupDto);
    }

    public List<SiteReviewsDto> getSiteReviews(boolean accepted) {
        return siteReviewsService.getSiteReviewsByAcceptedStatus(accepted);
    }

    public void changeAcceptedStatusSiteReviews(Long id) throws SiteReviewNotFoundException {
        siteReviewsService.changeAcceptedStatusSiteReviews(id);
    }

    public void deleteNotAcceptedSiteReviews(Long id) throws SiteReviewNotFoundException {
        siteReviewsService.deleteSiteReviews(id);
    }
}
