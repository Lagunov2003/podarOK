package ru.uniyar.podarok.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.uniyar.podarok.dtos.GiftDto;
import ru.uniyar.podarok.dtos.GiftFilterRequest;
import ru.uniyar.podarok.dtos.GiftToFavoritesDto;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.services.CatalogService;

import java.util.List;

@Controller
@AllArgsConstructor
public class CatalogController {
    private CatalogService catalogService;
    private PagedResourcesAssembler<GiftDto> pagedResourcesAssembler;

    @GetMapping("/catalog")
    public ResponseEntity<?> showCatalog(@RequestBody(required = false) GiftFilterRequest giftFilterRequest, @RequestParam(defaultValue = "1") int page) {
        if (page <= 0) {
            return ResponseEntity.badRequest().body("Номер страницы должен быть больше 0.");
        }
        if (giftFilterRequest == null) {
            giftFilterRequest = new GiftFilterRequest();
        }
        Pageable pageable = PageRequest.of(page - 1, 15);

        Page<GiftDto> giftsPage = catalogService.getGiftsCatalog(giftFilterRequest, pageable);
        if (giftsPage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("Нет элементов на странице!");
        }
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(giftsPage));
    }

    @GetMapping("/gift/{id}")
    public ResponseEntity<?> getGiftById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(catalogService.getGiftResponse(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Подарок с ID " + id + " не найден!");
        }
    }

    @GetMapping("/catalogSearch")
    public ResponseEntity<?> searchGiftsByName(@RequestParam String query, @RequestParam(defaultValue = "1") int page) {
        if (query.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Запрос не может быть пустым!");
        }
        if (page <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Номер страницы должен быть больше 0!");
        }
        Pageable pageable = PageRequest.of(page - 1, 15);

        Page<GiftDto> searchResults = catalogService.searchGiftsByName(query, pageable);
        if (searchResults.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("Нет совпадений для запроса '" + query + "'!");
        }
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(searchResults));
    }

    @PostMapping("/addToFavorites")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addGiftToFavorites(@RequestBody GiftToFavoritesDto giftToFavoritesDto) {
        try {
            catalogService.addGiftToFavorites(giftToFavoritesDto);
            return ResponseEntity.status(HttpStatus.OK).body("Подарок добавлен в избранные!");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Подарок с Id " + giftToFavoritesDto.getGiftId() + " не найден!");
        } catch(UserNotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch(UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
