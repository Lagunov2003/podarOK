package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.dtos.AddGiftDto;
import ru.uniyar.podarok.dtos.AddGroupDto;
import ru.uniyar.podarok.dtos.ChangeGiftDto;
import ru.uniyar.podarok.dtos.ChangeOrderStatusDto;
import ru.uniyar.podarok.dtos.OrderDto;
import ru.uniyar.podarok.dtos.SiteReviewsDto;
import ru.uniyar.podarok.exceptions.GiftNotFoundException;
import ru.uniyar.podarok.exceptions.OrderNotFoundException;
import ru.uniyar.podarok.exceptions.SiteReviewNotFoundException;

import java.util.List;

/**
 * Сервис для управления действиями администратора сайта.
 */
@Service
@AllArgsConstructor
public class AdminService {
    private OrderService orderService;
    private GiftService giftService;
    private GroupService groupService;
    private SiteReviewsService siteReviewsService;

    /**
     * Изменяет статус заказа.
     *
     * @param changeOrderStatusDto объект {@link ChangeOrderStatusDto}, содержащий данные о заказе и новом статусе.
     * @throws OrderNotFoundException если заказ с указанным идентификатором не найден.
     */
    public void changeOrderStatus(ChangeOrderStatusDto changeOrderStatusDto) throws OrderNotFoundException {
        orderService.changeOrderStatus(changeOrderStatusDto);
    }

    /**
     * Получает список заказов с указанным статусом.
     *
     * @param status статус заказов для фильтрации (например, "Оформлен", "Доставлен").
     * @return список {@link OrderDto}, содержащий данные о заказах.
     */
    public List<OrderDto> getOrders(String status) {
        return orderService.getOrders(status);
    }

    /**
     * Удаляет подарок по его идентификатору.
     *
     * @param id идентификатор подарка.
     * @throws GiftNotFoundException если подарок с указанным идентификатором не найден.
     */
    @Deprecated
    public void deleteGift(Long id) throws GiftNotFoundException {
        giftService.deleteGift(id);
    }

    /**
     * Обновляет данные о подарке.
     *
     * @param changeGiftDto объект {@link ChangeGiftDto}, содержащий новые данные о подарке.
     * @throws GiftNotFoundException если подарок с указанным идентификатором не найден.
     */
    @Deprecated
    public void changeGift(ChangeGiftDto changeGiftDto) throws GiftNotFoundException {
        giftService.updateGift(changeGiftDto);
    }

    /**
     * Добавляет новый подарок.
     *
     * @param addGiftDto объект {@link AddGiftDto}, содержащий данные о новом подарке.
     */
    @Deprecated
    public void addGift(AddGiftDto addGiftDto) {
        giftService.addGift(addGiftDto);
    }

    /**
     * Добавляет новую группу подарков.
     *
     * @param addGroupDto объект {@link AddGroupDto}, содержащий данные о новой группе подарков.
     */
    @Deprecated
    public void addGroup(AddGroupDto addGroupDto) {
        groupService.addGroup(addGroupDto);
    }

    /**
     * Получает список отзывов на сайт с учетом их статуса модерации.
     *
     * @param accepted `true` для получения принятых отзывов, `false` — непринятых.
     * @return список {@link SiteReviewsDto}, содержащий данные об отзывах.
     */
    public List<SiteReviewsDto> getSiteReviews(boolean accepted) {
        return siteReviewsService.getSiteReviewsByAcceptedStatus(accepted);
    }

    /**
     * Изменяет статус модерации отзыва.
     *
     * @param id идентификатор отзыва.
     * @throws SiteReviewNotFoundException если отзыв с указанным идентификатором не найден.
     */
    public void changeAcceptedStatusSiteReviews(Long id) throws SiteReviewNotFoundException {
        siteReviewsService.changeAcceptedStatusSiteReviews(id);
    }

    /**
     * Удаляет непринятый отзыв.
     *
     * @param id идентификатор отзыва.
     * @throws SiteReviewNotFoundException если отзыв с указанным идентификатором не найден.
     */
    public void deleteNotAcceptedSiteReviews(Long id) throws SiteReviewNotFoundException {
        siteReviewsService.deleteSiteReviews(id);
    }
}
