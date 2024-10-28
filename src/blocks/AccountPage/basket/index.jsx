import React, { useState } from "react";
import "./style.scss";

const list = [
    {
        name: "Футболка с принтом",
        price: "2100",
        number: "3498",
        date: "20.01.2025",
    },
    {
        name: "Футболка с принтом",
        price: "2100",
        number: "3498",
        date: "20.01.2025",
    },
    {
        name: "Футболка с принтом",
        price: "2100",
        number: "3498",
        date: "20.01.2025",
    },
];

const textView = [
    {
        button: "Перейти к прошлым заказам",
        title: "Актуальные заказы",
        empty: "У вас ещё нет незавершенных заказов",
    },
    {
        button: "Перейти к актуальным заказам",
        title: "Прошлые заказы",
        empty: "У вас ещё нет завершенных заказов",
    },
];

function Basket() {
    const [typeView, setTypeView] = useState(0);
    const [openItem, setOpenItem] = useState(false);
    const [indexItem, setIndexItem] = useState(-1);

    const handleChangeTypeView = () => {
        setTypeView((v) => (v == 0 ? 1 : 0));
    };

    const handleClickItem = (i) => {
        setIndexItem(i);
        setOpenItem(true);
    };

    return (
        <div className="basket">
            <h2 className="basket__title">{openItem == false ? textView[typeView].title : "Информация о заказе"}</h2>
            {openItem == false ? (
                <>
                    <button className="basket__button" onClick={() => handleChangeTypeView()}>
                        {textView[typeView].button}
                    </button>
                    {list.length != 0 ? (
                        <div className="basket__list">
                            {list.map((v, i) =>
                                typeView == 0 ? (
                                    <div className="basket__item" onClick={() => handleClickItem(i)}>
                                        <div className="basket__item-img">
                                            <img src="" alt="" />
                                        </div>
                                        <div className="basket__item-info">
                                            <p className="basket__item-number">Заказ № {v.number}</p>
                                            <p className="basket__item-name-price">
                                                {v.name}
                                                <span>{v.price} руб.</span>
                                            </p>
                                            <p className="basket__item-date">Ожидается получение: {v.date}</p>
                                        </div>
                                    </div>
                                ) : (
                                    <div className="basket__item" onClick={() => handleClickItem(i)}>
                                        <div className="basket__item-img">
                                            <img src="" alt="" />
                                        </div>
                                        <div className="basket__item-info">
                                            <p className="basket__item-number">Заказ № {v.number}</p>
                                            <p className="basket__item-name-price">
                                                {v.name}
                                                <span>{v.price} руб.</span>
                                            </p>
                                            <p className="basket__item-date">Получен: {v.date}</p>
                                        </div>
                                    </div>
                                )
                            )}
                        </div>
                    ) : (
                        <p className="basket__empty">{textView[typeView].empty}</p>
                    )}
                </>
            ) : (
                <>
                    {typeView == 0 ? (
                        <div className="basket__article">
                            <div className="basket__article-top">
                                <div className="basket__article-img">
                                    <img src="" alt="" />
                                </div>
                                <div className="basket__article-property">
                                    <h3 className="basket__article-property-title">Характеристики:</h3>
                                    <div className="basket__article-property-list">
                                        <p className="basket__article-property-item">Название: Комплект постельного белья</p>
                                        <p className="basket__article-property-item">Размер: 2-спальный комплект</p>
                                    </div>
                                    <div className="basket__article-price">
                                        <span>Цена:</span>
                                        1750 рублей
                                    </div>
                                </div>
                            </div>
                            <button className="basket__article-button-catalog button-style">Информация о товаре</button>
                            <div className="basket__article-info">
                                <div className="basket__article-info-item">
                                    <h3 className="basket__article-info-item-title">Дата получения</h3>
                                    <p className="basket__article-info-item-text">Получение товара ожидается 20.01.2025</p>
                                </div>
                                <div className="basket__article-info-item">
                                    <h3 className="basket__article-info-item-title">Сведения о доставке</h3>
                                    <p className="basket__article-info-item-text">
                                        Доставка по адресу г. Ярославль, ул. Сахарова д. 2, кв. 3
                                    </p>
                                </div>
                                <div className="basket__article-info-item">
                                    <h3 className="basket__article-info-item-title">Оставить отзыв</h3>
                                    <p className="basket__article-info-item-text">
                                        Написать отзыв о товаре и доставке можно будет после получения заказа
                                    </p>
                                </div>
                            </div>
                        </div>
                    ) : (
                        <div className="basket__article">
                            <div className="basket__article-top">
                                <div className="basket__article-img">
                                    <img src="" alt="" />
                                </div>
                                <div className="basket__article-property">
                                    <h3 className="basket__article-property-title">Характеристики:</h3>
                                    <div className="basket__article-property-list">
                                        <p className="basket__article-property-item">Название: Комплект постельного белья</p>
                                        <p className="basket__article-property-item">Размер: 2-спальный комплект</p>
                                    </div>
                                    <div className="basket__article-price">
                                        <span>Цена:</span>
                                        1750 рублей
                                    </div>
                                </div>
                            </div>
                            <button className="basket__article-button-catalog button-style">Информация о товаре</button>
                            <div className="basket__article-info">
                                <div className="basket__article-info-item">
                                    <h3 className="basket__article-info-item-title">Дата получения</h3>
                                    <p className="basket__article-info-item-text">Получение товара ожидается 20.01.2025</p>
                                </div>
                                <div className="basket__article-info-item">
                                    <h3 className="basket__article-info-item-title">Сведения о доставке</h3>
                                    <p className="basket__article-info-item-text">
                                        Доставка по адресу г. Ярославль, ул. Сахарова д. 2, кв. 3
                                    </p>
                                </div>
                                <div className="basket__article-info-item">
                                    <div className="basket__article-info-item-row">
                                        <h3 className="basket__article-info-item-title">Оставить отзыв</h3>
                                        <div className="basket__article-info-item-row-star">
                                            <div className="basket__article-info-item-star">
                                                <img src="/img/account/star.svg" alt="" />
                                            </div>
                                            <div className="basket__article-info-item-star">
                                                <img src="/img/account/star.svg" alt="" />
                                            </div>
                                            <div className="basket__article-info-item-star">
                                                <img src="/img/account/star.svg" alt="" />
                                            </div>
                                            <div className="basket__article-info-item-star">
                                                <img src="/img/account/star.svg" alt="" />
                                            </div>
                                            <div className="basket__article-info-item-star">
                                                <img src="/img/account/star.svg" alt="" />
                                            </div>
                                        </div>
                                    </div>
                                    <form action="" className="basket__article-info-form">
                                        <textarea name="" id="" placeholder="Напишите ваше мнение о товаре и доставке"></textarea>
                                    </form>
                                </div>
                            </div>
                        </div>
                    )}
                </>
            )}
        </div>
    );
}

export default Basket;
