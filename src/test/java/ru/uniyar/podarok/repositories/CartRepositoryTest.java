package ru.uniyar.podarok.repositories;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.uniyar.podarok.entities.Cart;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.entities.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;
    private Gift gift;
    private User user;
    private Cart cart;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setFirstName("user");
        user.setEmail("test@example.com");

        entityManager.persist(user);
        entityManager.flush();

        gift = new Gift();
        gift.setName("gift");
        gift.setPrice(BigDecimal.valueOf(100));

        entityManager.persist(gift);
        entityManager.flush();

        cart = new Cart();
        cart.setItemCount(1);
        cart.setUser(user);
        cart.setGift(gift);

        cart = cartRepository.save(cart);
    }

    @BeforeEach
    void cleanDatabase() {
        entityManager.createNativeQuery("TRUNCATE TABLE cart RESTART IDENTITY CASCADE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE users RESTART IDENTITY CASCADE").executeUpdate();

    }

    @Test
    public void CartRepository_FindItemByGiftId_ReturnsCart() {
        Optional<Cart> result = cartRepository.findItemByGiftId(gift.getId());

        assertTrue(result.isPresent());
        assertEquals(cart.getId(), result.get().getId());
        assertEquals(cart.getItemCount(), result.get().getItemCount());
        assertEquals(cart.getUser().getId(), result.get().getUser().getId());
        assertEquals(cart.getGift().getId(), result.get().getGift().getId());
    }

    @Test
    public void CartRepository_FindItemByGiftId_ReturnsIsEmpty() {
        Optional<Cart> result = cartRepository.findItemByGiftId(-1L);

        assertTrue(result.isEmpty());
    }

    @Test
    public void CartRepository_FindItemByGiftIdAndUserId_ReturnsCart() {
        Optional<Cart> result = cartRepository.findItemByGiftIdAndUserId(gift.getId(), user.getId());

        assertTrue(result.isPresent());
        assertEquals(cart.getId(), result.get().getId());
        assertEquals(cart.getItemCount(), result.get().getItemCount());
        assertEquals(cart.getUser().getId(), result.get().getUser().getId());
        assertEquals(cart.getGift().getId(), result.get().getGift().getId());
    }

    @Test
    public void CartRepository_FindItemByGiftIdAndUserId_ReturnsIsEmpty() {
        Optional<Cart> result = cartRepository.findItemByGiftIdAndUserId(-1L, -1L);

        assertTrue(result.isEmpty());
    }

    @Test
    @Transactional
    void CartRepository_DeleteAllByUserId_ReturnsIsEmpty() {
        Cart cart1 = new Cart();
        cart1.setUser(user);
        cart1.setItemCount(3);
        Cart cart2 = new Cart();
        cart2.setUser(user);
        cart2.setItemCount(5);
        cartRepository.saveAll(List.of(cart1, cart2));

        cartRepository.deleteAllByUserId(user.getId());
        entityManager.flush();
        entityManager.clear();

        List<Cart> remainingCarts = cartRepository.findAll();
        assertThat(remainingCarts).isEmpty();
    }

    @Test
    @Transactional
    void CartRepository_DeleteAllByUserId_ReturnsRemainingCart() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setPassword("password123");
        user1 = userRepository.save(user1);
        Cart cart1 = new Cart();
        cart1.setUser(user1);
        cart1.setItemCount(3);
        cartRepository.saveAll(List.of(cart1, cart));

        cartRepository.deleteAllByUserId(user1.getId());

        List<Cart> remainingCarts = cartRepository.findAll();
        assertThat(remainingCarts).hasSize(1);
        assertThat(remainingCarts.get(0).getUser().getId()).isEqualTo(user.getId());
    }
}
