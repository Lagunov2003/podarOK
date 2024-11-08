import React from "react";
import "./style.scss";
import Dropdown from "../../../component/dropdown";

const categorie = ["Мода", "Игрушки", "Товары для дома", "Красота", "Книги", "Аксессуары", "Путешествия", "Музыка", "Кулинария", "Машины"];
const holidays = [
    "14 февраля",
    "23 февраля",
    "8 марта",
    "Пасха",
    "1 сентября",
    "Новоселье",
    "День матери/отца",
    "Рождение ребенка",
    "Особенный день",
    "Без повода",
];
const age = ["Для детей", "Подросткам", "18-35 лет", "35-60 лет", "60 и старше"];

function Filter() {
    return (
        <div className="filter">
            <div className="filter__item">
                <p className="filter__item-title">Категории:</p>
                <div className="filter__item-wrapper">
                    <div className="filter__item-list">
                        {categorie.map((v, i) => (
                            <label className="filter__item-input" key={i}>
                                <input type="checkbox" name="" /> {v}
                            </label>
                        ))}
                    </div>
                </div>
            </div>
            <div className="filter__item">
                <p className="filter__item-title">Повод для подарка:</p>
                <div className="filter__item-wrapper">
                    <div className="filter__item-list">
                        {holidays.map((v, i) => (
                            <label className="filter__item-input" key={i}>
                                <input type="radio" name="" /> {v}
                            </label>
                        ))}
                    </div>
                </div>
            </div>
            <div className="filter__item">
                <p className="filter__item-title">Пол:</p>
                <div className="filter__item-gender">
                    <label className="filter__item-input">
                        <input type="radio" name="" /> Мужской
                    </label>
                    <label className="filter__item-input">
                        <input type="radio" name="" /> Женский
                    </label>
                </div>
            </div>
            <div className="filter__item">
                <p className="filter__item-title">Бюджет:</p>
                <div className="filter__item-price">
                    <label className="filter__item-input">
                        До <input type="text" name="" /> ₽
                    </label>
                </div>
            </div>
            <div className="filter__item">
                <p className="filter__item-title">Возраст:</p>
                <div className="filter__item-wrapper-dropdown">
                    <Dropdown list={age} classBlock="filter__dropdown" />
                </div>
            </div>
            <button className="button-style filter__button">Подобрать</button>
        </div>
    );
}

export default Filter;
