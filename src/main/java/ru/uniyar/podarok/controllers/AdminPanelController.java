package ru.uniyar.podarok.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.uniyar.podarok.dtos.AddGiftDto;
import ru.uniyar.podarok.dtos.AddGroupDto;
import ru.uniyar.podarok.dtos.ChangeGiftDto;
import ru.uniyar.podarok.dtos.ChangeOrderStatusDto;
import ru.uniyar.podarok.exceptions.GiftNotFoundException;
import ru.uniyar.podarok.exceptions.OrderNotFoundException;
import ru.uniyar.podarok.exceptions.SiteReviewNotFoundException;
import ru.uniyar.podarok.services.AdminService;

/**
 * Контроллер панели администратора.
 * Все методы доступны только пользователям с ролью администратора (`ROLE_ADMIN`).
 */
@RestController
@AllArgsConstructor
public class AdminPanelController {
    private AdminService adminService;

    /**
     * Получить заказы с указанным статусом.
     *
     * @param status статус заказов.
     * @return список заказов с указанным статусом.
     */
    @GetMapping("/getOrders")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getOrders(@RequestParam String status) {
        return ResponseEntity.ok(adminService.getOrders(status));
    }

    /**
     * Изменить статус заказа.
     *
     * @param changeOrderStatusDto с новым статусом заказа.
     * @return сообщение с подтверждением изменения статуса.
     * @throws OrderNotFoundException если заказ с указанным id не найден.
     */
    @PutMapping("/changeOrderStatus")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> changeOrderStatus(@RequestBody ChangeOrderStatusDto changeOrderStatusDto)
            throws OrderNotFoundException {
        adminService.changeOrderStatus(changeOrderStatusDto);
        return ResponseEntity.ok().body("Статус заказа успешно изменён!");
    }

    /**
     * Добавить новый подарок.
     *
     * @param addGiftDto данные о новом подарке.
     * @return сообщение с подтверждением добавления подарка.
     */
    @PostMapping("/addGift")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addGift(@RequestBody AddGiftDto addGiftDto) {
        adminService.addGift(addGiftDto);
        return ResponseEntity.ok("Подарок успешно добавлен!");
    }

    /**
     * Изменить информацию о подарке.
     *
     * @param changeGiftDto данные для изменения подарка.
     * @return сообщение с подтверждением изменения.
     * @throws GiftNotFoundException если подарок с указанным id не найден.
     */
    @PutMapping("/changeGift")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> changeGift(@RequestBody ChangeGiftDto changeGiftDto) throws GiftNotFoundException {
        adminService.changeGift(changeGiftDto);
        return ResponseEntity.ok("Подарок успешно изменён!");
    }

    /**
     * Удалить подарок.
     *
     * @param id идентификатор подарка для удаления.
     * @return сообщение с подтверждением удаления подарка.
     * @throws GiftNotFoundException если подарок с указанным ID не найден.
     */
    @DeleteMapping("/deleteGift")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteGift(@RequestParam Long id) throws GiftNotFoundException {
        adminService.deleteGift(id);
        return ResponseEntity.ok("Подарок успешно удалён!");
    }

    /**
     * Добавить новую группу подарков.
     *
     * @param addGroupDto данные о новой группе подарков.
     * @return сообщение с подтверждением добавления.
     */
    @PostMapping("/addGroup")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addGroup(@RequestBody AddGroupDto addGroupDto) {
        adminService.addGroup(addGroupDto);
        return ResponseEntity.ok("Группа подарков успешно добавлена!");
    }

    /**
     * Получить список подтверждённых отзывов о сайте.
     *
     * @return список подтверждённых отзывов.
     */
    @GetMapping("/getAcceptedSiteReviews")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAcceptedSiteReviews() {
        return ResponseEntity.ok(adminService.getSiteReviews(true));
    }

    /**
     * Получить список неподтверждённых отзывов о сайте.
     *
     * @return список неподтверждённых отзывов.
     */
    @GetMapping("/getNotAcceptedSiteReviews")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getNotAcceptedSiteReviews() {
        return ResponseEntity.ok(adminService.getSiteReviews(false));
    }

    /**
     * Подтвердить отзыв о сайте.
     *
     * @param id идентификатор отзыва для подтверждения.
     * @return сообщение о принятия отзыва на сайт.
     * @throws SiteReviewNotFoundException если отзыв с указанным id не найден.
     */
    @PutMapping("/changeAcceptedStatusSiteReviews")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> changeAcceptedStatusSiteReviews(@RequestParam Long id) throws SiteReviewNotFoundException {
        adminService.changeAcceptedStatusSiteReviews(id);
        return ResponseEntity.ok("Отзыв подтверждён!");
    }

    /**
     * Удалить неподтверждённый отзыв о сайте.
     *
     * @param id идентификатор отзыва для удаления.
     * @return сообщение с подтверждением отклонения отзыва о сайте.
     * @throws SiteReviewNotFoundException если отзыв с указанным id не найден.
     */
    @DeleteMapping("/deleteNotAcceptedSiteReviews")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteNotAcceptedSiteReviews(@RequestParam Long id) throws SiteReviewNotFoundException {
        adminService.deleteNotAcceptedSiteReviews(id);
        return ResponseEntity.ok("Отзыв о сайте отклонён!");
    }
}
