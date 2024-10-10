import React from "react";
import "./style.scss"

function Reviews() {

    return <section className="reviews">
        <div className="reviews__content padding-style">
            <h2 className="reviews__title title-style">Отзывы</h2>
            <div className="reviews__list">
                <div className="reviews__item">
                    <p className="reviews__item-author">Игнат <span>☆☆☆☆☆</span></p>
                    <p className="reviews__item-text">Замечательный подбор подарков, подошло все идеально, друг очень был рад, доставка вовремя!</p>
                </div>
                <div className="reviews__item">
                    <p className="reviews__item-author">Алёна <span>☆☆☆☆</span>☆</p>
                    <p className="reviews__item-text">Прекрасный сервис подбора подарков. Нужно было привезти день в день, было доставлено все в срок. Аккуратно упаковали, бережно довезли до квартиры</p>
                </div>
                <div className="reviews__item">
                    <p className="reviews__item-author">Марат <span>☆☆☆☆☆</span></p>
                    <p className="reviews__item-text">Все супер-пупер, были нюансы по доставке, обращался в чат поддержки, спасибо терпеливому администратору, помог решить все вопросы, <span>ещё...</span></p>
                </div>
            </div>
            <p className="reviews__label">Последние отзывы о нашем сервисе</p>
            <button className="reviews__button button-style">Оставить отзыв</button>
        </div>
    </section>
}

export default Reviews;