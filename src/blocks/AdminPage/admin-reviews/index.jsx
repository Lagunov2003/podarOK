import React from "react";
import "./style.scss";

function AdminReviews() {
    return (
        <div className="admin-reviews">
            <h2 className="admin-reviews__title">Отзывы о сайте</h2>
            <div className="admin-reviews__list">
                <div className="admin-reviews__item">
                    <div className="admin-reviews__item-content">
                        <div className="admin-reviews__item-row">
                            <span className="admin-reviews__item-name">Алина</span>
                            <div className="admin-reviews__item-stars">
                                <img src="/img/rating-star.svg" alt="" />
                                <img src="/img/rating-star.svg" alt="" />
                                <img src="/img/rating-star.svg" alt="" />
                                <img src="/img/rating-star-empty.svg" alt="" />
                                <img src="/img/rating-star-empty.svg" alt="" />
                            </div>
                            <span className="admin-reviews__item-time">30.12.2024 11:20</span>
                        </div>
                        <p className="admin-reviews__item-text">
                            Обожаю заказывать подарки на этом сайте. Всегда самые лучшие подборки подарков! Быстрая доставка в нужное время
                            это отдельный плюс, а еще приятный и понятный дизайн. На каждый праздник сюда возвращаюсь))
                        </p>
                    </div>
                    <div className="admin-reviews__row">
                        <button className="admin-reviews__button admin-reviews__button_accept">Принять</button>
                        <button className="admin-reviews__button admin-reviews__button_reject">Отклонить</button>
                    </div>
                </div>
                <div className="admin-reviews__item">
                    <div className="admin-reviews__item-content">
                        <div className="admin-reviews__item-row">
                            <span className="admin-reviews__item-name">Максим</span>
                            <div className="admin-reviews__item-stars">
                                <img src="/img/rating-star.svg" alt="" />
                                <img src="/img/rating-star.svg" alt="" />
                                <img src="/img/rating-star.svg" alt="" />
                                <img src="/img/rating-star.svg" alt="" />
                                <img src="/img/rating-star-empty.svg" alt="" />
                            </div>
                            <span className="admin-reviews__item-time">30.12.2024 11:20</span>
                        </div>
                        <p className="admin-reviews__item-text">Хороший сайт, помогает выбрать подарок</p>
                    </div>
                    <div className="admin-reviews__row">
                        <button className="admin-reviews__button admin-reviews__button_accept">Принять</button>
                        <button className="admin-reviews__button admin-reviews__button_reject">Отклонить</button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default AdminReviews;
