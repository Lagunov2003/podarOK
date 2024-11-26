import React from "react";
import "./style.scss";

function FeedbackItem() {
    return (
        <section className="feedback-item">
            <div className="feedback-item__content">
                <h2 className="feedback-item__title">Оставьте свой отзыв</h2>
                <div className="feedback-item__wrapper">
                    <div className="feedback-item__row">
                        <img src="/img/rating-star.svg" alt="" />
                        <img src="/img/rating-star.svg" alt="" />
                        <img src="/img/rating-star.svg" alt="" />
                        <img src="/img/rating-star-empty.svg" alt="" />
                        <img src="/img/rating-star-empty.svg" alt="" />
                    </div>
                    <form action="" className="feedback-item__form">
                        <textarea name="" id="" placeholder="Напишите ваше мнение о товаре и доставке"></textarea>
                        <button className="feedback-item__send button-style">Отправить</button>
                    </form>
                </div>
            </div>
        </section>
    );
}

export default FeedbackItem;
