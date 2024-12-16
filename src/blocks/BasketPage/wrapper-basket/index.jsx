import React, { useEffect, useState } from "react";
import "./style.scss";
import BasketItem from "../basket-item";
import { Link, useNavigate } from "react-router-dom";
import { convertImg, convertPrice } from "../../../tool/tool";

function WrapperBasket({ list, setList, setOrder }) {
    const navigate = useNavigate()
    const [selectItem, setSelectItem] = useState([...list]);
    const [priceAll, setPriceAll] = useState(0);

    
    useEffect(() => {
        setPriceAll(selectItem.reduce((acc, v) => (acc += v?.gift.price * v.itemCount), 0));
    }, [selectItem]);

    const handleSelectAll = () => {
        setSelectItem([...list]);
    };

    const handleCreateOrder = () => {
        setOrder({gift: [...selectItem], price: priceAll})
        navigate("/order/123")
    }

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
                                {list.map((v) => (
                                    <BasketItem
                                        itemValue={v}
                                        key={v?.gift?.id}
                                        setList={setList}
                                        setSelectItem={setSelectItem}
                                        selectItem={selectItem}
                                    />
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
                                        {selectItem.map((v, i) => (
                                            <div className="basket-page__info-item" key={i}>
                                                <img src={convertImg(v?.gift?.photoUrl)} alt="Картинка товара" />
                                                <span className="basket-page__info-count">{v.itemCount}</span>
                                            </div>
                                        ))}
                                    </div>
                                    <div className="basket-page__info-price">
                                        <span>Итого</span>
                                        <span>{convertPrice(priceAll)} ₽</span>
                                    </div>
                                    <p className="basket-page__info-label">Цена без учета доставки и промокодов</p>
                                    <button className="basket-page__info-button" onClick={() => handleCreateOrder()}>
                                        К оформлению
                                    </button>
                                </>
                            )}
                        </div>
                    </div>
                ) : (
                    <div className="basket-page__empty">
                        <h2 className="basket-page__empty-title">В корзине ничего нет</h2>
                        <p className="basket-page__empty-text">Вы можете пройти опрос или начать свои покупки в каталоге</p>
                        <Link to={"/catalog"} className="basket-page__button-catalog">
                            В каталог
                        </Link>
                    </div>
                )}
            </div>
        </section>
    );
}

export default WrapperBasket;
