import React, { useContext, useEffect, useRef, useState } from "react";
import "./style.scss";
import Dropdown from "../../../component/dropdown";
import { ContextSurvey } from "../../../app/app";

const categorie = [
    "Спорт",
    "Рукоделие",
    "Искусство",
    "Электроника",
    "Охота и рыбалка",
    "Аксессуары",
    "Путешествия",
    "Музыка",
    "Кулинария",
    "Машины",
    "Мода",
    "Игрушки",
    "Товары для дома",
    "Красота",
    "Книги",
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
    categories: [],
    occasions: [],
    gender: -1,
    age: "",
};

function Filter({ setFilter }) {
    const { survey, setSurvey } = useContext(ContextSurvey);
    const [data, setData] = useState(defaultFilter);
    const [ageVlaue, setAgeValue] = useState("");

    useEffect(() => {
        const section = sessionStorage.getItem("section");

        if (section) {
            if (section != "Новый год") {
                setData((v) => ({ ...v, categories: [section] }));
                setFilter((v) => ({...v, categories: [categorie.indexOf(section)]}))
            } else {
                setData((v) => ({ ...v, occasions: [section] }));
                setFilter((v) => ({...v, occasions: [holidays.indexOf(section)]}))
            }
            sessionStorage.removeItem("section");
        }

        return () => setSurvey(null)
    }, []);

    useEffect(() => {
        if (survey != null) {
            setData({
                price: survey.price,
                categories: survey.categories,
                occasions: survey.occasions,
                gender: survey.gender,
                age: survey.age,
            });
            setFilter({
                categories: [...survey.categories.map((v) => categorie.indexOf(v) + 1)],
                occasions: [...survey.occasions.map(v => holidays.indexOf(v) + 1)],
                gender: Number(survey.gender),
                price: survey.price,
                age: age.indexOf(survey.age)
            });
        }
    }, [survey]);

    useEffect(() => {
        if (ageVlaue != "") setData((v) => ({ ...v, age: ageVlaue }));
    }, [ageVlaue]);

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
        const { value } = e.target;

        if (data.categories.indexOf(value) != -1) {
            setData((v) => ({ ...v, categories: [...v.categories.filter((v) => v != value)] }));
        } else {
            setData((v) => ({ ...v, categories: [...v.categories, value] }));
        }
    };

    const handleChangeHolidays = (e) => {
        const { value } = e.target;

        if (data.occasions.indexOf(value) != -1) {
            setData((v) => ({ ...v, occasions: [...v.occasions.filter((v) => v != value)] }));
        } else {
            setData((v) => ({ ...v, occasions: [...v.occasions, value] }));
        }
    };

    const handleChangeGender = (e) => {
        setData((v) => ({ ...v, gender: e.target.value }));
    };

    const handleClearFilter = () => {
        setData(defaultFilter);
        setFilter({ price: "", categories: [], occasions: [], gender: -1, age: -1 });
    };

    const handleSearch = () => {
        setFilter({
            categories: [...data.categories.map(v => categorie.indexOf(v) + 1)],
            occasions: [...data.occasions.map(v => holidays.indexOf(v) + 1)],
            gender: Number(data.gender),
            price: data.price,
            age: age.indexOf(data.age)
        });
    }

    return (
        <form className="filter">
            <div className="filter__item">
                <p className="filter__item-title">Категории:</p>
                <div className="filter__item-wrapper">
                    <div className="filter__item-list">
                        {categorie.map((v, i) => (
                            <label className="filter__item-input" key={i}>
                                <input
                                    type="checkbox"
                                    name="categorieFilter"
                                    value={v}
                                    checked={data.categories.indexOf(v) != -1}
                                    onChange={(e) => handleChangeCategorie(e)}
                                />{" "}
                                {v}
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
                                <input
                                    type="checkbox"
                                    name="holidaysFilter"
                                    value={v}
                                    checked={data.occasions.indexOf(v) != -1}
                                    onChange={(e) => handleChangeHolidays(e)}
                                />{" "}
                                {v}
                            </label>
                        ))}
                    </div>
                </div>
            </div>
            <div className="filter__item">
                <p className="filter__item-title">Пол:</p>
                <div className="filter__item-gender">
                    <label className="filter__item-input">
                        <input
                            type="radio"
                            name="genderFilter"
                            value={1}
                            checked={data.gender == 1}
                            onChange={(e) => handleChangeGender(e)}
                        />{" "}
                        Мужской
                    </label>
                    <label className="filter__item-input">
                        <input
                            type="radio"
                            name="genderFilter"
                            value={0}
                            checked={data.gender == 0}
                            onChange={(e) => handleChangeGender(e)}
                        />{" "}
                        Женский
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
                    <Dropdown
                        list={age}
                        setData={setAgeValue}
                        classBlock="filter__dropdown"
                        defaultValue={data.age != "" ? data.age : "Выбрать"}
                    />
                </div>
            </div>
            <button className="button-style filter__button" type="button" onClick={() => handleSearch()}>Подобрать</button>
            <button className="button-style filter__reset" type="reset" onClick={() => handleClearFilter()}>
                Сбросить фильтр
            </button>
        </form>
    );
}

export default Filter;
