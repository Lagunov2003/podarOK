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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.uniyar.podarok.dtos.GiftDto;
import ru.uniyar.podarok.dtos.GiftFilterRequest;
import ru.uniyar.podarok.dtos.GiftToFavoritesDto;
import ru.uniyar.podarok.dtos.ReviewRequestDto;
import ru.uniyar.podarok.exceptions.GiftNotFoundException;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.services.CatalogService;

/**
 * Контроллер для управления каталогом подарков.
 */
@Controller
@AllArgsConstructor
public class CatalogController {
    private CatalogService catalogService;
    /**
     * Асемблер для создания пагинированных ресурсов.
     * Используется для преобразования данных о подарках в формат с поддержкой постраничного вывода.
     */
    private PagedResourcesAssembler<GiftDto> pagedResourcesAssembler;

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
     * Показать каталог подарков с фильтрацией (по фильтрам или по опросу), поиском по названию, сортировке.
     *
     * @param giftFilterRequest объект с параметрами фильтрации.
     * @param name название подарка для поиска.
     * @param sort параметр сортировки.
     * @param page номер страницы (по умолчанию 1).
     * @return список подарков.
     */
    @GetMapping("/catalog")
    public ResponseEntity<?> showCatalog(
            @RequestBody(required = false) GiftFilterRequest giftFilterRequest,
            @RequestParam String name,
            @RequestParam String sort,
            @RequestParam(defaultValue = "1") int page
    ) {
        final int pageSize = 15;

        if (page <= 0) {
            return ResponseEntity.badRequest().body("Номер страницы должен быть больше 0.");
        }

        if (giftFilterRequest == null) {
            giftFilterRequest = new GiftFilterRequest();
        }

        if (name.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Запрос не может быть пустым!");
        }

        if (sort.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Запрос не может быть пустым!");
        }

        Pageable pageable = PageRequest.of(page - 1, pageSize);

        Page<GiftDto> giftsPage = catalogService.searchGiftsByFilters(giftFilterRequest, name, sort, pageable);

        if (giftsPage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("Нет элементов на странице!");
        }

        return ResponseEntity.ok(pagedResourcesAssembler.toModel(giftsPage));
    }

    /**
     * Добавление отзыва о подарке.
     *
     * @param reviewRequestDto объект с отзывом.
     * @return список подарков.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     * @throws UserNotFoundException если пользователь не найден.
     * @throws GiftNotFoundException если подарок с указанным id не найден.
     */
    @PostMapping("/addGiftReview")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addGiftReview(@RequestBody ReviewRequestDto reviewRequestDto)
            throws UserNotAuthorizedException, UserNotFoundException, GiftNotFoundException {
        catalogService.addGiftReview(reviewRequestDto);
        return ResponseEntity.ok("Отзыв успешно добавлен!");
    }
}
