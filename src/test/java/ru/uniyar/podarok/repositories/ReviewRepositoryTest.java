package ru.uniyar.podarok.repositories;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.entities.Review;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReviewRepositoryTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ReviewRepository reviewRepository;
    @BeforeEach
    public void setUp() {
        Gift gift = new Gift();
        gift.setId(1L);
        Review review1 = new Review();
        review1.setId(1L);
        review1.setRating(5);
        review1.setGift(gift);
        Review review2 = new Review();
        review2.setId(2L);
        review2.setRating(3);
        review2.setGift(gift);
        reviewRepository.save(review1);
        reviewRepository.save(review2);
    }

    @BeforeEach
    public void cleanDatabase() {
        entityManager.createNativeQuery("TRUNCATE TABLE review RESTART IDENTITY CASCADE").executeUpdate();
    }

    @Test
    public void ReviewRepository_FindAverageRatingByGiftId_ReturnsRating() {
        Double rating = reviewRepository.findAverageRatingByGiftId(1L);

        assertNotNull(rating);
        assertEquals(4.0, rating);
    }

    @Test
    public void ReviewRepository_CountReviewsAmountById_ReturnsReviewCount() {
        Long reviews = reviewRepository.countReviewsAmountById(1L);

        assertNotNull(reviews);
        assertEquals(2, reviews);
    }

    @Test
    public void ReviewRepository_FindReviewsByGiftId_ReturnsReviewList() {
        List<Review> reviews = reviewRepository.findReviewsByGiftId(1L);

        assertNotNull(reviews);
        assertEquals(2, reviews.size());
    }
}
