import React, { useEffect, useState } from "react";
import Button from "../../../component/button";
import "./style.scss";
import { useNavigate } from "react-router";
import InfoPassword from "../../../component/info-password";
import { useSearchParams } from "react-router-dom";
import { responseResetPassword } from "../../../tool/response";

const errors = ["Пароли не совпадают!", "Пароль меньше 6 символов!"];

function Password() {
    const [searchParams, setSearchParams] = useSearchParams();
    const [error, setError] = useState(-1);
    const [modalPassword, setModalPassword] = useState(false);
    const [tokenChange, setTokenChange] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        const token = searchParams.get("token");
        const code = searchParams.get("code")

        console.log(token, code, location.pathname);
        

        if (location.pathname == "/resetPassword") {
            if (token) {
                setTokenChange(token);
            } else {
                navigate("/error");
            }
        } else if (location.pathname == "/confirmChanges") {
            if (code) {
                setTokenChange(code);
            } else {
                navigate("/error");
            }
        } 
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();

        console.log(e.target[0].value, e.target[2].value);

        if (e.target[0].value.length < 6 || e.target[2].value.length < 6) {
            setError(1);
        } else if (e.target[0].value != e.target[2].value && e.target[0].value != "") {
            setError(0);
        } else {
            const email = localStorage.getItem("email");
            const status = await responseResetPassword(tokenChange, email, e.target[0].value, e.target[2].value);
            setError(-1);
            if (status == "успешно") {
                alert("Пароль успешно изменён!");
            } else {
                alert("Ошибка сервера!");
            }
            navigate("/");
        }
    };

    const handleChangeInput = (e) => {
        let str = e.target.value;
        if (str != "" && !str[str.length - 1].match(/[a-zA-Z]/)) {
            e.target.value = str.slice(0, str.length - 1);
        }
    };

    const handleOpenModalPassword = () => {
        setModalPassword((v) => !v);
    };

    return (
        <section className="password">
            <div className="password__content">
                <h1 className="password__title">Изменение пароля</h1>
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
                            <input
                                type="password"
                                autoComplete="off"
                                className="password__input"
                                maxLength={12}
                                name="passwordFirst"
                                onChange={(e) => handleChangeInput(e)}
                            />
                            <button className="infoBlockPassword" type="button" onClick={() => handleOpenModalPassword()}></button>
                        </label>
                        <label className="password__label">
                            Повторите пароль:
                            <input
                                type="password"
                                autoComplete="off"
                                className="password__input"
                                maxLength={12}
                                name="passwordSecond"
                                onChange={(e) => handleChangeInput(e)}
                            />
                        </label>
                        <Button
                            text="Установить новый пароль"
                            classNameText="password__button"
                            typeButton="submit"
                            idForm="passwordReset"
                        />
                    </form>
                    {modalPassword == true && <InfoPassword handleOpen={handleOpenModalPassword} />}
                </div>
            </div>
        </section>
    );
}

export default Password;
