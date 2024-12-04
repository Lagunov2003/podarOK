package ru.uniyar.podarok.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.uniyar.podarok.dtos.AddGiftDto;
import ru.uniyar.podarok.dtos.ChangeGiftDto;
import ru.uniyar.podarok.dtos.GiftDto;
import ru.uniyar.podarok.dtos.GiftFilterRequest;
import ru.uniyar.podarok.entities.Category;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.entities.GiftRecommendation;
import ru.uniyar.podarok.entities.Occasion;
import ru.uniyar.podarok.repositories.GiftRepository;
import ru.uniyar.podarok.utils.GiftDtoConverter;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GiftServiceTest {

    @Mock
    private GiftRepository giftRepository;
    @Mock
    private GiftFilterService giftFilterService;
    @Mock
    private GiftDtoConverter giftDtoConverter;
    @InjectMocks
    private GiftService giftService;

    private Pageable pageable;



    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);

        Occasion occasion = new Occasion();
        occasion.setId(1L);
        occasion.setName("Birthday");
    }


    @Test
    public void GiftService_GetAllGifts_ReturnsGiftProjection() {
        Page<Gift> mockPage = new PageImpl<>(List.of(mock(Gift.class)));
        Page<GiftDto> mockDtoPage = new PageImpl<>(List.of(mock(GiftDto.class)));
        when(giftRepository.findAllGifts(pageable)).thenReturn(mockPage);
        when(giftDtoConverter.convertToGiftDtoPage(mockPage)).thenReturn(mockDtoPage);

        Page<GiftDto> result = giftService.getAllGifts(pageable);

        assertNotNull(result);
        assertTrue(result.hasContent());
        assertEquals(mockDtoPage, result);
        verify(giftRepository, times(1)).findAllGifts(pageable);
        verify(giftDtoConverter, times(1)).convertToGiftDtoPage(mockPage);
    }

    @Test
    public void GiftService_GetGiftsByFilter_ReturnsGiftProjection() {
        GiftFilterRequest filterRequest = new GiftFilterRequest();
        GiftFilterRequest processedRequest = new GiftFilterRequest();
        Page<Gift> mockGiftsPage = new PageImpl<>(List.of(mock(Gift.class)));
        Page<GiftDto> mockDtoPage = new PageImpl<>(List.of(mock(GiftDto.class)));
        when(giftFilterService.processRequest(filterRequest)).thenReturn(processedRequest);
        when(giftRepository.findGiftsByFilter(
                any(), any(), any(), any(), any(), eq(pageable)
        )).thenReturn(mockGiftsPage);
        when(giftDtoConverter.convertToGiftDtoPage(mockGiftsPage)).thenReturn(mockDtoPage);

        Page<GiftDto> result = giftService.getGiftsByFilter(filterRequest, pageable);

        assertNotNull(result);
        assertTrue(result.hasContent());
        assertEquals(mockDtoPage, result);
        verify(giftRepository, times(1)).findGiftsByFilter(
                any(), any(), any(), any(), any(), eq(pageable)
        );
    }

    @Test
    public void GiftService_GetGiftById_ReturnsGift() {
        Gift gift = new Gift();
        gift.setName("test");
        gift.setId(1L);
        when(giftRepository.findById(1L)).thenReturn(Optional.of(gift));

        Gift result = giftService.getGiftById(1L);

        assertEquals("test", result.getName());
        assertEquals(1L, result.getId());
    }

    @Test
    public void GiftService_GetGiftById_ThrowsEntityNotFoundException() {
        Long giftId = 1L;
        when(giftRepository.findById(giftId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> giftService.getGiftById(giftId));
        assertEquals("Подарок не найден!", exception.getMessage());
        verify(giftRepository, times(1)).findById(giftId);
    }

    @Test
    public void GiftService_DeleteGift_VerifiesGiftIsDeleted() {
        Long giftId = 1L;
        when(giftRepository.existsById(giftId)).thenReturn(true);

        giftService.deleteGift(giftId);

        verify(giftRepository, times(1)).existsById(giftId);
        verify(giftRepository, times(1)).deleteById(giftId);
    }

    @Test
    public void GiftService_DeleteGift_ThrowsEntityNotFoundException() {
        Long giftId = 1L;
        when(giftRepository.existsById(giftId)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> giftService.deleteGift(giftId));
        assertEquals("Подарок с Id 1 не найден!", exception.getMessage());
        verify(giftRepository, times(1)).existsById(giftId);
        verify(giftRepository, never()).deleteById(anyLong());
    }

    @Test
    public void GiftService_GetSimilarGifts_ReturnsSimilarGiftDtoList() {
        Gift gift = mock(Gift.class);
        Category category = mock(Category.class);
        Occasion occasion = mock(Occasion.class);
        when(category.getId()).thenReturn(1L);
        when(occasion.getId()).thenReturn(2L);
        when(gift.getCategories()).thenReturn(Set.of(category));
        when(gift.getOccasions()).thenReturn(Set.of(occasion));
        when(gift.getId()).thenReturn(1L);
        Gift similarGift = mock(Gift.class);
        when(similarGift.getId()).thenReturn(2L);
        when(giftRepository.findGiftsByCategoriesOrOccasions(any(), any())).thenReturn(List.of(similarGift));
        GiftDto similarGiftDto = mock(GiftDto.class);
        when(giftDtoConverter.convertToGiftDtoList(List.of(similarGift))).thenReturn(List.of(similarGiftDto));

        List<GiftDto> result = giftService.getSimilarGifts(gift);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(similarGiftDto, result.get(0));
        verify(giftRepository, times(1)).findGiftsByCategoriesOrOccasions(any(), any());
        verify(giftDtoConverter, times(1)).convertToGiftDtoList(List.of(similarGift));
    }

    @Test
    void GiftService_UpdateGift_VerifiesGiftIsUpdated() throws Exception {
        ChangeGiftDto changeGiftDto = new ChangeGiftDto();
        changeGiftDto.setId(1L);
        changeGiftDto.setPrice(BigDecimal.valueOf(100));
        changeGiftDto.setDescription("Обновленное описание");
        changeGiftDto.setName("Обновленное название");
        changeGiftDto.setGroupId(2L);
        changeGiftDto.setPhotos(List.of("photo1.png", "photo2.png"));
        changeGiftDto.setCategories(List.of(1L, 2L));
        changeGiftDto.setOccasions(List.of(3L, 4L));
        changeGiftDto.setFeatures(new HashMap<>(Map.of("цвет", "красный", "размер", "L")));
        changeGiftDto.setGender(true);
        changeGiftDto.setMinAge(18L);
        changeGiftDto.setMaxAge(50L);
        Gift mockGift = mock(Gift.class);
        GiftRecommendation recommendation = mock(GiftRecommendation.class);
        when(mockGift.getRecommendation()).thenReturn(recommendation);
        when(recommendation.getId()).thenReturn(10L);
        when(giftRepository.findById(changeGiftDto.getId())).thenReturn(Optional.of(mockGift));

        giftService.updateGift(changeGiftDto);

        verify(giftRepository, times(1)).updateGift(
                changeGiftDto.getId(),
                changeGiftDto.getPrice(),
                10L,
                changeGiftDto.getDescription(),
                changeGiftDto.getName(),
                changeGiftDto.getGroupId()
        );
        verify(giftRepository, times(1)).updateGiftRecommendation(
                10L,
                changeGiftDto.getGender(),
                changeGiftDto.getMinAge(),
                changeGiftDto.getMaxAge()
        );
        verify(giftRepository, times(2)).updateGiftPhoto(eq(changeGiftDto.getId()), anyString());
        verify(giftRepository, times(2)).updateGiftCategory(eq(changeGiftDto.getId()), anyLong());
        verify(giftRepository, times(2)).updateGiftOccasion(eq(changeGiftDto.getId()), anyLong());
        verify(giftRepository, times(2)).updateGiftFeature(eq(changeGiftDto.getId()), anyString(), anyString());
    }

    @Test
    void GiftService_AddGift_VerifiesGiftIsAdded() {
        AddGiftDto addGiftDto = new AddGiftDto();
        addGiftDto.setPrice(BigDecimal.valueOf(200));
        addGiftDto.setDescription("Описание");
        addGiftDto.setName("Название");
        addGiftDto.setGroupId(3L);
        addGiftDto.setPhotos(List.of("photo1.png", "photo2.png"));
        addGiftDto.setCategories(List.of(5L, 6L));
        addGiftDto.setOccasions(List.of(7L, 8L));
        addGiftDto.setFeatures(new HashMap<>(Map.of("материал", "дерево", "вес", "2кг")));
        addGiftDto.setGender(false);
        addGiftDto.setMinAge(10L);
        addGiftDto.setMaxAge(60L);
        Long recommendationId = 15L;
        Long giftId = 20L;
        when(giftRepository.addGiftRecommendation(addGiftDto.getGender(), addGiftDto.getMinAge(), addGiftDto.getMaxAge()))
                .thenReturn(recommendationId.intValue());
        when(giftRepository.addGift(
                addGiftDto.getPrice(),
                recommendationId,
                addGiftDto.getDescription(),
                addGiftDto.getName(),
                addGiftDto.getGroupId()
        )).thenReturn(giftId.intValue());

        giftService.addGift(addGiftDto);

        verify(giftRepository, times(1)).addGiftRecommendation(
                addGiftDto.getGender(),
                addGiftDto.getMinAge(),
                addGiftDto.getMaxAge()
        );
        verify(giftRepository, times(1)).addGift(
                addGiftDto.getPrice(),
                recommendationId,
                addGiftDto.getDescription(),
                addGiftDto.getName(),
                addGiftDto.getGroupId()
        );
        verify(giftRepository, times(2)).addGiftPhoto(eq(giftId), anyString());
        verify(giftRepository, times(2)).addGiftCategory(eq(giftId), anyLong());
        verify(giftRepository, times(2)).addGiftOccasion(eq(giftId), anyLong());
        verify(giftRepository, times(2)).addGiftFeature(eq(giftId), anyString(), anyString());
    }

    @Test
    void GiftService_SearchGiftsByName_ReturnsPageOfGiftDto() {
        String name = "gift";
        Pageable pageable = PageRequest.of(0, 10);
        Gift gift = new Gift();
        gift.setId(1L);
        gift.setName(name);
        Page<Gift> giftPage = new PageImpl<>(List.of(gift));
        Page<GiftDto> expectedDtoPage = new PageImpl<>(List.of(new GiftDto(1L, "Gift Name", new BigDecimal("100.00"), "photo")));
        when(giftRepository.findGiftsByName(name, pageable)).thenReturn(giftPage);
        when(giftDtoConverter.convertToGiftDtoPage(giftPage)).thenReturn(expectedDtoPage);

        Page<GiftDto> result = giftService.searchGiftsByName(name, pageable);

        assertEquals(expectedDtoPage, result);
        verify(giftRepository, times(1)).findGiftsByName(name, pageable);
        verify(giftDtoConverter, times(1)).convertToGiftDtoPage(giftPage);
    }

    @Test
    void GiftService_SearchGiftsByName_ReturnsEmptyPage() {
        String name = "unknown";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Gift> emptyGiftPage = Page.empty(pageable);
        Page<GiftDto> emptyDtoPage = Page.empty(pageable);
        when(giftRepository.findGiftsByName(name, pageable)).thenReturn(emptyGiftPage);
        when(giftDtoConverter.convertToGiftDtoPage(emptyGiftPage)).thenReturn(emptyDtoPage);

        Page<GiftDto> result = giftService.searchGiftsByName(name, pageable);

        assertTrue(result.isEmpty());
        verify(giftRepository, times(1)).findGiftsByName(name, pageable);
        verify(giftDtoConverter, times(1)).convertToGiftDtoPage(emptyGiftPage);
    }

    @Test
    void GiftService_GetGiftsByGroupId_ReturnsListOfGifts() {
        Long groupId = 1L;
        Gift gift1 = new Gift();
        gift1.setId(1L);
        gift1.setName("gift 1");
        Gift gift2 = new Gift();
        gift2.setId(2L);
        gift2.setName("gift 2");
        List<Gift> expectedGifts = List.of(gift1, gift2);
        when(giftRepository.findGiftsByGroupId(groupId)).thenReturn(expectedGifts);

        List<Gift> result = giftService.getGiftsByGroupId(groupId);

        assertEquals(expectedGifts, result);
        verify(giftRepository, times(1)).findGiftsByGroupId(groupId);
    }

    @Test
    void GiftService_GetGiftsByGroupId_ReturnsEmptyList() {
        Long groupId = 999L;
        List<Gift> emptyGifts = List.of();
        when(giftRepository.findGiftsByGroupId(groupId)).thenReturn(emptyGifts);

        List<Gift> result = giftService.getGiftsByGroupId(groupId);

        assertTrue(result.isEmpty());
        verify(giftRepository, times(1)).findGiftsByGroupId(groupId);
    }

}
