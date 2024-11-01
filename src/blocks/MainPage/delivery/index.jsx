import React, { useRef, useState } from "react";
import "./style.scss";
import Dropdown from "../../../component/dropdown";

const list = ["С 8:00 до 11:00", "С 11:00 до 14:00", "С 14:00 до 17:00", "С 17:00 до 20:00", "С 20:00 до 23:00", "Любое время"];

function Delivery() {
    return (
        <section className="delivery">
            <div className="delivery__content padding-style">
                <h2 className="delivery__title title-style">Рассчитать доставку</h2>
                <div className="delivery__block">
                    <div className="delivery__list">
                        <div className="delivery__col">
                            <h3 className="delivery__col-title">1. Выберите дату доставки:</h3>
                            <input
                                type="date"
                                className="delivery__col-calendary"
                                min={new Date(new Date().getTime() + 24 * 60 * 60 * 1000).toISOString().split("T")[0]}
                            />
                        </div>
                        <div className="delivery__col">
                            <h3 className="delivery__col-title">2. Укажите расстояние доставки:</h3>
                            <p className="delivery__col-label">*Расстояние влияет на стоимость</p>
                            <label className="delivery__col-radio">
                                <input type="radio" className="delivery__col-type" name="type" />
                                Доставка в пределах города
                            </label>
                            <label className="delivery__col-radio">
                                <input type="radio" className="delivery__col-type" name="type" />
                                Доставка за город (не более 10 км)
                            </label>
                            <label className="delivery__col-radio">
                                <input type="radio" className="delivery__col-type" name="type" />
                                Доставка за город (более 10 км)
                            </label>
                        </div>
                        <div className="delivery__col">
                            <h3 className="delivery__col-title">3. Выберите время доставки*</h3>
                            <div className="delivery__col-wrapper">
                                <Dropdown list={list} classBlock={"delivery__dropdown"} />
                            </div>
                            <label className="delivery__col-radio">
                                <input type="checkbox" className="delivery__col-fast" name="fast" />
                                Срочная доставка
                            </label>
                        </div>
                    </div>
                    <div className="delivery__price-row">
                        Цена за доставку составит: <span>100 ₽</span>
                    </div>
                    <div className="delivery__decor-arrow-left">
                        <img src="/img/red-arrow-right.svg" alt="" />
                    </div>
                    <div className="delivery__decor-arrow-right">
                        <img src="/img/red-arrow-right.svg" alt="" />
                    </div>
                </div>
            </div>
        </section>
    );
}

export default Delivery;
