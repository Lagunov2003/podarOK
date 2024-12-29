import React, { useContext, useEffect, useRef, useState } from "react";
import "./style.scss";
import Dropdown from "../dropdown";
import { Link, useNavigate } from "react-router-dom";
import WrapperModal from "../wrapper-modal";
import { ContextSurvey } from "../../app/app";

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

function SurveyModal() {
    const navigate = useNavigate();
    const { survey, setSurvey } = useContext(ContextSurvey);
    const [open, setOpen] = useState(false);
    const [gender, setGender] = useState(-1);
    const [ageData, setAgeData] = useState("");
    const [categories, setCategories] = useState([]);
    const [price, setPrice] = useState(0);
    const [occasions, setOccasions] = useState([]);

    const handleOpen = () => {
        document.body.classList.toggle("local-page");
        setOpen((t) => !t);
        setGender(-1);
        setAgeData("");
        setCategories([]);
        setPrice(0);
        setOccasions([]);
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        setOpen(false);
        setSurvey({
            gender: gender,
            age: ageData,
            categories: categories,
            price: price,
            occasions: occasions,
        });
        document.body.classList.remove("local-page");
        setTimeout(() => {
            navigate("/catalog");
        }, 500);
    };

    const handleChangePrice = (e) => {
        const value = e.target.value;

        if (/^[0-9]*$/.test(value)) {
            setPrice(value);
        }
    };

    const handleBlurPrice = () => {
        setPrice((v) => Number(v).toString());
    };

    const handleSelectCategorie = (e) => {
        const arr = [...categories];

        if (arr.indexOf(e.target.value) != -1) {
            setCategories((v) => v.filter((value) => value != e.target.value));
        } else {
            setCategories([...arr, e.target.value]);
        }
    };

    const handleSelectOccasions = (e) => {
        const arr = [...occasions];

        if (arr.indexOf(e.target.value) != -1) {
            setOccasions((v) => v.filter((value) => value != e.target.value));
        } else {
            setOccasions([...arr, e.target.value]);
        }
    };

    return (
        <div className="survey-modal">
            <button className="survey-modal__button" onClick={() => handleOpen()}>
                Пройти опрос
            </button>
            <WrapperModal activeModal={open}>
                <div className="survey-modal__content">
                    <button className="survey-modal__cancle" onClick={() => handleOpen()}>
                        <span></span>
                        <span></span>
                    </button>
                    <h2 className="survey-modal__name">
                        podar<span>OK</span>
                    </h2>
                    <p className="survey-modal__text">
                        Ответьте на несколько вопросов для подбора наилучшего подарка для выбранного вами человечка❤
                    </p>
                    <form className="survey-modal__form" onSubmit={(e) => handleSubmit(e)}>
                        <div className="survey-modal__row">
                            <div className="survey-modal__item">
                                <span className="survey-modal__item-number">{"1)"}</span>
                                <div className="survey-modal__item-content">
                                    <h3 className="survey-modal__text survey-modal__item-title">Пол:</h3>
                                    <div className="survey-modal__item-wrapper">
                                        <label className="survey-modal__text survey-modal__circle">
                                            <input
                                                type="radio"
                                                name="gender"
                                                className="survey-modal__item-input"
                                                checked={gender == 1}
                                                onChange={() => setGender(1)}
                                            />{" "}
                                            Мужской
                                        </label>
                                        <label className="survey-modal__text survey-modal__circle">
                                            <input
                                                type="radio"
                                                name="gender"
                                                className="survey-modal__item-input"
                                                checked={gender == 0}
                                                onChange={() => setGender(0)}
                                            />{" "}
                                            Женский
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="survey-modal__item">
                                <span className="survey-modal__item-number">{"2)"}</span>
                                <div className="survey-modal__item-content">
                                    <h3 className="survey-modal__text survey-modal__item-title">Возраст:</h3>
                                    <div className="survey-modal__item-wrapper">
                                        <Dropdown
                                            list={age}
                                            classBlock="survey-modal__dropdown"
                                            setData={setAgeData}
                                            defaultValue="Нужно выбрать"
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="survey-modal__item">
                            <span className="survey-modal__item-number">{"3)"}</span>
                            <div className="survey-modal__item-content">
                                <h3 className="survey-modal__text survey-modal__item-title">Категории:</h3>
                                <div className="survey-modal__item-wrapper">
                                    {categorie.map((v, i) => (
                                        <label className="survey-modal__text survey-modal__cube" key={i}>
                                            <input
                                                type="checkbox"
                                                name="categorie"
                                                className="survey-modal__item-input"
                                                value={v}
                                                checked={categories.indexOf(v) != -1}
                                                onChange={(e) => handleSelectCategorie(e)}
                                            />{" "}
                                            {v}
                                        </label>
                                    ))}
                                </div>
                            </div>
                        </div>
                        <div className="survey-modal__item">
                            <span className="survey-modal__item-number">{"4)"}</span>
                            <div className="survey-modal__item-content">
                                <h3 className="survey-modal__text survey-modal__item-title">Бюджет:</h3>
                                <div className="survey-modal__item-wrapper">
                                    <label className="survey-modal__text survey-modal__item-price">
                                        <input
                                            maxLength={7}
                                            type="text"
                                            name="price"
                                            value={price}
                                            onChange={(e) => handleChangePrice(e)}
                                            onBlur={() => handleBlurPrice()}
                                        />{" "}
                                        ₽
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div className="survey-modal__item">
                            <span className="survey-modal__item-number">{"5)"}</span>
                            <div className="survey-modal__item-content">
                                <h3 className="survey-modal__text survey-modal__item-title">Повод для подарка:</h3>
                                <div className="survey-modal__item-wrapper">
                                    {holidays.map((v, i) => (
                                        <label className="survey-modal__text survey-modal__cube" key={i}>
                                            <input
                                                type="checkbox"
                                                name="holidays"
                                                className="survey-modal__item-input"
                                                value={v}
                                                checked={occasions.indexOf(v) != -1}
                                                onChange={(e) => handleSelectOccasions(e)}
                                            />{" "}
                                            {v}
                                        </label>
                                    ))}
                                </div>
                            </div>
                        </div>
                        <button type="submit" className="survey-modal__submit">
                            Результат
                        </button>
                    </form>
                </div>
            </WrapperModal>
        </div>
    );
}

export default SurveyModal;
