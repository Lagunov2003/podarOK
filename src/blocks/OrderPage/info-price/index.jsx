import React, { useEffect, useState } from "react";
import "./style.scss";
import { convertImg, convertPrice } from "../../../tool/tool";

function InfoPrice({ dataOrder, handleOrderRegistration, setDataOrder, order }) {
    const [code, setCode] = useState("");
    const [activeCode, setActiveCode] = useState("");
    const [discount, setDiscount] = useState(0);

    const handleCheck = () => {
        if (code === "CODE10") {
            setDiscount((dataOrder.price / 100) * 10);
            setActiveCode("true");
        } else {
            setActiveCode("false");
        }
    };

    const handleChangeCode = (value) => {
        setCode(value);
        setActiveCode("");
    };

    useEffect(() => {
        if (activeCode === "true") setDiscount(((dataOrder.price + dataOrder.addressPrice) / 100) * 10);
    }, [dataOrder, activeCode]);

    useEffect(() => {
        if (activeCode === "true") setDataOrder((v) => ({ ...v, discount: discount }));
    }, [discount, activeCode, setDataOrder]);

    return (
        <div className="info-price">
            <div className="info-price__list">
                {order?.gift?.map((v) => (
                    <div className="info-price__item" key={v?.gift?.id}>
                        <img src={convertImg(v?.gift?.photoUrl)} alt="Фотография товара" />
                        <span className="info-price__item-count">{v?.itemCount}</span>
                    </div>
                ))}
            </div>
            <div className="info-price__line"></div>
            <div className="info-price__center">
                <div className="info-price__row">
                    <p className="info-price__text">Промокод</p>
                    <div className="info-price__wrapper-input">
                        <input
                            name="code"
                            type="text"
                            className={"info-price__code " + (activeCode !== "" ? `info-price__code_${activeCode}` : "")}
                            value={code}
                            maxLength={10}
                            onChange={(e) => handleChangeCode(e.target.value)}
                            autoComplete="off"
                        />
                        {activeCode === "" && (
                            <button className="info-price__check" onClick={() => handleCheck()}>
                                <img src="/img/gray-arrow.svg" alt="Проверка промокода" />
                            </button>
                        )}
                    </div>
                </div>
                <div className="info-price__row">
                    <p className="info-price__text">Доставка</p>
                    <p className="info-price__text">{convertPrice(dataOrder.addressPrice)} ₽</p>
                </div>
                {activeCode === "true" && (
                    <div className="info-price__row">
                        <p className="info-price__text">Скидка</p>
                        <p className="info-price__text">- {convertPrice(dataOrder.discount)} ₽</p>
                    </div>
                )}
            </div>
            <div className="info-price__row">
                <p className="info-price__text-bold">Итого</p>
                <p className="info-price__text-bold">{convertPrice(dataOrder.price - dataOrder.discount + dataOrder.addressPrice)} ₽</p>
            </div>
            <button className="info-price__button" onClick={() => handleOrderRegistration()}>
                Оформить заказ
            </button>
        </div>
    );
}

export default InfoPrice;
