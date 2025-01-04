import React from "react";
import "./style.scss";
import { convertDate, convertImg, convertPrice } from "../../../tool/tool";
import { Link, useLocation } from "react-router-dom";

const listStatus = ["Оформлен", "Собран", "В пути", "Доставлен"];

function OrderItemUser({ data, handleChangeStatus = () => {}, typeOrder = "current" }) {

    return (
        <div className="order-item-user">
            <div className="order-item-user__info">
                <div className="order-item-user__big-row">
                    <div className="order-item-user__row">
                        <h3 className="order-item-user__title">Получатель</h3>
                        <p className="order-item-user__text">Получатель: {data?.recipientName}</p>
                        <p className="order-item-user__text">Почта: {data?.recipientEmail}</p>
                        <p className="order-item-user__text">Номер телефона: {data?.recipientPhoneNumber}</p>
                    </div>
                    <div className="order-item-user__row">
                        <p className="order-item-user__number">Заказ №{data.id}</p>
                        <p className="order-item-user__price">{convertPrice(data?.orderCost)} руб</p>
                    </div>
                </div>
                <div className="order-item-user__row">
                    <h3 className="order-item-user__title">Сведения о доставке</h3>
                    <p className="order-item-user__text">Получение товара ожидается {convertDate(data.deliveryDate)}</p>
                    <p className="order-item-user__text">Доставка по адресу {data?.information}</p>
                </div>
                {typeOrder == "current" && (
                    <div className="order-item-user__row">
                        <h3 className="order-item-user__title">Состояние доставки</h3>
                        <p className="order-item-user__text">Мы пришлем уведомление об изменении состояния товара</p>
                        <div className="order-item-user__status">
                            {listStatus.map((v, i) => (
                                <p
                                    className={
                                        listStatus.indexOf(data?.status) >= i
                                            ? "order-item-user__state order-item-user__state_active"
                                            : "order-item-user__state"
                                    }
                                    key={i}
                                    onClick={() => handleChangeStatus(v)}
                                >
                                    <span>{i + 1}</span> {v}
                                </p>
                            ))}
                        </div>
                    </div>
                )}
            </div>
            <div className="order-item-user__list">
                {data?.gifts.map((v) => (
                    <div className="order-item-user__item" key={v.id}>
                        <div className="order-item-user__item-img">
                            <img src={convertImg(v.photoUrl)} alt="" />
                        </div>
                        <div className="order-item-user__item-info">
                            <h3 className="order-item-user__item-name">{v.name}</h3>
                            <div className="order-item-user__item-price">Цена: {v.price} руб</div>
                            <Link to={"/article/" + v.id} className="order-item-user__item-button button-style">
                                Страница товара
                            </Link>
                        </div>
                    </div>
                ))}
            </div>
            <p className="order-item-user__feedback">Отзыв об этом товаре вы можете оставить на странице товара</p>
        </div>
    );
}

export default OrderItemUser;
