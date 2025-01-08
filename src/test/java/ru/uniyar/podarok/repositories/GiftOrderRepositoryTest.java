package ru.uniyar.podarok.repositories;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.uniyar.podarok.entities.*;
import ru.uniyar.podarok.testEntities.GiftCategory;
import ru.uniyar.podarok.testEntities.GiftOccasion;
import ru.uniyar.podarok.testRepositories.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GiftOrderRepositoryTest {
    @Autowired
    private GiftOrderRepository giftOrderRepository;

    @Autowired
    private GiftRepository giftRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private OccasionRepository occasionRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private GiftRecomendationsRepository giftRecomendationsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GiftCategoryRepository giftCategoryRepository;
    @Autowired
    private GiftOccasionRepository giftOccasionRepository;
    private Gift gift1 = new Gift();
    private User user = new User();
    private Category category1 = new Category();
    private Category category2 = new Category();
    private GiftGroup giftGroup = new GiftGroup();

    @BeforeEach
    void setUp() {
        GiftOrder giftOrder = new GiftOrder();

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

        Category category1 = new Category();
        category1.setName("name1");
        categoryRepository.save(category1);

        Category category2 = new Category();
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

        giftGroup.setId(1L);
        giftGroup = groupRepository.save(giftGroup);

        gift1.setCategories(new HashSet<>(List.of(category2)));
        gift1.setRecommendation(giftRecommendation);
        gift1.setGiftGroup(giftGroup);
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
        Order order = new Order();
        order.setId(1L);
        order.setStatus("Оформлен");
        order.setOrderCost(BigDecimal.valueOf(1000));
        order.setInformation("ул. Союзная, д. 144");
        order.setDeliveryDate(LocalDate.now());
        order.setFromDeliveryTime(LocalTime.now());
        order.setPayMethod("cash");
        order.setRecipientEmail("test@example.com");
        order.setToDeliveryTime(LocalTime.now());
        orderRepository.save(order);
        giftOrder.setOrder(order);
        giftOrder.setGift(gift1);
        giftOrder.setItemCount(3);
        giftOrderRepository.save(giftOrder);
        entityManager.flush();
    }

    @BeforeEach
    void cleanDatabase() {
        entityManager.createNativeQuery("TRUNCATE TABLE Gift_Order RESTART IDENTITY CASCADE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE orders RESTART IDENTITY CASCADE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE gift RESTART IDENTITY CASCADE").executeUpdate();
    }

    @Test
    void GiftOrderRepository_FindGiftOrdersByOrderId_ReturnsGiftOrders() {
        Long orderId = 1L;

        Set<GiftOrder> giftOrders = giftOrderRepository.findGiftOrdersByOrderId(orderId);

        assertThat(giftOrders).isNotEmpty();
        assertThat(giftOrders.size()).isEqualTo(1);
        giftOrders.forEach(giftOrder -> {
            assertThat(giftOrder.getOrder().getId()).isEqualTo(orderId);
            assertThat(giftOrder.getGift()).isNotNull();
        });
    }

    @Test
    void GiftOrderRepository_FindGiftOrdersByOrderId_ReturnsEmptyGiftOrders() {
        Long nonExistentOrderId = 999L;

        Set<GiftOrder> giftOrders = giftOrderRepository.findGiftOrdersByOrderId(nonExistentOrderId);

        assertThat(giftOrders).isEmpty();
    }
}
