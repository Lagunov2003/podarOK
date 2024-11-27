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
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.uniyar.podarok.dtos.GiftFilterRequest;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.repositories.projections.GiftProjection;
import ru.uniyar.podarok.services.CatalogService;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private PagedResourcesAssembler<GiftProjection> pagedResourcesAssembler;

    @Test
    public void CatalogController_ShowCatalog_ReturnsCatalog() throws Exception {
        Page<GiftProjection> mockPage = new PageImpl<>(Collections.singletonList(mock(GiftProjection.class)));
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
        Page<GiftProjection> emptyPage = Page.empty();
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
        when(catalogService.getGift(1L)).thenReturn(gift);

        mockMvc.perform(get("/gift/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.price").value(100));

        verify(catalogService, times(1)).getGift(1L);
    }

    @Test
    void CatalogController_GetGiftById_ThrowsEntityNotFoundException() throws Exception {
        when(catalogService.getGift(1L)).thenThrow(new EntityNotFoundException("Подарок не найден!"));

        mockMvc.perform(get("/gift/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Подарок с ID 1 не найден!"));

        verify(catalogService, times(1)).getGift(1L);
    }
}

