package ru.uniyar.podarok.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.uniyar.podarok.dtos.GiftDto;
import ru.uniyar.podarok.dtos.GiftFilterRequest;
import ru.uniyar.podarok.dtos.GiftResponseDto;
import ru.uniyar.podarok.dtos.GiftToFavoritesDto;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.services.CatalogService;

import java.math.BigDecimal;
import java.util.Collections;
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
    public void CatalogController_ShowCatalog_ReturnsCatalog() throws Exception {
        Page<GiftDto> mockPage = new PageImpl<>(Collections.singletonList(mock(GiftDto.class)));
        when(catalogService.getGiftsCatalog(any(GiftFilterRequest.class), any(PageRequest.class))).thenReturn(mockPage);
        when(pagedResourcesAssembler.toModel(mockPage)).thenReturn(null);

        mockMvc.perform(get("/catalog").param("page", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(catalogService, times(1)).getGiftsCatalog(any(GiftFilterRequest.class), any(PageRequest.class));
    }

    @Test
    public void CatalogController_ShowCatalog_ReturnsBadRequest_WithInvalidPageNumber() throws Exception {
        mockMvc.perform(get("/catalog").param("page", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Номер страницы должен быть больше 0."));
    }

    @Test
    public void CatalogController_ShowCatalog_ReturnsNoContentMessage() throws Exception {
        Page<GiftDto> emptyPage = Page.empty();
        when(catalogService.getGiftsCatalog(any(GiftFilterRequest.class), any(PageRequest.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/catalog").param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Нет элементов на странице!"));
        verify(catalogService, times(1)).getGiftsCatalog(any(GiftFilterRequest.class), any(PageRequest.class));
    }

    @Test
    public void CatalogController_GetGiftById_ReturnsStatusIsOk() throws Exception {
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
    void CatalogController_GetGiftById_ReturnsStatusIsNotFound() throws Exception {
        when(catalogService.getGiftResponse(1L)).thenThrow(new EntityNotFoundException("Подарок не найден!"));

        mockMvc.perform(get("/gift/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Подарок с id 1 не найден!"));

        verify(catalogService, times(1)).getGiftResponse(1L);
    }

    @Test
    void CatalogController_SearchGiftsByName_ReturnsStatusIsBadRequest_WhenQueryIsBlank() throws Exception {
        mockMvc.perform(get("/catalogSearch")
                        .param("query", "")
                        .param("page", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Запрос не может быть пустым!"));
    }

    @Test
    void CatalogController_SearchGiftsByName_ReturnsStatusIsBadRequest_WhenPageIsInvalid() throws Exception {
        mockMvc.perform(get("/catalogSearch")
                        .param("query", "test")
                        .param("page", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Номер страницы должен быть больше 0!"));
    }

    @Test
    void CatalogController_SearchGiftsByName_ReturnsStatusIsOk_WhenNoMatchesFound() throws Exception {
        String query = "no-match";
        Page<GiftDto> emptyPage = Page.empty();
        when(catalogService.searchGiftsByName(eq(query), any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/catalogSearch")
                        .param("query", query)
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Нет совпадений для запроса '" + query + "'!"));
    }

    @Test
    void CatalogController_AddGiftToFavorites_ReturnsStatusIsOk() throws Exception {
        GiftToFavoritesDto dto = new GiftToFavoritesDto(1L);
        doNothing().when(catalogService).addGiftToFavorites(eq(dto));

        mockMvc.perform(post("/addToFavorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Подарок добавлен в избранные!"));
    }

    @Test
    void CatalogController_AddGiftToFavorites_ReturnsStatusIsNotFound_WhenGiftNotFound() throws Exception {
        GiftToFavoritesDto dto = new GiftToFavoritesDto(1L);
        doThrow(new EntityNotFoundException("Подарок не найден!")).when(catalogService).addGiftToFavorites(eq(dto));

        mockMvc.perform(post("/addToFavorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Подарок с id 1 не найден!"));
    }

    @Test
    void CatalogController_AddGiftToFavorites_ReturnsStatusIsUnauthorized() throws Exception {
        GiftToFavoritesDto dto = new GiftToFavoritesDto(1L);
        doThrow(new UserNotAuthorizedException("Пользователь не авторизован!")).when(catalogService).addGiftToFavorites(eq(dto));

        mockMvc.perform(post("/addToFavorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Пользователь не авторизован!"));
    }

    @Test
    void CatalogController_AddGiftToFavorites_ReturnsStatusIsNotFound_WhenUserNotFound() throws Exception {
        GiftToFavoritesDto dto = new GiftToFavoritesDto(1L);
        doThrow(new UserNotFoundException("Пользователь не найден!")).when(catalogService).addGiftToFavorites(eq(dto));

        mockMvc.perform(post("/addToFavorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"giftId\":1}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Пользователь не найден!"));
    }
}

