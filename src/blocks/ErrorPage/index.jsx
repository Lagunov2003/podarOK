import React from "react";
import "./style.scss";
import { Link } from "react-router-dom";

function ErrorPage() {

    return <section className="error-page">
        <div className="error-page__content">
            <h1 className="error-page__title">Упс! Этой страницы не существует</h1>
            <p className="error-page__text">Вы можете перейти на главную страницу и начать работу там!</p>
            <Link to={"/"} className="error-page__button">На главную</Link>
        </div>
    </section>
}

export default ErrorPage