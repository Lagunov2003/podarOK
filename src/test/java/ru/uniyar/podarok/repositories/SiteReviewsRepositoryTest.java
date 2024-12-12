package ru.uniyar.podarok.repositories;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.uniyar.podarok.entities.SiteReviews;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SiteReviewsRepositoryTest {

    @Autowired
    private SiteReviewsRepository siteReviewsRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void SiteReviewsRepository_FindByAcceptedTrue_ReturnsAcceptedReviewsList() {
        SiteReviews acceptedReview = new SiteReviews();
        acceptedReview.setReview("Great site!");
        acceptedReview.setMark(5);
        acceptedReview.setAccepted(true);
        entityManager.persist(acceptedReview);
        SiteReviews rejectedReview = new SiteReviews();
        rejectedReview.setReview("Needs improvement");
        rejectedReview.setMark(2);
        rejectedReview.setAccepted(false);
        entityManager.persist(rejectedReview);

        List<SiteReviews> acceptedReviews = siteReviewsRepository.findByAcceptedTrue();

        assertEquals(1, acceptedReviews.size());
        assertEquals("Great site!", acceptedReviews.get(0).getReview());
    }

    @Test
    void SiteReviewsRepository_FindByAcceptedFalse_ReturnsRejectedReviewsList() {
        SiteReviews acceptedReview = new SiteReviews();
        acceptedReview.setReview("Amazing!");
        acceptedReview.setMark(5);
        acceptedReview.setAccepted(true);
        entityManager.persist(acceptedReview);
        SiteReviews rejectedReview = new SiteReviews();
        rejectedReview.setReview("Not good");
        rejectedReview.setMark(1);
        rejectedReview.setAccepted(false);
        entityManager.persist(rejectedReview);

        List<SiteReviews> rejectedReviews = siteReviewsRepository.findByAcceptedFalse();

        assertEquals(1, rejectedReviews.size());
        assertEquals("Not good", rejectedReviews.get(0).getReview());
    }

    @Test
    void SiteReviewsRepository_FindTop6ByAcceptedTrue_ReturnsAcceptedReviewsList() {
        for (int i = 1; i <= 8; i++) {
            SiteReviews review = new SiteReviews();
            review.setReview("Review " + i);
            review.setMark(i % 5 + 1); // Marks 1-5
            review.setAccepted(true);
            entityManager.persist(review);
        }

        List<SiteReviews> top6Reviews = siteReviewsRepository.findTop6ByAcceptedTrue();

        assertEquals(6, top6Reviews.size());
        assertEquals("Review 1", top6Reviews.get(0).getReview());
        assertEquals("Review 6", top6Reviews.get(5).getReview());
    }
}

