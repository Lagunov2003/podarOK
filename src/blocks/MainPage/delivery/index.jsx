import React, { useEffect, useState } from "react";
import "./style.scss";
import Dropdown from "../../../component/dropdown";

const list = ["Любое время", "С 8:00 до 11:00", "С 11:00 до 14:00", "С 14:00 до 17:00", "С 17:00 до 20:00", "С 20:00 до 23:00"];

function Delivery({ refDelivery }) {
    const [price, setPrice] = useState(100);
    const [distance, setDistance] = useState(1);
    const [, setTime] = useState();
    const [fast, setFast] = useState(false);

    useEffect(() => {
        const pri = 100 * distance + (fast ? 250 : 0);
        setPrice(pri);
    }, [distance, fast]);


    return (
        <section className="delivery" ref={refDelivery}>
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
                                <input
                                    type="radio"
                                    className="delivery__col-type"
                                    name="type"
                                    defaultChecked
                                    onChange={() => setDistance(1)}
                                />
                                Доставка в пределах города
                            </label>
                            <label className="delivery__col-radio">
                                <input type="radio" className="delivery__col-type" name="type" onChange={() => setDistance(2)} />
                                Доставка за город (не более 10 км)
                            </label>
                            <label className="delivery__col-radio">
                                <input type="radio" className="delivery__col-type" name="type" onChange={() => setDistance(3)} />
                                Доставка за город (более 10 км)
                            </label>
                        </div>
                        <div className="delivery__col">
                            <h3 className="delivery__col-title">3. Выберите время доставки*</h3>
                            <div className="delivery__col-wrapper">
                                <Dropdown
                                    list={list}
                                    classBlock={fast ? "delivery__dropdown dropdown_inactive" : "delivery__dropdown"}
                                    setData={setTime}
                                    inactive={fast}
                                />
                            </div>
                            <label className="delivery__col-radio">
                                <input type="checkbox" className="delivery__col-fast" name="fast" onChange={() => setFast((v) => !v)} />
                                Срочная доставка
                            </label>
                        </div>
                    </div>
                    <div className="delivery__price-row">
                        Цена за доставку составит: <span>{price} ₽</span>
                    </div>
                    <div className="delivery__decor-arrow-left">
                        <img src="/img/red-arrow-right.svg" alt="Декоративный элемент" />
                    </div>
                    <div className="delivery__decor-arrow-right">
                        <img src="/img/red-arrow-right.svg" alt="Декоративный элемент" />
                    </div>
                </div>
            </div>
        </section>
    );
}

export default Delivery;
