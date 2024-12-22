import React, { useEffect, useRef, useState } from "react";
import "./style.scss";
import { responsePostAddGiftReview } from "../../../tool/response";
import { useParams } from "react-router";

const arr = new Array(5).fill("");

function FeedbackItem() {
    const { id } = useParams();
    const [tokenCheck, setTokenCheck] = useState(false);
    const [rate, setRate] = useState(0);
    const [text, setText] = useState("");
    const refRate = useRef(0);

    useEffect(() => {
        const token = localStorage.getItem("token");

        if (token) {
            setTokenCheck(true);
        }
    }, []);

    const handleSendReview = async () => {
        if (text != "" && refRate.current != 0) {
            await responsePostAddGiftReview(text, refRate.current, id);
            location.reload();
        }
    };

    return (
        <section className="feedback-item">
            <div className="feedback-item__content">
                <h2 className="feedback-item__title">Оставьте свой отзыв</h2>
                {tokenCheck ? (
                    <div className="feedback-item__wrapper">
                        <div className="feedback-item__row" onMouseLeave={() => setRate(0)}>
                            {arr.map((_, i) =>
                                rate > i || (refRate.current > i && rate == 0) ? (
                                    <img
                                        src="/img/rating-star.svg"
                                        alt="Звезда рейтинга"
                                        key={i}
                                        onMouseMove={() => setRate(i + 1)}
                                        onClick={() => (refRate.current = i + 1)}
                                    />
                                ) : (
                                    <img
                                        src="/img/rating-star-empty.svg"
                                        alt="Звезда рейтинга"
                                        key={i}
                                        onMouseMove={() => setRate(i + 1)}
                                        onClick={() => (refRate.current = i + 1)}
                                    />
                                )
                            )}
                        </div>
                        <div className="feedback-item__form">
                            <textarea
                                name=""
                                id=""
                                placeholder="Напишите ваше мнение о товаре и доставке"
                                value={text}
                                onChange={(e) => setText(e.target.value)}
                            ></textarea>
                            <button className="feedback-item__send button-style" onClick={() => handleSendReview()}>
                                Отправить
                            </button>
                        </div>
                    </div>
                ) : (
                    <p className="feedback-item__label">Чтобы оставить свой отзыв о заказе необходимо зайти в личный кабинет</p>
                )}
            </div>
        </section>
    );
}

export default FeedbackItem;
