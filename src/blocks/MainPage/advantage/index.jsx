import React, { useState } from "react";
import "./style.scss";

function Advantage() {

    return (
        <section className="advantage">
            <div className="advantage__content padding-style">
                <h2 className="advantage__title title-style">Почему выбирают нас?</h2>
                <div className="advantage__list">
                    <div className="advantage__item">
                        <h3 className="advantage__item-title">Индивидуальный подход к каждому покупателю</h3>
                        <p className="advantage__item-text">
                            Наш чат поддержки работает постоянно, поэтому мы всегда стараемся отнестись с заботой и вниманием к каждому
                            пользователю. Всегда поможем и ответим на все вопросы, обращайтесь!
                        </p>
                    </div>
                    <div className="advantage__item">
                        <h3 className="advantage__item-title">Подберем для вас самый классный подарок</h3>
                        <p className="advantage__item-text">
                            Благодаря нашему опросу вы сможете найти лучшие варианты для подарков своим близким. Найдутся подарки по любому
                            поводу и без!
                        </p>
                    </div>
                    <div className="advantage__item">
                        <h3 className="advantage__item-title">Доставка в любое время и место</h3>
                        <p className="advantage__item-text">
                            Вы можете указать куда и во-сколько ожидаете доставку подарка, а мы привезем точно в срок и в целости и
                            сохранности!
                        </p>
                    </div>
                </div>
                <div className="advantage__decor-cards">
                    <img src="/img/heart-and-cards.png" alt="Декоративный элемент" />
                </div>
                <div className="advantage__decor-hearts">
                    <img src="/img/bg-hearts.png" alt="Декоративный элемент" />
                </div>
            </div>
        </section>
    );
}

export default Advantage;
