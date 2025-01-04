import React, { useContext } from "react";
import "./style.scss";
import { Link } from "react-router-dom";
import { ContextData } from "../../../app/app";
import { convertDate } from "../../../tool/tool";

function User() {
    const data = useContext(ContextData);

    return (
        <div className="user">
            <div className="user__content">
                <div className="user__top">
                    <div className="user__top-block">
                        <div className="user__top-wrapper">
                            <p className="user__top-f-name">{data.firstName}</p>
                            <Link to={"/account/edit"} className="user__top-edit">
                                <img src="/img/account/pencil.svg" alt="Редактирование" />
                            </Link>
                        </div>
                        <p className="user__top-s-name">{data.lastName ? data.lastName : ""}</p>
                        <p className="user__top-reg">Дата регистрации {convertDate(data?.registrationDate)}</p>
                    </div>
                </div>
                <div className="user__info">
                    <div className="user__info-item">
                        <p className="user__info-text">Дата Рождения:</p>
                        <p className="user__info-text">{data.dateOfBirth ? data?.dateOfBirth?.split("-")?.reverse()?.join(".") : "не указан"}</p>
                    </div>
                    <div className="user__info-item">
                        <p className="user__info-text">Пол:</p>
                        <p className="user__info-text">{data.gender ? "Мужской" : "Женский"}</p>
                    </div>
                    <div className="user__info-item">
                        <p className="user__info-text">Почта:</p>
                        <p className="user__info-text">{data.email}</p>
                    </div>
                    <div className="user__info-item">
                        <p className="user__info-text">Телефон:</p>
                        <p className="user__info-text">{data.phoneNumber ? data.phoneNumber : "не указан"}</p>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default User;
