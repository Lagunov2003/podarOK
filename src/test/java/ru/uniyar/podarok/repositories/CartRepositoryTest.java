package ru.uniyar.podarok.repositories;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.uniyar.podarok.entities.Cart;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.entities.User;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private EntityManager entityManager;
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
}
