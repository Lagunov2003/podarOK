import React, { useEffect, useState } from "react";
import "./style.scss";
import Dropdown from "../../../component/dropdown";

const categorie = [
    "Спорт",
    "Рукоделие",
    "Искусство",
    "Электроника",
    "Охота и рыбалка",
    "Мода",
    "Игрушки",
    "Товары для дома",
    "Красота",
    "Книги",
    "Аксессуары",
    "Путешествия",
    "Музыка",
    "Кулинария",
    "Машины",
];
const holidays = [
    "День Рождения",
    "Новый год",
    "Выпускной",
    "Свадьба",
    "Годовщина",
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

const defaultFilter = {
    price: "",
    categorie: [],
    holidays: [],
    gender: -1,
    age: "",
};

function Filter() {
    const [data, setData] = useState(defaultFilter);
    const [ageVlaue, setAgeValue] = useState()


    useEffect(() => {
        setData(v => ({...v, age: ageVlaue }))
    }, [ageVlaue])

    const handleChangePrice = (e) => {
        const { value } = e.target;

        if (/^[0-9]*$/.test(value)) {
            setData((v) => ({ ...v, price: value }));
        }
    };

    const handleBlurPrice = () => {
        setData((v) => ({ ...v, price: Number(v.price).toString() }));
    };

    const handleChangeCategorie = (e) => {
        const { value } = e.target

        if(data.categorie.indexOf(value) != -1) {
            setData(v => ({...v, categorie: [...v.categorie.filter(v => v != value)]}))
        } else {
            setData(v => ({...v, categorie: [...v.categorie, value]}))
        }
    }

    const handleChangeHolidays = (e) => {
        const { value } = e.target

        if(data.holidays.indexOf(value) != -1) {
            setData(v => ({...v, holidays: [...v.holidays.filter(v => v != value)]}))
        } else {
            setData(v => ({...v, holidays: [...v.holidays, value]}))
        }
    }

    const handleChangeGender = (e) => {
        setData(v => ({...v, gender: e.target.value}))
    }

    return (
        <div className="filter">
            <div className="filter__item">
                <p className="filter__item-title">Категории:</p>
                <div className="filter__item-wrapper">
                    <div className="filter__item-list">
                        {categorie.map((v, i) => (
                            <label className="filter__item-input" key={i}>
                                <input type="checkbox" name="categorieFilter" value={v} onChange={(e) => handleChangeCategorie(e)} /> {v}
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
                                <input type="checkbox" name="holidaysFilter" value={v} onChange={(e) => handleChangeHolidays(e)}/> {v}
                            </label>
                        ))}
                    </div>
                </div>
            </div>
            <div className="filter__item">
                <p className="filter__item-title">Пол:</p>
                <div className="filter__item-gender">
                    <label className="filter__item-input">
                        <input type="radio" name="genderFilter" value={1} onChange={(e) => handleChangeGender(e)}/> Мужской
                    </label>
                    <label className="filter__item-input">
                        <input type="radio" name="genderFilter" value={2} onChange={(e) => handleChangeGender(e)}/> Женский
                    </label>
                </div>
            </div>
            <div className="filter__item">
                <p className="filter__item-title">Бюджет:</p>
                <div className="filter__item-price">
                    <label className="filter__item-input">
                        До{" "}
                        <input
                            type="text"
                            name="priceFilter"
                            maxLength={7}
                            value={data.price}
                            onChange={(e) => handleChangePrice(e)}
                            onBlur={(e) => handleBlurPrice(e)}
                        />{" "}
                        ₽
                    </label>
                </div>
            </div>
            <div className="filter__item">
                <p className="filter__item-title">Возраст:</p>
                <div className="filter__item-wrapper-dropdown">
                    <Dropdown list={age} setData={setAgeValue} classBlock="filter__dropdown" defaultValue="Выбрать" />
                </div>
            </div>
            <button className="button-style filter__button">Подобрать</button>
        </div>
    );
}

export default Filter;
