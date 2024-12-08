import React from "react";
import "./style.scss";

function Manual() {
    return (
        <section className="manual">
            <div className="manual__content padding-style">
                <div className="manual__list">
                    <div className="manual__item">
                        <span className="manual__item-number">1</span>
                        <p className="manual__item-text">Проходите короткий опрос</p>
                        <div className="manual__item-circle">
                            <span>
                                <img src="/img/accept.svg" alt="Декоративный элемент" />
                            </span>
                            <span>
                                <img src="/img/accept.svg" alt="Декоративный элемент" />
                            </span>
                            <span></span>
                        </div>
                    </div>
                    <div className="manual__item">
                        <p className="manual__item-text">Из предложенных вариантов выбираете подходящий</p>
                        <span className="manual__item-number">2</span>
                    </div>
                    <div className="manual__item">
                        <span className="manual__item-number">3</span>
                        <p className="manual__item-text">Оформляете заказ и доставку</p>
                        <div className="manual__item-bag">
                            <img src="/img/bag-and-stars.svg" alt="Декоративный элемент" />
                        </div>
                    </div>
                    <div className="manual__decor-arrow-top">
                        <img src="/img/arrow-white-1.svg" alt="Декоративный элемент" />
                    </div>
                    <div className="manual__decor-arrow-bottom">
                        <img src="/img/arrow-white-2.svg" alt="Декоративный элемент" />
                    </div>
                </div>
            </div>
        </section>
    );
}

export default Manual;
