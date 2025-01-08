package ru.uniyar.podarok.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.uniyar.podarok.dtos.GiftDto;
import ru.uniyar.podarok.dtos.GiftResponseDto;
import ru.uniyar.podarok.dtos.GiftFavoritesDto;
import ru.uniyar.podarok.dtos.ReviewRequestDto;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.exceptions.*;
import ru.uniyar.podarok.services.CatalogService;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WithMockUser
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CatalogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CatalogService catalogService;

    @MockBean
    private PagedResourcesAssembler<GiftDto> pagedResourcesAssembler;

    @Test
    public void CatalogController_ShowCatalog_ReturnsStatusIsOk_WhenIsNotEmpty()
            throws Exception {
        Page<GiftDto> mockPage = new PageImpl<>(List.of(new GiftDto(
                1L,
             "Gift 1",
                    BigDecimal.valueOf(100),
            "photo1",
            false),
                new GiftDto(2L, "Gift 2", BigDecimal.valueOf(200), "photo2", false)));
        when(catalogService.getGiftsCatalog(any(), eq("test"), eq("asc"), any())).thenReturn(mockPage);
        PagedModel<?> pagedModel = PagedModel.of(mockPage.getContent(),
                new PagedModel.PageMetadata(15, 0, 2, 1));
        when(pagedResourcesAssembler.toModel(mockPage)).thenReturn((PagedModel<EntityModel<GiftDto>>) pagedModel);

        mockMvc.perform(post("/catalog").param("name", "test")
                        .param("sort", "asc")
                        .param("page", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.totalElements").value(2L));
        verify(catalogService, times(1))
                .getGiftsCatalog(any(), eq("test"), eq("asc"), any());
    }

    @Test
    void CatalogController_ShowCatalog_ReturnsStatusIsBadRequest()
            throws Exception {
        mockMvc.perform(post("/catalog")
                        .param("name", "test")
                        .param("sort", "asc")
                        .param("page", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Номер страницы должен быть больше 0."));
    }

    @Test
    void CatalogController_ShowCatalog_ReturnsStatusIsOk_WhenPageIsEmpty()
            throws Exception {
        Page<GiftDto> emptyPage = Page.empty();
        when(catalogService.getGiftsCatalog(any(), eq("test"), eq("asc"), any()))
                .thenReturn(emptyPage);

        mockMvc.perform(post("/catalog")
                        .param("name", "test")
                        .param("sort", "asc")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Нет элементов на странице!"));
    }


    @Test
    void CatalogController_ShowCatalog_ReturnsStatusIsOk_WhenMissingGiftFilterRequest()
            throws Exception {
        Page<GiftDto> giftPage = Page.empty();
        when(catalogService.getGiftsCatalog(any(), eq("test"), eq("asc"), any()))
                .thenReturn(giftPage);

        mockMvc.perform(post("/catalog")
                        .param("name", "test")
                        .param("sort", "asc")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Нет элементов на странице!"));
    }

    @Test
    public void CatalogController_GetGiftById_ReturnsStatusIsOk()
            throws Exception {
        Gift gift = new Gift();
        gift.setId(1L);
        gift.setName("test");
        gift.setPrice(BigDecimal.valueOf(100));
        GiftResponseDto giftResponseDto = new GiftResponseDto();
        giftResponseDto.setGroupGifts(List.of(gift));
        when(catalogService.getGiftResponse(1L)).thenReturn(giftResponseDto);

        mockMvc.perform(get("/gift/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupGifts[0].id").value(1L))
                .andExpect(jsonPath("$.groupGifts[0].name").value("test"))
                .andExpect(jsonPath("$.groupGifts[0].price").value(100));
        verify(catalogService, times(1)).getGiftResponse(1L);
    }

    @Test
    void CatalogController_GetGiftById_ReturnsStatusIsNotFound()
            throws Exception {
        when(catalogService.getGiftResponse(1L)).thenThrow(
                new GiftNotFoundException("Подарок не найден!"));

        mockMvc.perform(get("/gift/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Подарок не найден!"));
        verify(catalogService, times(1)).getGiftResponse(1L);
    }

    @Test
    void CatalogController_AddGiftToFavorites_ReturnsStatusIsOk()
            throws Exception {
        GiftFavoritesDto dto = new GiftFavoritesDto(1L);
        doNothing().when(catalogService).addGiftToFavorites(eq(dto));

        mockMvc.perform(post("/addToFavorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Подарок добавлен в избранные!"));
    }

    @Test
    void CatalogController_AddGiftToFavorites_ReturnsStatusIsNotFound_WhenGiftNotFound()
            throws Exception {
        GiftFavoritesDto dto = new GiftFavoritesDto(1L);
        doThrow(new GiftNotFoundException("Подарок не найден!")).when(catalogService).addGiftToFavorites(eq(dto));

        mockMvc.perform(post("/addToFavorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Подарок не найден!"));
    }

    @Test
    void CatalogController_AddGiftToFavorites_ReturnsStatusIsUnauthorized()
            throws Exception {
        GiftFavoritesDto dto = new GiftFavoritesDto(1L);
        doThrow(new UserNotAuthorizedException("Пользователь не авторизован!"))
                .when(catalogService).addGiftToFavorites(eq(dto));

        mockMvc.perform(post("/addToFavorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }

    @Test
    void CatalogController_AddGiftToFavorites_ReturnsStatusIsNotFound_WhenUserNotFound()
            throws Exception {
        GiftFavoritesDto dto = new GiftFavoritesDto(1L);
        doThrow(new UserNotFoundException("Пользователь не найден!"))
                .when(catalogService).addGiftToFavorites(eq(dto));

        mockMvc.perform(post("/addToFavorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден!"));
    }

    @Test
    void CatalogController_AddGiftToFavorites_ReturnsStatusIsConflict()
            throws Exception {
        GiftFavoritesDto dto = new GiftFavoritesDto(1L);
        doThrow(new FavoritesGiftAlreadyExistException("Подарок уже в избранном!"))
                .when(catalogService).addGiftToFavorites(eq(dto));

        mockMvc.perform(post("/addToFavorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Подарок уже в избранном!"));
    }

    @Test
    void CatalogController_DeleteFromFavorites_ReturnsStatusIsOk()
            throws Exception {
        GiftFavoritesDto dto = new GiftFavoritesDto(1L);
        doNothing().when(catalogService).deleteFromFavorites(eq(dto));

        mockMvc.perform(post("/deleteFromFavorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Подарок удалён из избранных!"));
    }

    @Test
    void CatalogController_DeleteFromFavorites_ReturnsStatusIsNotFound_WhenGiftNotFound()
            throws Exception {
        GiftFavoritesDto dto = new GiftFavoritesDto(1L);
        doThrow(new GiftNotFoundException("Подарок не найден!")).when(catalogService).deleteFromFavorites(eq(dto));

        mockMvc.perform(post("/deleteFromFavorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Подарок не найден!"));
    }

    @Test
    void CatalogController_DeleteFromFavorites_ReturnsStatusIsUnauthorized()
            throws Exception {
        GiftFavoritesDto dto = new GiftFavoritesDto(1L);
        doThrow(new UserNotAuthorizedException("Пользователь не авторизован!"))
                .when(catalogService).deleteFromFavorites(eq(dto));

        mockMvc.perform(post("/deleteFromFavorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }

    @Test
    void CatalogController_DeleteFromFavorites_ReturnsStatusIsNotFound_WhenUserNotFound()
            throws Exception {
        GiftFavoritesDto dto = new GiftFavoritesDto(1L);
        doThrow(new UserNotFoundException("Пользователь не найден!"))
                .when(catalogService).deleteFromFavorites(eq(dto));

        mockMvc.perform(post("/deleteFromFavorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден!"));
    }

    @Test
    void CatalogController_DeleteFromFavorites_ReturnsStatusIsNotFound_WhenGiftNotInFavorites()
            throws Exception {
        GiftFavoritesDto dto = new GiftFavoritesDto(1L);
        doThrow(new FavoritesGiftNotFoundException("Подарка нет в избранном!"))
                .when(catalogService).deleteFromFavorites(eq(dto));

        mockMvc.perform(post("/deleteFromFavorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Подарка нет в избранном!"));
    }

    @Test
    void CatalogController_AddGiftReview_ReturnsStatusIsOk()
            throws Exception {
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto();
        reviewRequestDto.setGiftId(1L);
        reviewRequestDto.setRating(5);
        reviewRequestDto.setText("Отличный подарок!");
        doNothing().when(catalogService).addGiftReview(any(ReviewRequestDto.class));

        mockMvc.perform(post("/addGiftReview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(reviewRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Отзыв успешно добавлен!"));
    }

    @Test
    void CatalogController_AddGiftReview_ReturnsStatusIsUnauthorized()
            throws Exception {
        doThrow(new UserNotAuthorizedException("Пользователь не авторизован!"))
                .when(catalogService).addGiftReview(any(ReviewRequestDto.class));

        mockMvc.perform(post("/addGiftReview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new ReviewRequestDto())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void CatalogController_AddGiftReview_ReturnsStatusIsNotFound_WhenUserNotFound()
            throws Exception {
        doThrow(new UserNotFoundException("Пользователь не найден!"))
                .when(catalogService).addGiftReview(any(ReviewRequestDto.class));

        mockMvc.perform(post("/addGiftReview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new ReviewRequestDto())))
                .andExpect(status().isNotFound());
    }

    @Test
    void CatalogController_AddGiftReview_ReturnsStatusIsNotFound_WhenGiftNotFound()
            throws Exception {
        doThrow(new GiftNotFoundException("Подарок не найден!"))
                .when(catalogService).addGiftReview(any(ReviewRequestDto.class));

        mockMvc.perform(post("/addGiftReview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new ReviewRequestDto())))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Подарок не найден!"));
    }

    @Test
    void CatalogController_AddGiftReview_ReturnsStatusIsConflict()
            throws Exception {
        doThrow(new GiftReviewAlreadyExistException("Отзыв уже существует!"))
                .when(catalogService).addGiftReview(any(ReviewRequestDto.class));

        mockMvc.perform(post("/addGiftReview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new ReviewRequestDto())))
                .andExpect(status().isConflict())
                .andExpect(content().string("Отзыв уже существует!"));
    }

}

