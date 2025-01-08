package ru.uniyar.podarok.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.uniyar.podarok.dtos.AddSiteReviewDto;
import ru.uniyar.podarok.exceptions.SiteReviewAlreadyExistException;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.services.SiteReviewsService;

/**
 * Контроллер для управления отзывами о сайте.
 */
@RestController
@AllArgsConstructor
public class SiteReviewsController {
    private SiteReviewsService siteReviewsService;

    /**
     * Получить список опубликованных отзывов о сайте.
     *
     * @return список отзывов.
     */
    @GetMapping("/getSiteReviews")
    public ResponseEntity<?> getAcceptedSiteReviews() {
        return ResponseEntity.ok(siteReviewsService.getSiteReviews());
    }

    /**
     * Добавить новый отзыв о сайте.
     * Доступно только для авторизованных пользователей.
     *
     * @param addSiteReviewDto DTO с данными отзыва.
     * @return сообщениие об успешной отправке.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     * @throws UserNotFoundException если пользователь не найден.
     * @throws SiteReviewAlreadyExistException если отзыв о сайте уже добавлен.
     */
    @PostMapping("/addSiteReview")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addSiteReview(@RequestBody AddSiteReviewDto addSiteReviewDto)
            throws UserNotAuthorizedException, UserNotFoundException, SiteReviewAlreadyExistException {
        siteReviewsService.addSiteReview(addSiteReviewDto);
        return ResponseEntity.ok("Отзыв о сайте отправлен на модерацию!");
    }
}
