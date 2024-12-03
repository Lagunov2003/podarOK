import React, { useEffect, useState } from "react";
import "./style.scss";
import BasketItem from "../basket-item";
import { Link } from "react-router-dom";

function WrapperBasket({ list }) {
    const [selectItem, setSelectItem] = useState([...list]);
    const [priceAll, setPriceAll] = useState(0);

    const handleSelectAll = () => {
        setSelectItem([...list]);
    };

    useEffect(() => {
        setPriceAll(selectItem.reduce((acc, v) => (acc += v.price * v.count), 0));
    }, [selectItem]);

    return (
        <section className="basket-page">
            <div className="basket-page__content">
                <div className="basket-page__top">
                    <h1 className="basket-page__title">Корзина</h1>
                </div>
                {list.length != 0 ? (
                    <div className="basket-page__main">
                        <div className="basket-page__wrapper-left">
                            <div className="basket-page__list">
                                {list.map((v, i) => (
                                    <BasketItem itemValue={v} key={i} setSelectItem={setSelectItem} selectItem={selectItem} />
                                ))}
                            </div>
                            <p className="basket-page__label">
                                Если у вас есть промокод, вы можете применить его во время оформления заказа
                            </p>
                        </div>
                        <div className="basket-page__info">
                            {selectItem.length == 0 ? (
                                <>
                                    <p className="basket-page__info-text">Выберите товары, чтобы продолжить</p>
                                    <button className="basket-page__info-button" onClick={() => handleSelectAll()}>
                                        Выбрать всё
                                    </button>
                                </>
                            ) : (
                                <>
                                    <h3 className="basket-page__info-title">Подтверждение товаров</h3>
                                    <div className="basket-page__info-list">
                                        {selectItem.map((_, i) => (
                                            <div className="basket-page__info-item" key={i}></div>
                                        ))}
                                    </div>
                                    <div className="basket-page__info-price">
                                        <span>Итого</span>
                                        <span>
                                            {[...priceAll.toString().slice("")]
                                                .reverse()
                                                .map((v, i) => ((i + 1) % 3 == 0 ? " " + v : v))
                                                .reverse()
                                                .join("")}{" "}
                                            ₽
                                        </span>
                                    </div>
                                    <p className="basket-page__info-label">Цена без учета доставки и промокодов</p>
                                    <Link to={"/order/123"} className="basket-page__info-button">К оформлению</Link>
                                </>
                            )}
                        </div>
                    </div>
                ) : (
                    <div className="basket-page__empty">
                        <h2 className="basket-page__empty-title">В корзине ничего нет</h2>
                        <p className="basket-page__empty-text">Вы можете пройти опрос или начать свои покупки в каталоге</p>
                    </div>
                )}
            </div>
        </section>
    );
}

export default WrapperBasket;
