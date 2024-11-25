import React, { useState } from "react";
import "./style.scss";
import Dropdown from "../dropdown";
import { Link, useNavigate } from "react-router-dom";

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
    const [open, setOpen] = useState(false);
    const navigate = useNavigate();

    const handleOpen = () => {
        document.body.classList.toggle("local-page");
        setOpen((t) => !t);
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        document.body.classList.remove("local-page");
        navigate("/catalog");
    };

    return (
        <div className="survey-modal">
            <button className="survey-modal__button" onClick={() => handleOpen()}>
                Пройти опрос
            </button>
            {open == true && (
                <div className="survey-modal__window">
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
                        <form className="survey-modal__form" id="surveyForm" onSubmit={(e) => handleSubmit(e)}>
                            <div className="survey-modal__row">
                                <div className="survey-modal__item">
                                    <span className="survey-modal__item-number">{"1)"}</span>
                                    <div className="survey-modal__item-content">
                                        <h3 className="survey-modal__text survey-modal__item-title">Пол:</h3>
                                        <div className="survey-modal__item-wrapper">
                                            <label className="survey-modal__text survey-modal__circle">
                                                <input type="radio" name="gender" className="survey-modal__item-input" /> Мужской
                                            </label>
                                            <label className="survey-modal__text survey-modal__circle">
                                                <input type="radio" name="gender" className="survey-modal__item-input" /> Женский
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div className="survey-modal__item">
                                    <span className="survey-modal__item-number">{"2)"}</span>
                                    <div className="survey-modal__item-content">
                                        <h3 className="survey-modal__text survey-modal__item-title">Возраст:</h3>
                                        <div className="survey-modal__item-wrapper">
                                            <Dropdown list={age} classBlock="survey-modal__dropdown" />
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
                                                <input type="radio" name="categorie" className="survey-modal__item-input" /> {v}
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
                                            <input type="text" name="gender" /> ₽
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
                                                <input type="radio" name="holidays" className="survey-modal__item-input" /> {v}
                                            </label>
                                        ))}
                                    </div>
                                </div>
                            </div>
                        </form>
                        <button type="submit" form="surveyForm" className="survey-modal__submit">
                            Результат
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
}

export default SurveyModal;
