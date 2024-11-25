import React, { useState } from "react";
import "./style.scss";

const items = ["Набор 3 шт", "Карамель-попкорн", "Синнабон", "Апельсин-корица"];

function Info() {
    const [typeItem, setTypeItem] = useState(0);

    return (
        <section className="info">
            <div className="info__content">
                <div className="info__top">
                    <div className="info__top-left">
                        <span className="info__top-tag">Новый год</span>
                        <span className="info__top-tag">Товары для дома</span>
                        <span className="info__top-tag">Набор</span>
                    </div>
                    <div className="info__top-right">
                        <span className="info__top-avail info__top-avail_active">Есть в наличии</span>
                        <div className="info__top-favorite">
                            <img src={"/img/favorite.svg"} alt="Добавить в избранное" />
                        </div>
                    </div>
                </div>
                <div className="info__block">
                    <div className="info__block-column">
                        <div className="info__block-wrapper">
                            <div className="info__block-prev"></div>
                            <div className="info__block-scroll">
                                <div className="info__block-img"></div>
                                <div className="info__block-img"></div>
                                <div className="info__block-img"></div>
                            </div>
                            <div className="info__block-next"></div>
                        </div>
                        <div className="info__block-main-img"></div>
                    </div>
                    <div className="info__block-column">
                        <h2 className="info__block-name">Ароматические свечи, подарочный набор для дома 2025 год</h2>
                        <div className="info__block-row">
                            <div className="info__block-rating">
                                <img src="/img/yellow-star.png" alt="Элемент оформления" />
                                <span className="info__block-text">4,7</span>
                            </div>
                            <span className="info__block-comment">6 отзывов</span>
                        </div>
                        <div className="info__block-list">
                            {items.map((v, i) => (
                                <span
                                    className={"info__block-item" + (typeItem == i ? " info__block-item_active" : "")}
                                    key={i}
                                    onClick={() => setTypeItem(i)}
                                >
                                    {v}
                                </span>
                            ))}
                        </div>
                        <h3 className="info__block-title">Коротко о товаре</h3>
                        <div className="info__block-descr">
                            <p className="info__block-text">Цвет: Белый</p>
                            <p className="info__block-text">Аромат: Апельсин; Синнабон; Попкорн</p>
                            <p className="info__block-text">Время горения: 30 ч</p>
                            <p className="info__block-text">Группа аромата: Сладкий аромат</p>
                            <p className="info__block-text">Тип свечи: Ароматическая; кокосовая; декоративная</p>
                            <p className="info__block-text">Страна производства: Россия</p>
                            <p className="info__block-text">Комплектация: Коробка - 1 шт;</p>
                            <p className="info__block-text">Аромасвеча в стеклянной банке 3 штуки</p>
                            <p className="info__block-text">Высота предмета: 5 см</p>
                            <p className="info__block-text">Ширина предмета: 21 см</p>
                        </div>
                    </div>
                    <div className="info__block-column">
                        <div className="info__block-bg">
                            <p className="info__block-price">
                                400 ₽ <span>760 ₽</span>
                            </p>
                            <button className="info__block-add">Добавить в корзину</button>
                            <button className="info__block-buy">Купить</button>
                        </div>
                        <h3 className="info__block-title">Описание товара</h3>
                        <p className="info__block-text-small">
                            Аромасвеча EMERGE Decors набор из 3 штук: Апельсин-корица, Синнабон, Карамельный попкорн в круглой баночке
                            станет украшением интерьера комнаты и заполнит чудесным ароматом весь дом! Для воскового изделия ручной работы
                            используются только высококачественные натуральные компоненты. Основа свечки выполнена из кокосового воска. Для
                            придания сладкого запаха применяют ароматические отдушки из Франции. Погасить её довольно просто - достаточно
                            закрыть крышку. Подставка из стеклянной банки делает свечи абсолютно безопасными для любых поверхностей.
                            Поставьте сладкие ароматные свечи у елки в новый год и наполните праздничный вечер оригинальным домашним
                            волшебством!
                        </p>
                    </div>
                </div>
            </div>
        </section>
    );
}

export default Info;
