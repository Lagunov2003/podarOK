
import React, { useState } from "react";
import Button from "../../../component/button";
import "./style.scss";
import { useNavigate } from "react-router";
import InfoPassword from "../../../component/info-password";

const errors = [
    "Пароли не совпадают!",
    "Пароль меньше 6 символов!"
]

function Password() {
    const [error, setError] = useState(-1);
    const [modalPassword, setModalPassword] = useState(false)
    const navigate = useNavigate()

    const handleSubmit = (e) => {
        e.preventDefault();

        if(e.target[0].value.length < 6 || e.target[1].value.length < 6 )   {
            setError(1);
        } else if (e.target[0].value != e.target[1].value && e.target[0].value != "") {
            setError(0);
        } else {
            setError(-1);
            navigate("/")
        }
    };

    const handleChangeInput = (e) => {
        let str = e.target.value
        if(str != "" && !str[str.length - 1].match(/[a-zA-Z]/)) {
            e.target.value = str.slice(0, str.length - 1);
        } 
    }

    const handleOpenModalPassword = () => {
        setModalPassword(v => !v)
    }


    return (
        <section className="password">
            <div className="password__content">
                <h1 className="password__title">Восстановление пароля</h1>
                <div className="password__block">

                    {error != -1 && <p className="password__error">{errors[error]}</p>}
                    <form
                        action=""
                        className="password__form"
                        id="passwordReset"
                        onSubmit={(e) => {
                            handleSubmit(e);
                        }}
                    >
                        <label className="password__label">
                            Придумайте новый пароль:
                            <input type="password" className="password__input" maxLength={12} name="passwordFirst" onChange={(e) => handleChangeInput(e)}/>
                            <button className="infoBlockPassword" type="button" onClick={() => handleOpenModalPassword()}></button>
                        </label>
                        <label className="password__label">
                            Повторите пароль:
                            <input type="password" className="password__input" maxLength={12} name="passwordSecond" onChange={(e) => handleChangeInput(e)}/>
                        </label>
                        <Button
                            text="Установить новый пароль"
                            classNameText="password__button"
                            typeButton="submit"
                            idForm="passwordReset"
                        />
                    </form>
                    {modalPassword == true && <InfoPassword handleOpen={handleOpenModalPassword}/>}
                </div>
            </div>
        </section>
    );
}

export default Password;

