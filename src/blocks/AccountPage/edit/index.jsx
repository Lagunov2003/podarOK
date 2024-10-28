import React from "react";
import "./style.scss";
import Button from "../../../component/button";
import { useNavigate } from "react-router";

function Edit() {
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        navigate("/account/user");
    };

    return (
        <div className="edit">
            <form className="edit__content" id="editUser" onSubmit={(e) => handleSubmit(e)}>
                <h2 className="edit__title">Редактировать профиль</h2>
                <div className="edit__top">
                    <div className="edit__top-img">
                        <img src="" alt="" />
                    </div>
                    <div className="edit__top-block">
                        <label className="edit__top-label edit__text">
                            Имя:
                            <input type="text" className="edit__input" />
                        </label>
                        <label className="edit__top-label edit__text">
                            Фамилия:
                            <input type="text" className="edit__input" />
                        </label>
                    </div>
                </div>
                <div className="edit__info">
                    <div className="edit__info-item">
                        <p className="edit__text">Дата Рождения:</p>
                        <input type="date" className="edit__input" />
                    </div>
                    <div className="edit__info-item">
                        <p className="edit__text">Пол:</p>
                        <div className="edit__info-wrapper">
                            <label className="edit__text">
                                <input type="radio" name="gender" className="edit__radio" />
                                Мужской
                            </label>
                            <label className="edit__text">
                                <input type="radio" name="gender" className="edit__radio" />
                                Женский
                            </label>
                        </div>
                    </div>
                    <div className="edit__info-item">
                        <p className="edit__text">Почта:</p>
                        <input type="email" className="edit__input" />
                    </div>
                    <div className="edit__info-item">
                        <p className="edit__text">Телефон:</p>
                        <input type="tel" className="edit__input" />
                    </div>
                </div>
                <Button text="Сохранить" classNameText="edit__submit" typeButton="submit" idForm="editUser" />
            </form>
        </div>
    );
}

export default Edit;
