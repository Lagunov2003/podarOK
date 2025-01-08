package ru.uniyar.podarok.repositories;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.uniyar.podarok.entities.*;
import ru.uniyar.podarok.testEntities.GiftCategory;
import ru.uniyar.podarok.testEntities.GiftCategoryId;
import ru.uniyar.podarok.testEntities.GiftOccasion;
import ru.uniyar.podarok.testEntities.GiftOccasionId;
import ru.uniyar.podarok.testRepositories.*;


import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GiftRepositoryTest {
    @Autowired
    private GiftRepository giftRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GiftRecomendationsRepository giftRecomendationsRepository;
    @Autowired
    private GiftCategoryRepository giftCategoryRepository;
    @Autowired
    private GiftOccasionRepository giftOccasionRepository;
    @Autowired
    private OccasionRepository occasionRepository;
    @Autowired
    private GiftPhotoRepository giftPhotoRepository;
    @Autowired
    private GiftFeatureRepository giftFeatureRepository;
    @Autowired
    private GroupRepository groupRepository;
    private Pageable pageable;
    private Gift gift1 = new Gift();
    private User user = new User();
    private Category category1 = new Category();
    private Category category2 = new Category();
    private GiftGroup giftGroup = new GiftGroup();

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 100);

        user.setEmail("test@example.com");
        user.setGender(true);
        user.setFirstName("user");
        user = userRepository.save(user);
        entityManager.flush();

        Occasion occasion = new Occasion();
        occasion.setName("test");
        occasion = occasionRepository.save(occasion);

        Occasion occasion2 = new Occasion();
        occasion2.setName("test");
        occasion2 = occasionRepository.save(occasion2);

        category1.setName("name1");
        categoryRepository.save(category1);

        category2.setName("name2");
        category2.setGifts(new ArrayList<>());
        category2 = categoryRepository.save(category2);
        entityManager.flush();

        gift1.setCategories(new HashSet<>(List.of(category2)));
        gift1.setName("test");
        gift1.setOccasions(new HashSet<>(List.of(occasion)));
        gift1.setPrice(BigDecimal.valueOf(90));
        gift1 = giftRepository.save(gift1);
        entityManager.flush();

        category2.setGifts(new ArrayList<>(List.of(gift1)));
        category2 = categoryRepository.save(category2);
        entityManager.flush();

        GiftRecommendation giftRecommendation = new GiftRecommendation();
        giftRecommendation.setGender(true);
        giftRecommendation.setMinAge(18);
        giftRecommendation.setMaxAge(101);
        giftRecomendationsRepository.save(giftRecommendation);

        GiftGroup giftGroup0 = new GiftGroup();
        giftGroup0.setId(1L);
        groupRepository.save(giftGroup0);
        giftGroup.setId(2L);
        giftGroup = groupRepository.save(giftGroup);

        gift1.setCategories(new HashSet<>(List.of(category2)));
        gift1.setRecommendation(giftRecommendation);
        gift1.setGiftGroup(giftGroup0);
        gift1 = giftRepository.save(gift1);


        GiftCategory giftCategory = new GiftCategory();
        giftCategory.setCategoryId(category2.getId());
        giftCategory.setGiftId(gift1.getId());
        giftCategoryRepository.save(giftCategory);

        GiftOccasion giftOccasion = new GiftOccasion();
        giftOccasion.setOccasionId(occasion.getId());
        giftOccasion.setGiftId(gift1.getId());
        giftOccasionRepository.save(giftOccasion);
        entityManager.flush();
        entityManager.clear();
    }

    @BeforeEach
    void cleanDatabase() {
        entityManager.createNativeQuery("TRUNCATE TABLE gift RESTART IDENTITY CASCADE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE category RESTART IDENTITY CASCADE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE occasion RESTART IDENTITY CASCADE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE users RESTART IDENTITY CASCADE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE gift_recommendations RESTART IDENTITY CASCADE")
                .executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE gift_group RESTART IDENTITY CASCADE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE gift_photo RESTART IDENTITY CASCADE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE gift_category RESTART IDENTITY CASCADE")
                .executeUpdate();
    }

    @Test
    public void GiftRepository_FindById_ReturnsGift() {
        Optional<Gift> gift = giftRepository.findById(1L);

        Gift result = gift.get();
        assertThat(result).isNotNull();
        assertEquals("test", gift1.getName());
    }

    @Test
    public void GiftRepository_FindGiftsByCategoriesOrOccasions_ReturnsGiftList() {
        List<Gift> result = giftRepository.findGiftsByCategoriesOrOccasions(Arrays.asList(1L, 2L), List.of(1L));

        assertEquals(1, result.size());
        assertEquals("test", result.get(0).getName());
    }

    @Test
    public void GiftRepository_FindGiftsByGroupId_ReturnsGiftList() {
        List<Gift> result = giftRepository.findGiftsByGroupId(1L);

        assertEquals(1, result.size());
        assertEquals("test", result.get(0).getName());
    }

    @Test
    @Deprecated
    @Transactional
    public void GiftRepository_UpdateGift_VerifiesGiftUpdated() {
        giftRepository.updateGift(
                1L,
                BigDecimal.valueOf(200),
                1L,
                "Updated Description",
                "Updated Gift",
                2L);
        entityManager.flush();
        entityManager.clear();

        Gift updatedGift = giftRepository.findById(1L).get();
        assertNotNull(updatedGift);
        assertEquals(0, BigDecimal.valueOf(200).compareTo(updatedGift.getPrice()));
        assertEquals("Updated Description", updatedGift.getDescription());
        assertEquals("Updated Gift", updatedGift.getName());
        assertEquals(2L, updatedGift.getGiftGroup().getId());
    }

    @Test
    @Deprecated
    @Transactional
    public void GiftRepository_DeleteGiftCategories_VerifiesCategoryDeleted() {
        GiftCategory giftCategory = new GiftCategory();
        giftCategory.setGiftId(1L);
        giftCategory.setCategoryId(2L);
        giftCategoryRepository.save(giftCategory);
        entityManager.flush();
        entityManager.clear();
        giftRepository.deleteGiftCategories(1L);

        Optional<GiftCategory> deletedCategory = giftCategoryRepository.findById(
                new GiftCategoryId(2L, 1L));
        assertTrue(deletedCategory.isEmpty());
    }

    @Test
    @Deprecated
    @Transactional
    public void GiftRepository_UpdateGiftOccasion() {
        giftRepository.updateGiftOccasion(1L, 2L);

        Gift updatedGift = giftRepository.findById(1L).get();
        assertEquals(2L, updatedGift.getOccasions().stream().findFirst().get().getId());
    }

    @Test
    @Deprecated
    @Transactional
    public void GiftRepository_DeleteGiftPhotos_VerifiesPhotoDeleted() {
        GiftPhoto giftPhoto = new GiftPhoto();
        Gift gift = new Gift();
        gift.setId(1L);
        giftPhoto.setGift(gift);
        giftPhoto.setPhotoUrl("url");
        giftPhotoRepository.save(giftPhoto);
        entityManager.flush();
        entityManager.clear();

        giftRepository.deleteGiftPhotos(1L);

        Optional<GiftPhoto> deletedPhoto = giftPhotoRepository.findById(giftPhoto.getId());
        assertTrue(deletedPhoto.isEmpty());
    }

    @Test
    @Deprecated
    @Transactional
    public void GiftRepository_DeleteGiftFeatures_VerifiesFeatureDeleted() {
        GiftFeature giftFeature = new GiftFeature();
        Gift gift = new Gift();
        gift.setId(1L);
        giftFeature.setGift(gift);
        giftFeature.setItemName("name");
        giftFeature.setItemValue("value");
        giftFeatureRepository.save(giftFeature);
        entityManager.flush();
        entityManager.clear();

        giftRepository.deleteGiftFeatures(1L);

        Optional<GiftFeature> deletedFeature = giftFeatureRepository.findById(giftFeature.getId());
        assertTrue(deletedFeature.isEmpty());
    }

    @Test
    @Deprecated
    @Transactional
    public void GiftRepository_UpdateGiftRecommendation_VerifiesRecommendationUpdated() {
        GiftRecommendation recommendation = new GiftRecommendation();
        recommendation.setId(1L);
        recommendation.setGender(false);
        recommendation.setMinAge(18);
        recommendation.setMaxAge(30);
        giftRecomendationsRepository.save(recommendation);
        entityManager.flush();
        entityManager.clear();

        giftRepository.updateGiftRecommendation(1L, true, 25L, 35L);

        GiftRecommendation updatedRecommendation = giftRecomendationsRepository.findById(1L).get();
        assertNotNull(updatedRecommendation);
        assertTrue(updatedRecommendation.getGender());
        assertEquals(25, updatedRecommendation.getMinAge());
        assertEquals(35, updatedRecommendation.getMaxAge());
    }

    @Test
    @Deprecated
    @Transactional
    public void GiftRepository_AddGift_VerifiesGiftAdded() {
        Integer addedGiftId = giftRepository.addGift(
                BigDecimal.valueOf(200),
                1L,
                "Updated Description",
                "Updated Gift",
                2L);
        entityManager.flush();
        entityManager.clear();

        Gift addedGift = giftRepository.findById(Long.valueOf(addedGiftId)).get();
        assertNotNull(addedGift);
        assertEquals(0, BigDecimal.valueOf(200).compareTo(addedGift.getPrice()));
        assertEquals("Updated Description", addedGift.getDescription());
        assertEquals("Updated Gift", addedGift.getName());
        assertEquals(2L, addedGift.getGiftGroup().getId());
    }

    @Test
    @Deprecated
    @Transactional
    public void GiftRepository_AddGiftCategory_VerifiesCategoryAdded() {
        GiftCategory giftCategory = new GiftCategory();
        giftCategory.setGiftId(1L);
        giftCategory.setCategoryId(1L);

        giftRepository.addGiftCategory(1L, 1L);

        GiftCategory addedCategory = giftCategoryRepository.findById(
                new GiftCategoryId(1L, 1L)).get();
        assertNotNull(addedCategory);
        assertEquals(1L, addedCategory.getCategoryId());
    }

    @Test
    @Deprecated
    @Transactional
    public void GiftRepository_AddGiftOccasion_VerifiesCategoryAdded() {
        GiftOccasion giftOccasion = new GiftOccasion();
        giftOccasion.setGiftId(1L);
        giftOccasion.setOccasionId(2L);

        giftRepository.addGiftOccasion(1L, 2L);

        GiftOccasion addedOccasion = giftOccasionRepository.findById(
                new GiftOccasionId(2L, 1L)).get();
        assertNotNull(addedOccasion);
        assertEquals(2L, addedOccasion.getOccasionId());
    }

    @Test
    @Deprecated
    @Transactional
    public void GiftRepository_AddGiftPhoto_VerifiesPhotoAdded() {
        GiftPhoto giftPhoto = new GiftPhoto();
        Gift gift = new Gift();
        gift.setId(1L);
        giftPhoto.setGift(gift);
        giftPhoto.setPhotoUrl("url");

        giftRepository.addGiftPhoto(1L, "url");

        GiftPhoto addedPhoto = giftPhotoRepository.findById(1L).get();
        assertNotNull(addedPhoto);
        assertEquals("url", addedPhoto.getPhotoUrl());
    }

    @Test
    @Deprecated
    @Transactional
    public void GiftRepository_AddGiftFeature_VerifiesFeatureAdded() {
        GiftFeature giftFeature = new GiftFeature();
        Gift gift = new Gift();
        gift.setId(1L);
        giftFeature.setGift(gift);
        giftFeature.setItemName("name");
        giftFeature.setItemValue("value");

        giftRepository.addGiftFeature(1L, "name", "value");

        GiftFeature addedFeature = giftFeatureRepository.findById(1L).get();
        assertNotNull(addedFeature);
        assertEquals("name", addedFeature.getItemName());
        assertEquals("value", addedFeature.getItemValue());
    }

    @Test
    @Deprecated
    @Transactional
    public void GiftRepository_AddGiftRecommendation_VerifiesRecommendationAdded() {
        GiftRecommendation recommendation = new GiftRecommendation();
        recommendation.setId(2L);
        recommendation.setGender(false);
        recommendation.setMinAge(18);
        recommendation.setMaxAge(30);

        giftRepository.addGiftRecommendation(true, 18L, 30L);

        GiftRecommendation addedRecommendation = giftRecomendationsRepository.findById(2L).get();
        assertNotNull(addedRecommendation);
        assertTrue(addedRecommendation.getGender());
        assertEquals(18, addedRecommendation.getMinAge());
        assertEquals(30, addedRecommendation.getMaxAge());
    }

    @Test
    public void GiftRepository_FindAllByFiltersByNameAndByAverageRatingDesc_ReturnsGiftPage() {
        Page<Gift> result = giftRepository.findAllByFiltersByNameAndByAverageRatingDesc(
                BigDecimal.valueOf(100),
                true,
                20,
                List.of(2L),
                List.of(1L),
                "test",
                PageRequest.of(0, 15)
        );

        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().stream().anyMatch(gift -> gift.getId().equals(1L)));
    }

    @Test
    public void GiftRepository_FindAllByFiltersByNameAndByPriceAsc_ReturnsGiftPage() {
        Page<Gift> result = giftRepository.findAllByFiltersByNameAndByPriceAsc(
                BigDecimal.valueOf(100),
                true,
                20,
                List.of(2L),
                List.of(1L),
                "test",
                PageRequest.of(0, 15)
        );

        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().stream().anyMatch(gift -> gift.getId().equals(1L)));
    }

    @Test
    public void GiftRepository_FindAllByFiltersByNameAndByPriceDesc_ReturnsGiftPage() {
        Page<Gift> result = giftRepository.findAllByFiltersByNameAndByPriceDesc(
                BigDecimal.valueOf(100),
                true,
                20,
                List.of(2L),
                List.of(1L),
                "test",
                PageRequest.of(0, 15)
        );

        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().stream().anyMatch(gift -> gift.getId().equals(1L)));
    }

    @Test
    public void GiftRepository_FindAllByFiltersByName_ReturnsGiftPage() {
        Page<Gift> result = giftRepository.findAllByFiltersByName(
                BigDecimal.valueOf(100),
                true,
                20,
                List.of(2L),
                List.of(1L),
                "test",
                PageRequest.of(0, 15)
        );

        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().stream().anyMatch(gift -> gift.getId().equals(1L)));
    }

    @Test
    public void GiftRepository_FindAllGifts_ReturnsGift() {
        Page<Gift> giftsPage = giftRepository.findAllGifts(pageable);

        assertNotNull(giftsPage);
        assertTrue(giftsPage.hasContent());
        Gift actualGift = giftsPage.getContent().get(0);
        assertEquals(gift1.getName(), actualGift.getName());
        assertEquals(0, gift1.getPrice().compareTo(actualGift.getPrice()));
    }
}
