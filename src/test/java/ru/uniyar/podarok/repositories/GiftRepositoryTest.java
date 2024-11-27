package ru.uniyar.podarok.repositories;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.uniyar.podarok.entities.*;
import ru.uniyar.podarok.repositories.projections.GiftProjection;
import ru.uniyar.podarok.testEntities.GiftCategory;
import ru.uniyar.podarok.testEntities.GiftOccasion;
import ru.uniyar.podarok.testRepositories.*;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private Pageable pageable;
    private Gift gift1 = new Gift();
    private User user = new User();

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 100);
        BigDecimal budget = new BigDecimal("100.00");
        Boolean gender = true;
        Integer age = 25;
        Boolean urgency = true;

        user.setEmail("test@example.com");
        user.setGender(true);
        user.setFirstName("user");
        user = userRepository.save(user);
        entityManager.flush();

        Occasion occasion = new Occasion();
        occasion.setName("test");
        occasion = occasionRepository.save(occasion);

        Category category1 = new Category();
        category1.setName("name1");
        categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setName("name2");
        category2.setGifts(new ArrayList<>());
        category2 = categoryRepository.save(category2);
        entityManager.flush();

        gift1.setCategories(new ArrayList<>(List.of(category2)));
        gift1.setName("test");
        gift1.setOccasions(new ArrayList<>(List.of(occasion)));
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

        gift1.setCategories(new ArrayList<>(List.of(category2)));
        gift1.setRecommendation(giftRecommendation);
        gift1 = giftRepository.save(gift1);

        GiftCategory giftCategory = new GiftCategory();
        giftCategory.setCategoryId(category2.getId());
        giftCategory.setGiftId(gift1.getId());
        giftCategoryRepository.save(giftCategory);

        GiftOccasion giftOccasion = new GiftOccasion();
        giftOccasion.setOccasionId(occasion.getId());
        giftOccasion.setGiftId(gift1.getId());
        giftOccasionRepository.save(giftOccasion);

    }

    @BeforeEach
    void cleanDatabase() {
        entityManager.createNativeQuery("TRUNCATE TABLE gift RESTART IDENTITY CASCADE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE category RESTART IDENTITY CASCADE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE occasion RESTART IDENTITY CASCADE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE users RESTART IDENTITY CASCADE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE gift_recommendations RESTART IDENTITY CASCADE").executeUpdate();
    }

    @Test
    public void GiftRepository_FindAllGifts_ReturnsGift() {
        Page<GiftProjection> giftsPage = giftRepository.findAllGifts(pageable);

        assertNotNull(giftsPage);
        assertTrue(giftsPage.hasContent());
        GiftProjection actualGift = giftsPage.getContent().get(0);
        assertEquals(gift1.getName(), actualGift.getName());
        assertEquals(gift1.getPrice(), actualGift.getPrice());
    }

    @Test
    public void GiftRepository_FindGiftsByFilter_ReturnsGift() {
        BigDecimal budget = new BigDecimal("150.00");
        Boolean gender = true;
        Integer age = 30;
        List<Long> categories = List.of(1L, 2L);
        List<Long> occasions = List.of(1L);

        Page<GiftProjection> giftsPage = giftRepository.findGiftsByFilter(
                budget, gender, age, categories, occasions, pageable);

        assertNotNull(giftsPage);
        assertTrue(giftsPage.hasContent());
        giftsPage.forEach(gift -> {
            assertNotNull(gift.getId());
            assertTrue(gift.getPrice().compareTo(budget) <= 0);
        });
    }

    @Test
    public void GiftRepository_FindById_ReturnsGift() {
        Optional<Gift> gift = giftRepository.findById(1L);
        Gift result = gift.get();
        assertThat(result).isNotNull();
        assertEquals("test", gift1.getName());
    }
}
