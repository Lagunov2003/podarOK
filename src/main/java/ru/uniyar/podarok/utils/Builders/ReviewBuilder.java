package ru.uniyar.podarok.utils.Builders;

import org.springframework.stereotype.Component;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.entities.Review;
import ru.uniyar.podarok.entities.User;

import java.time.LocalDate;

@Component
public class ReviewBuilder {
    private String text;
    private LocalDate creationDate;
    private Integer rating;
    private Gift gift;
    private User user;

    public ReviewBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public ReviewBuilder setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public ReviewBuilder setRating(Integer rating) {
        this.rating = rating;
        return this;
    }

    public ReviewBuilder setGift(Gift gift) {
        this.gift = gift;
        return this;
    }

    public ReviewBuilder setUser(User user) {
        this.user = user;
        return this;
    }

    public Review build() {
        Review review = new Review();
        review.setText(this.text);
        review.setCreationDate(this.creationDate);
        review.setRating(this.rating);
        review.setGift(this.gift);
        review.setUser(this.user);
        return review;
    }
}