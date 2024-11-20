package ru.uniyar.podarok.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.uniyar.podarok.dtos.GiftFilterRequest;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.repositories.projections.GiftProjection;
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

        Page<GiftProjection> giftsPage = catalogService.getGiftsCatalog(giftFilterRequest, pageable);
        if (giftsPage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("Нет элементов на странице!");
        }
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(giftsPage));
    }

    @GetMapping("/gift/{id}")
    public ResponseEntity<?> getGiftById(@PathVariable Long id) {
        try {
            Gift gift = catalogService.getGift(id);
            return ResponseEntity.ok(gift);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Подарок с ID " + id + " не найден!");
        }
    }
}
