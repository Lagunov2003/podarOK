import React from "react";
import "./style.scss";
import Button from "../../../component/button";
import { useNavigate } from "react-router";

function GiftsInCity() {
    const navigate = useNavigate()

    const handleClickItem = (section) => {
        sessionStorage.setItem("section", section)
        navigate("/catalog")
    }

    const handleClickItemSort = (nameSort) => {
        sessionStorage.setItem("nameSort", nameSort)
        navigate("/catalog")
    }


    return (
        <section className="gifts-in-city">
            <div className="gifts-in-city__content padding-style">
                <h2 className="gifts-in-city__title title-style">Вам может понравиться</h2>
                <div className="gifts-in-city__grid">
                    <div className="gifts-in-city__row">
                        <div className="gifts-in-city__item" onClick={() => handleClickItem("Электроника")}>
                            <h3 className="gifts-in-city__item-title">Техника</h3>
                            <div className="gifts-in-city__item-img">
                                <img src="/img/item-1.png" alt="Декоративный элемент" />
                            </div>
                        </div>
                        <div className="gifts-in-city__item" onClick={() => handleClickItem("Аксессуары")}>
                            <h3 className="gifts-in-city__item-title">Аксессуары</h3>
                            <div className="gifts-in-city__item-img">
                                <img src="/img/item-2.png" alt="Декоративный элемент" />
                            </div>
                        </div>
                        <div className="gifts-in-city__item flex-x2" onClick={() => handleClickItemSort("По рейтингу")}>
                            <h3 className="gifts-in-city__item-title">Популярное</h3>
                            <div className="gifts-in-city__item-img">
                                <img src="/img/item-3.png" alt="Декоративный элемент" />
                            </div>
                        </div>
                    </div>
                    <div className="gifts-in-city__row">
                        <div className="gifts-in-city__item flex-x2" onClick={() => handleClickItemSort("По возрастанию цены")}>
                            <h3 className="gifts-in-city__item-title">Скидки</h3>
                            <div className="gifts-in-city__item-img">
                                <img src="/img/item-4.png" alt="Декоративный элемент" />
                            </div>
                        </div>
                        <div className="gifts-in-city__item" onClick={() => handleClickItem("Новый год")}>
                            <h3 className="gifts-in-city__item-title">Новый год</h3>
                            <div className="gifts-in-city__item-img">
                                <img src="/img/item-5.png" alt="Декоративный элемент" />
                            </div>
                        </div>
                        <div className="gifts-in-city__item" onClick={() => handleClickItem("Товары для дома")}>
                            <h3 className="gifts-in-city__item-title">Товары для дома</h3>
                            <div className="gifts-in-city__item-img">
                                <img src="/img/item-6.png" alt="Декоративный элемент" />
                            </div>
                        </div>
                    </div>
                </div>
                <p className="gifts-in-city__label">Больше вариантов можете увидеть в разделе Каталог </p>
                <Button text="Перейти в каталог" classNameText="gifts-in-city__button" onClick={() => navigate("/catalog")} />
            </div>
        </section>
    );
}

export default GiftsInCity;
