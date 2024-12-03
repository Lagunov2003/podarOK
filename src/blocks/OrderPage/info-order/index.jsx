import React from "react";
import "./style.scss";
import Dropdown from "../../../component/dropdown";

const list = ["Любое время", "С 8:00 до 11:00", "С 11:00 до 14:00", "С 14:00 до 17:00", "С 17:00 до 20:00", "С 20:00 до 23:00"];

function InfoOrder() {
    return (
        <div className="info-order">
            <div className="info-order__content">
                <div className="info-order__recipient info-order__block">
                    <div className="info-order__recipient-row">
                        <h2 className="info-order__title">Получатель</h2>
                        <button className="info-order__recipient-edit">
                            <img src={"/img/pencil.svg"} alt="" />
                        </button>
                    </div>
                    <ul className="info-order__recipient-list">
                        <li className="info-order__recipient-item">
                            <p className="info-order__text info-order__recipient-label">Имя получателя:</p>
                            <p className="info-order__text">Имя Фамилия</p>
                        </li>
                        <li className="info-order__recipient-item">
                            <p className="info-order__text info-order__recipient-label">Почта:</p>
                            <p className="info-order__text">pochta@gmail.com</p>
                        </li>
                        <li className="info-order__recipient-item">
                            <p className="info-order__text info-order__recipient-label">Телефон:</p>
                            <p className="info-order__text">+79012345678</p>
                        </li>
                    </ul>
                </div>
                <div className="info-order__delivery info-order__block">
                    <h2 className="info-order__title">Доставка</h2>
                    <div className="info-order__delivery-row">
                        <p className="info-order__text">Выберите дату:</p>
                        <input
                            type="date"
                            className="info-order__delivery-calendary"
                            min={new Date(new Date().getTime() + 24 * 60 * 60 * 1000).toISOString().split("T")[0]}
                        />
                    </div>
                    <div className="info-order__delivery-row">
                        <p className="info-order__text">Укажите адрес:</p>
                        <button className="info-order__delivery-button">Выбрать на карте</button>
                    </div>
                    <div className="info-order__delivery-row">
                        <p className="info-order__text">Выберите время доставки:</p>
                        <div className="info-order__delivery-wrapper">
                            <Dropdown list={list} classBlock={"info-order__delivery-dropdown"} />
                        </div>
                    </div>
                    <label className="info-order__text info-order__delivery-radio">
                        <input type="checkbox" className="info-order__delivery-input" />
                        Срочная доставка
                    </label>
                    <p className="info-order__text info-order__error">
                        В выбранную дату срочная доставка невозможна
                    </p>
                    <p className="info-order__text info-order__delivery-price">Стоимость доставки составит: 9999 ₽</p>
                </div>
                <div className="info-order__pay info-order__block">
                    <h2 className="info-order__title">Способ оплаты</h2>
                    <div className="info-order__pay-row">
                        <div className="info-order__pay-item">
                            <p className="info-order__pay-text">Оплата онлайн</p>
                            <div className="info-order__pay-img">
                                <img src="/img/pay-tel.svg" alt="" />
                            </div>
                        </div>
                        <div className="info-order__pay-item">
                            <p className="info-order__pay-text">Картой курьеру</p>
                            <div className="info-order__pay-img">
                                <img src="/img/pay-card.svg" alt="" />
                            </div>
                        </div>
                        <div className="info-order__pay-item">
                            <p className="info-order__pay-text">Наличными</p>
                            <div className="info-order__pay-img">
                                <img src="/img/cash.svg" alt="" />
                            </div>
                        </div>
                    </div>
                </div>
                <div className="info-order__online info-order__block">
                    <div className="info-order__online-top">
                        <h2 className="info-order__title">Оплата онлайн</h2>
                        <p className="info-order__text">Укажите данные карты</p>
                    </div>
                    <div className="info-order__online-wrapper">
                        <div className="info-order__online-column">
                            <div className="info-order__online-fields">
                                <label className="info-order__online-input">
                                    Номер
                                    <input type="text" />
                                </label>
                                <div className="info-order__online-row">
                                    <label className="info-order__online-input">
                                        Срок
                                        <input type="text" />
                                    </label>
                                    <label className="info-order__online-input">
                                        Код
                                        <input type="text" />
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div className="info-order__online-img">
                            <img src="/img/card-online.svg" alt="" />
                        </div>
                    </div>
                </div>
                <p className="info-order__block info-order__agreement">
                    Оформляя заказ, вы даёте согласие на обработку персональных данных.
                </p>
                <p className="info-order__block info-order__agreement">Для отказа от товара обратитесь в чат поддержки.</p>
            </div>
        </div>
    );
}

export default InfoOrder;
