import React from "react";
import "./style.scss"
import { Link } from "react-router-dom";

function User() {

    return (
        <div className="user">
            <div className="user__content">
                <div className="user__top">
                    <div className="user__top-img">
                        <img src="" alt="" />
                    </div>
                    <div className="user__top-block">
                        <p className="user__top-f-name">Имя</p>
                        <div className="user__top-wrapper">
                            <p className="user__top-s-name">Фамилия</p>
                            <Link to={"/account/user/edit"} className="user__top-edit">
                                <img src="/img/account/pencil.svg" alt="" />
                            </Link>
                        </div>
                        <p className="user__top-reg">Дата регистрации 01.01.2025</p>
                    </div>
                </div>
                <div className="user__info">
                    <div className="user__info-item">
                        <p className="user__info-text">Дата Рождения:</p>
                        <p className="user__info-text">20.02.2002</p>
                    </div>
                    <div className="user__info-item">
                        <p className="user__info-text">Пол:</p>
                        <p className="user__info-text">Женский</p>
                    </div>
                    <div className="user__info-item">
                        <p className="user__info-text">Почта:</p>
                        <p className="user__info-text">user01@mail.ru</p>
                    </div>
                    <div className="user__info-item">
                        <p className="user__info-text">Телефон:</p>
                        <p className="user__info-text">не указан</p>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default User;
