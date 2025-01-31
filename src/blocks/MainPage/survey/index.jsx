import React from "react";
import "./style.scss";
import SurveyModal from "../../../component/survey-modal";

function Survey() {
    return (
        <section className="survey">
            <div className="survey__content padding-style">
                <h2 className="survey__title title-style">Опрос</h2>
                <p className="survey__text">
                    Чтобы подобрать подарок для своих друзей или близких пройдите небольшой опрос и мы найдем для вас множество интересных
                    вариантов разных подарков.
                </p>
                <p className="survey__text">Удачных покупок!</p>
                <SurveyModal />
                <div className="survey__decor-heart">
                    <img src="/img/heart.png" alt="Декоративный элемент" />
                </div>
            </div>
        </section>
    );
}

export default Survey;
