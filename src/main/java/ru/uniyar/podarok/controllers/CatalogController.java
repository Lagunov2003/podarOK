package ru.uniyar.podarok.controllers;

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
import ru.uniyar.podarok.exceptions.GiftNotFoundException;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.services.CatalogService;

/**
 * Контроллер для управления каталогом подарков, включая фильтрацию, поиск, сортировку и добавление подарков в избранное.
 */
@Controller
@AllArgsConstructor
public class CatalogController {
    private CatalogService catalogService;
    private PagedResourcesAssembler<GiftDto> pagedResourcesAssembler;

    /**
     * Показать каталог подарков с фильтрацией (по фильтрам или по опросу).
     *
     * @param giftFilterRequest объект с параметрами фильтрации.
     * @param page номер страницы (по умолчанию 1).
     * @return отфильтрованный список подарков.
     */
    @GetMapping("/catalog")
    public ResponseEntity<?> showCatalog(
            @RequestBody(required = false) GiftFilterRequest giftFilterRequest,
            @RequestParam(defaultValue = "1") int page
    ) {
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

    /**
     * Получить подробную информацию о подарке по id.
     *
     * @param id идентификатор подарка.
     * @return подробные данные о подарке.
     * @throws GiftNotFoundException если подарок с указанным id не найден.
     */
    @GetMapping("/gift/{id}")
    public ResponseEntity<?> getGiftById(@PathVariable Long id) throws GiftNotFoundException {
        return ResponseEntity.ok(catalogService.getGiftResponse(id));
    }

    /**
     * Поиск подарков по имени.
     *
     * @param query поисковый запрос.
     * @param page номер страницы (по умолчанию 1).
     * @return список подарков по результатам поиска.
     */
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

    /**
     * Добавить подарок в избранное.
     * Доступно только для авторизованных пользователей.
     * @param giftToFavoritesDto объект с id подарка для добавления в избранное.
     * @return сообщение с подтверждением добавления подарка в избраноое.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     * @throws UserNotFoundException если пользователь не найден.
     * @throws GiftNotFoundException если подарок с указанным id не найден.
     */
    @PostMapping("/addToFavorites")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addGiftToFavorites(@RequestBody GiftToFavoritesDto giftToFavoritesDto)
            throws UserNotAuthorizedException, UserNotFoundException, GiftNotFoundException {
        catalogService.addGiftToFavorites(giftToFavoritesDto);
        return ResponseEntity.status(HttpStatus.OK).body("Подарок добавлен в избранные!");
    }

    /**
     * Сортировка каталога подарков по указанному параметру.
     *
     * @param sort параметр сортировки.
     * @param page номер страницы (по умолчанию 1).
     * @return отсортированный список подарков.
     */
    @GetMapping("/sortCatalog")
    public ResponseEntity<?> sortCatalog(@RequestParam String sort, @RequestParam(defaultValue = "1") int page) {
        if (sort.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Запрос не может быть пустым!");
        }
        if (page <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Номер страницы должен быть больше 0!");
        }
        Pageable pageable = PageRequest.of(page - 1, 15);

        Page<GiftDto> searchResults = catalogService.searchGiftsBySortParam(sort.toLowerCase(), pageable);
        if (searchResults.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("Нет совпадений для запроса '" + sort + "'!");
        }
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(searchResults));
    }
}
