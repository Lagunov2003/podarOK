package ru.uniyar.podarok.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.repositories.projections.GiftProjection;
import ru.uniyar.podarok.services.CatalogService;

import java.util.Collections;

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
    public void CatalogController_ShowCatalog_ShouldReturnCatalog() throws Exception {
        Page<GiftProjection> mockPage = new PageImpl<>(Collections.singletonList(mock(GiftProjection.class)));
        when(catalogService.getGiftsCatalog(any(GiftFilterRequest.class), any(PageRequest.class))).thenReturn(mockPage);
        when(pagedResourcesAssembler.toModel(mockPage)).thenReturn(null);

        mockMvc.perform(get("/catalog").param("page", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(catalogService, times(1)).getGiftsCatalog(any(GiftFilterRequest.class), any(PageRequest.class));
    }

    @Test
    public void CatalogController_ShowCatalog_ShouldReturnBadRequest_WithInvalidPageNumber() throws Exception {
        mockMvc.perform(get("/catalog").param("page", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Номер страницы должен быть больше 0."));
    }

    @Test
    public void CatalogController_ShowCatalog_ShouldReturnNoContentMessage() throws Exception {
        Page<GiftProjection> emptyPage = Page.empty();
        when(catalogService.getGiftsCatalog(any(GiftFilterRequest.class), any(PageRequest.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/catalog").param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Нет элементов на странице!"));
        verify(catalogService, times(1)).getGiftsCatalog(any(GiftFilterRequest.class), any(PageRequest.class));
    }

    @Test
    public void CatalogController_ShowCatalog_ShouldReturnForbidden() throws Exception {
        when(catalogService.getGiftsCatalog(any(GiftFilterRequest.class), any(PageRequest.class)))
                .thenThrow(new UserNotFoundException("Пользователь не найден"));

        mockMvc.perform(get("/catalog").param("page", "1"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Ошибка авторизации или пользователь не найден"));
    }

    @Test
    public void CatalogController_ShowCatalog_ShouldReturnBadRequest_WithIllegalArgument() throws Exception {
        when(catalogService.getGiftsCatalog(any(GiftFilterRequest.class), any(PageRequest.class)))
                .thenThrow(new IllegalArgumentException("Вы не можете использовать фильтрацию по опросу и параметрам одновременно!"));

        mockMvc.perform(get("/catalog").param("page", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Вы не можете использовать фильтрацию по опросу и параметрам одновременно!"));
    }
}

