package ru.uniyar.podarok.controllers;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.uniyar.podarok.dtos.GiftFilterRequest;
import ru.uniyar.podarok.repositories.projections.GiftProjection;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.services.CatalogService;

@Controller
@AllArgsConstructor
public class CatalogController {
    private CatalogService catalogService;
    private PagedResourcesAssembler<GiftProjection> pagedResourcesAssembler;

    @GetMapping("/catalog")
    public ResponseEntity<?> showCatalog(@RequestBody(required = false) GiftFilterRequest giftFilterRequest, @RequestParam(defaultValue = "1") int page) {
        if (page <= 0) {
            return ResponseEntity.badRequest().body("Номер страницы должен быть больше 0.");
        }
        if (giftFilterRequest == null) {
            giftFilterRequest = new GiftFilterRequest();
        }
        Pageable pageable = PageRequest.of(page - 1, 15);
        try {
            Page<GiftProjection> giftsPage = catalogService.getGiftsCatalog(giftFilterRequest, pageable);
            if (giftsPage.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body("Нет элементов на странице!");
            }
            return ResponseEntity.ok(pagedResourcesAssembler.toModel(giftsPage));
        } catch (UserNotFoundException | UserNotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Ошибка авторизации или пользователь не найден");
        } catch(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
