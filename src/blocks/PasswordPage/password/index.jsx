import React from "react";
import Button from "../../../component/button";
import "./style.scss"

function Password() {
    return (
        <section className="password">
            <div className="password__content">
                <h1 className="password__title">Восстановление пароля</h1>
                <div className="password__block">
                    <form action="" className="password__form">
                        <label className="password__label">
                            Придумайте новый пароль:
                            <input type="password" className="password__input" />
                        </label>
                        <label className="password__label">
                            Повторите пароль:
                            <input type="password" className="password__input" />
                        </label>
                        <Button text="Установить новый пароль" classNameText="password__button"/>
                    </form>
                </div>
            </div>
        </section>
    );
}

export default Password;