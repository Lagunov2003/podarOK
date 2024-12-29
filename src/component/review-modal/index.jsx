import React, { useRef, useState } from "react";
import "./style.scss";
import { responsePostAddSiteReviews } from "../../tool/response";

const arr = new Array(5).fill("");

function ReviewModal({ handleOpen }) {
    const refTextarea = useRef(null);
    const [rate, setRate] = useState(0);
    const refRate = useRef(0);

    const handleSend = () => {
        (async () => {
            const status = await responsePostAddSiteReviews(refRate.current, refTextarea.current.value);

            if(status == 200) {
                alert("Отзыв о сайте отправлен на модерацию!")
            }

            refTextarea.current.value = "";
            refRate.current = 0;
            handleOpen();
        })();
    };

    const handleClose = () => {
        refTextarea.current.value = "";
        refRate.current = 0;
        handleOpen();
    }

    return (
        <div className="review-modal">
            <h3 className="review-modal__title">Напишите ваше мнение о нашем сервисе!</h3>
            <div className="review-modal__row">
                <p className="review-modal__label">Ваша оценка</p>
                <div className="review-modal__stars-row" onMouseLeave={() => setRate(0)}>
                    {arr.map((v, i) =>
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
            </div>
            <textarea
                name=""
                id=""
                className="review-modal__textarea"
                ref={refTextarea}
                placeholder="Расскажите о своем опыте использования нашего сайта"
            />
            <button className="review-modal__send" onClick={() => handleSend()}>
                Отправить
            </button>
            <button className="review-modal__close" onClick={() => handleClose()}>
                <svg width="23.000000" height="23.000000" viewBox="0 0 23 23" fill="none">
                    <defs />
                    <path
                        id="cancle-color"
                        d="M13.52 11.49L22.57 2.46C22.84 2.19 22.99 1.82 22.99 1.44C22.99 1.05 22.84 0.69 22.57 0.42C22.3 0.15 21.93 0 21.55 0C21.17 0 20.8 0.15 20.53 0.42L11.5 9.47L2.46 0.42C2.19 0.15 1.82 0 1.44 0C1.06 0 0.69 0.15 0.42 0.42C0.15 0.69 0 1.05 0 1.44C0 1.82 0.15 2.19 0.42 2.46L9.47 11.49L0.42 20.53C0.29 20.66 0.18 20.82 0.11 21C0.03 21.17 0 21.36 0 21.55C0 21.74 0.03 21.93 0.11 22.1C0.18 22.28 0.29 22.44 0.42 22.57C0.55 22.7 0.71 22.81 0.89 22.88C1.06 22.96 1.25 23 1.44 23C1.63 23 1.82 22.96 1.99 22.88C2.17 22.81 2.33 22.7 2.46 22.57L11.5 13.52L20.53 22.57C20.66 22.7 20.82 22.81 21 22.88C21.17 22.96 21.36 23 21.55 23C21.74 23 21.93 22.96 22.1 22.88C22.28 22.81 22.44 22.7 22.57 22.57C22.7 22.44 22.81 22.28 22.88 22.1C22.96 21.93 23 21.74 23 21.55C23 21.36 22.96 21.17 22.88 21C22.81 20.82 22.7 20.66 22.57 20.53L13.52 11.49Z"
                        fill="#000000"
                    />
                </svg>
            </button>
        </div>
    );
}

export default ReviewModal;
