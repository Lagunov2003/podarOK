import React from "react";
import "./style.scss";
import SurveyModal from "../../../component/survey-modal";

function Intro() {
    return (
        <section className="intro">
            <div className="intro__content padding-style">
                <h1 className="intro__title">
                    Подберите лучший
                    <span> подарок </span>
                    для близких
                </h1>
                <p className="intro__text">Пройдите небольшой опрос и мы составим подборку самых подходящих подарков именно для вас</p>
                <SurveyModal />
                <div className="intro__decor-bag">
                    <img src="/img/bag-podarOK.png" alt="Декоративный элемент" />
                </div>
                <div className="intro__decor-star">
                    <img src="/img/star-yellow.svg" alt="Декоративный элемент" />
                </div>
            </div>
        </section>
    );
}

export default Intro;
