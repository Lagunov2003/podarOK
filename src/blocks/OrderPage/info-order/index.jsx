import React, { useEffect, useState } from "react";
import "./style.scss";
import Dropdown from "../../../component/dropdown";
import { convertPrice } from "../../../tool/tool";

const list = ["Любое время", "С 8:00 до 11:00", "С 11:00 до 14:00", "С 14:00 до 17:00", "С 17:00 до 20:00", "С 20:00 до 23:00"];

function InfoOrder({ openModalRecipient, openModalAddress, acceptAddress, dataOrder, setDataOrder }) {
    const [typePay, setTypePay] = useState("card");
    const [time, setTime] = useState("Любое время");
    const [fast, setFast] = useState(false);

    const handleClickPay = (tPay) => {
        setTypePay(tPay);
        setDataOrder(v => ({...v, typePay: tPay}))
    };

    const handleChangeData = (e) => {
        setDataOrder((v) => ({ ...v, deliveryDate: e.target.value }));
    };

    useEffect(() => {
        setDataOrder((v) => ({ ...v, time: time }));
    }, [time]);

    useEffect(() => {
        if (fast) {
            setDataOrder((v) => {
                let pr = v.addressPrice;
                if (v.fastDelivery == false) {
                    pr += 250;
                }
                return { ...v, fastDelivery: fast, addressPrice: pr };
            });
        } else {
            setDataOrder((v) => {
                let pr = v.addressPrice;
                if (v.fastDelivery == true) {
                    pr -= 250;
                }
                return { ...v, fastDelivery: fast, addressPrice: pr };
            });
        }
    }, [fast]);

    return (
        <div className="info-order">
            <div className="info-order__content">
                <div className="info-order__recipient info-order__block">
                    <div className="info-order__recipient-row">
                        <h2 className="info-order__title">Получатель</h2>
                        <button className="info-order__recipient-edit" onClick={() => openModalRecipient(true)}>
                            <img src={"/img/pencil.svg"} alt="Изменить данные" />
                        </button>
                    </div>
                    <ul className="info-order__recipient-list">
                        <li className="info-order__recipient-item">
                            <p className="info-order__text info-order__recipient-label">Имя получателя:</p>
                            <p className="info-order__text">{dataOrder.recipient.name == "" ? "*Не указано*" : dataOrder.recipient.name}</p>
                        </li>
                        <li className="info-order__recipient-item">
                            <p className="info-order__text info-order__recipient-label">Почта:</p>
                            <p className="info-order__text">
                                {dataOrder.recipient.email == "" ? "*Не указано*" : dataOrder.recipient.email}
                            </p>
                        </li>
                        <li className="info-order__recipient-item">
                            <p className="info-order__text info-order__recipient-label">Телефон:</p>
                            <p className="info-order__text">{dataOrder.recipient.tel == "" ? "*Не указано*" : dataOrder.recipient.tel}</p>
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
                            defaultValue={new Date(new Date().getTime() + 24 * 60 * 60 * 1000).toISOString().split("T")[0]}
                            onChange={(e) => handleChangeData(e)}
                        />
                    </div>
                    <div className="info-order__delivery-row">
                        <p className="info-order__text">Укажите адрес:</p>
                        <button className="info-order__delivery-button" onClick={() => openModalAddress(true)}>
                            Выбрать на карте
                        </button>
                        {acceptAddress == true && (
                            <span className="info-order__delivery-accept">
                                <img src="/img/accept-green.svg" alt="Знак указанного адреса" />
                            </span>
                        )}
                    </div>
                    <div className="info-order__delivery-row">
                        <p className="info-order__text">Выберите время доставки:</p>
                        <div className="info-order__delivery-wrapper">
                            <Dropdown
                                list={list}
                                classBlock={fast ? "info-order__delivery-dropdown dropdown_inactive" : "info-order__delivery-dropdown"}
                                setData={setTime}
                                inactive={fast}
                            />
                        </div>
                    </div>
                    <label className="info-order__text info-order__delivery-radio">
                        <input type="checkbox" className="info-order__delivery-input" onChange={() => setFast((v) => !v)} />
                        Срочная доставка
                    </label>
                </div>
                <div className="info-order__pay info-order__block">
                    <h2 className="info-order__title">Способ оплаты</h2>
                    <div className="info-order__pay-row">
                        <div
                            className={"info-order__pay-item " + (typePay == "online" ? "info-order__pay-item_select" : "")}
                            onClick={() => handleClickPay("online")}
                        >
                            <p className="info-order__pay-text">Оплата онлайн</p>
                            <div className="info-order__pay-img">
                                <img src="/img/pay-tel.svg" alt="" />
                            </div>
                        </div>
                        <div
                            className={"info-order__pay-item " + (typePay == "card" ? "info-order__pay-item_select" : "")}
                            onClick={() => handleClickPay("card")}
                        >
                            <p className="info-order__pay-text">Картой курьеру</p>
                            <div className="info-order__pay-img">
                                <img src="/img/pay-card.svg" alt="" />
                            </div>
                        </div>
                        <div
                            className={"info-order__pay-item " + (typePay == "cash" ? "info-order__pay-item_select" : "")}
                            onClick={() => handleClickPay("cash")}
                        >
                            <p className="info-order__pay-text">Наличными</p>
                            <div className="info-order__pay-img">
                                <img src="/img/cash.svg" alt="" />
                            </div>
                        </div>
                    </div>
                </div>
                {typePay == "online" && (
                    <div className="info-order__online info-order__block">
                        <div className="info-order__online-top">
                            <h2 className="info-order__title">Оплата онлайн</h2>
                        </div>
                        <div className="info-order__online-wrapper">
                            <div className="info-order__online-column">
                                <div className="info-order__online-fields">
                                    <label className="info-order__online-input">
                                        Номер
                                        <input type="text" maxLength={16} />
                                    </label>
                                    <div className="info-order__online-row">
                                        <label className="info-order__online-input">
                                            Срок
                                            <input type="text" maxLength={5} />
                                        </label>
                                        <label className="info-order__online-input">
                                            Код
                                            <input type="text" maxLength={3} />
                                        </label>
                                    </div>
                                </div>
                                <p className="info-order__text">Укажите данные карты</p>
                            </div>
                            <div className="info-order__online-img">
                                <img src="/img/card-online.svg" alt="Элемент оформления" />
                            </div>
                        </div>
                    </div>
                )}
                {typePay == "cash" && (
                    <div className="info-order__sum info-order__block">
                        <h2 className="info-order__title">Оплата наличными</h2>
                        <div className="info-order__sum-row">
                            <p className="info-order__text">Укажите с какой суммы Вам нужна сдача</p>
                            <input type="text" maxLength={10} />
                        </div>
                    </div>
                )}
                <p className="info-order__block info-order__agreement">
                    Оформляя заказ, вы даёте согласие на обработку персональных данных.
                </p>
                <p className="info-order__block info-order__agreement">Для отказа от товара обратитесь в чат поддержки.</p>
            </div>
        </div>
    );
}

export default InfoOrder;
