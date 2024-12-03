import React, { useState } from "react";
import "./style.scss";

function InfoPrice() {
    const [code, setCode] = useState("");
    const [activeCode, setActiveCode] = useState("");

    const handleCheck = () => {
        if (code == "CODE10") {
            setActiveCode("true");
        } else {
            setActiveCode("false");
        }
    };

    const handleChangeCode = (value) => {
        setCode(value);
        setActiveCode("");
    };

    return (
        <div className="info-price">
            <div className="info-price__list">
                <div className="info-price__item">
                    <span className="info-price__item-count">2</span>
                </div>
                <div className="info-price__item"></div>
            </div>
            <div className="info-price__line"></div>
            <div className="info-price__center">
                <div className="info-price__row">
                    <p className="info-price__text">Промокод</p>
                    <div className="info-price__wrapper-input">
                        <input
                            type="text"
                            className={"info-price__code " + (activeCode != "" ? `info-price__code_${activeCode}` : "")}
                            value={code}
                            maxLength={10}
                            onChange={(e) => handleChangeCode(e.target.value)}
                        />
                        {activeCode == "" && (
                            <button className="info-price__check" onClick={() => handleCheck()}>
                                <img src="/img/gray-arrow.svg" alt="" />
                            </button>
                        )}
                    </div>
                </div>
                <div className="info-price__row">
                    <p className="info-price__text">Доставка</p>
                    <p className="info-price__text">9 999 ₽</p>
                </div>
                {activeCode == "true" && (
                    <div className="info-price__row">
                        <p className="info-price__text">Скидка</p>
                        <p className="info-price__text">6 199 ₽</p>
                    </div>
                )}
            </div>
            <div className="info-price__row">
                <p className="info-price__text-bold">Итого</p>
                <p className="info-price__text-bold">22 396 ₽</p>
            </div>
            <button className="info-price__button">Оформить заказ</button>
        </div>
    );
}

export default InfoPrice;
