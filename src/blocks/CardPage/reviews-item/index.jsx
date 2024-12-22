import React from "react";
import "./style.scss";

const stars = new Array(5).fill(0);

function ReviewsItem({ list }) {

    return (
        <section className="reviews-item">
            <div className="reviews-item__content">
                <h2 className="reviews-item__title">Отзывы</h2>
                <div className="reviews-item__list">
                    {list.map((v, i) => (
                        <div className="reviews-item__item" key={i}>
                            <div className="reviews-item__item-row">
                                <span className="reviews-item__item-name">{v.userName}</span>
                                <div className="reviews-item__item-rating">
                                    <div className="reviews-item__item-star">
                                        {stars.map((_, i) => (
                                            <img key={i} src={v.rating >= i + 1 ? "/img/rating-star.svg" : "/img/rating-star-empty.svg"} alt="Элемент оформления" />
                                        ))}
                                    </div>
                                </div>
                            </div>
                            <p className="reviews-item__item-text">{v.text}</p>
                        </div>
                    ))}
                </div>
            </div>
        </section>
    );
}

export default ReviewsItem;
