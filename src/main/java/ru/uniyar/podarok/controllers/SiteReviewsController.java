package ru.uniyar.podarok.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.uniyar.podarok.dtos.AddSiteReviewDto;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.services.SiteReviewsService;

@RestController
@AllArgsConstructor
public class SiteReviewsController {
    private SiteReviewsService siteReviewsService;

    @GetMapping("/getSiteReviews")
    public ResponseEntity<?> getAcceptedSiteReviews() {
        return ResponseEntity.ok(siteReviewsService.getSiteReviews());
    }

    @PostMapping("/addSiteReview")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addSiteReview(@RequestBody AddSiteReviewDto addSiteReviewDto) {
        try {
            siteReviewsService.addSiteReview(addSiteReviewDto);
            return ResponseEntity.ok("Отзыв о сайте отправлен на модерацию!");
        } catch(UserNotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch(UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
