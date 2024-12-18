package ru.uniyar.podarok.utils.Builders;

import org.springframework.stereotype.Component;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.entities.Review;
import ru.uniyar.podarok.entities.User;

import java.time.LocalDate;

/**
 * Строитель для создания объектов {@link Review}.
 */
@Component
public class ReviewBuilder {
    private String text;
    private LocalDate creationDate;
    private Integer rating;
    private Gift gift;
    private User user;

    /**
     * Устанавливает текст отзыва.
     *
     * @param text текст отзыва
     * @return текущий объект {@link ReviewBuilder} для дальнейшей настройки
     */
    public ReviewBuilder setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * Устанавливает дату создания отзыва.
     *
     * @param creationDate дата создания отзыва
     * @return текущий объект {@link ReviewBuilder} для дальнейшей настройки
     */
    public ReviewBuilder setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    /**
     * Устанавливает рейтинг отзыва.
     *
     * @param rating рейтинг отзыва (от 1 до 5)
     * @return текущий объект {@link ReviewBuilder} для дальнейшей настройки
     */
    public ReviewBuilder setRating(Integer rating) {
        this.rating = rating;
        return this;
    }

    /**
     * Устанавливает подарок, к которому относится отзыв.
     *
     * @param gift объект {@link Gift}, к которому относится отзыв
     * @return текущий объект {@link ReviewBuilder} для дальнейшей настройки
     */
    public ReviewBuilder setGift(Gift gift) {
        this.gift = gift;
        return this;
    }

    /**
     * Устанавливает пользователя, оставившего отзыв.
     *
     * @param user объект {@link User}, оставивший отзыв
     * @return текущий объект {@link ReviewBuilder} для дальнейшей настройки
     */
    public ReviewBuilder setUser(User user) {
        this.user = user;
        return this;
    }

    /**
     * Строит объект {@link Review} с заданными параметрами.
     *
     * @return новый объект {@link Review}, содержащий все параметры, заданные через методы билдера
     */
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
