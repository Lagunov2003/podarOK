import React, { useRef, useState } from "react";
import "./style.scss";
import { Link } from "react-router-dom";
import InfoPassword from "../info-password";
import EmailSend from "../email-send";

const data = [
    {
        blockTitle: "Заказываете впервые?",
        blockText: "Создайте аккаунт за пару минут и продолжайте покупки!",
        blockButton: "Регистрация",
    },
    {
        blockTitle: "Уже есть аккаунт?",
        blockText: "Рады снова вас видеть! Войдите, чтобы увидеть свои доставки и избранные товары.",
        blockButton: "Вход",
    },
];

function SingIn({ openModal, activeModal }) {
    const [activeBlock, setActiveBlock] = useState(0);
    const [errorEnter, setErrorEnter] = useState(false);
    const [errorRegister, setErrorRegister] = useState(false);
    const [modalPassword, setModalPassword] = useState(false);
    const [modalEmail, setModalEmail] = useState(false);

    const refSingIn = useRef();
    const refFormEnter = useRef();
    const refFormRegister = useRef();

    const handleClickBB = () => {
        refSingIn.current.classList.toggle("sing-in_active");
        if (activeBlock == 0) {
            setErrorRegister(false);
        } else {
            setModalPassword(false);
            setErrorEnter(false);
            refFormEnter.current.reset();
            refFormRegister.current.reset();
        }
        setTimeout(() => {
            if (activeBlock == 0) {
                setActiveBlock(1);
            } else {
                setActiveBlock(0);
            }
        }, 150);
    };

    const handleOpenModal = () => {
        refSingIn.current.classList.remove("sing-in_visit");
        setTimeout(() => {
            openModal();
        }, 300);
    };

    const handleOnEnter = (e) => {
        e.preventDefault();
        setErrorEnter(true);
    };

    const handleOpenModalPassword = () => {
        setModalPassword((v) => !v);
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        if (e.target[2].value.length < 6) {
            setErrorRegister(true);
        } else {
            setErrorRegister(false);
        }
    };

    const handleChangeInputPassword = (e) => {
        let str = e.target.value;
        if (str != "" && !str[str.length - 1].match(/[a-zA-Z]/)) {
            e.target.value = str.slice(0, str.length - 1);
        }
    };

    const handleChangeInputName = (e) => {
        let str = e.target.value;
        if (str != "" && !str[str.length - 1].match(/[а-яА-Я]/)) {
            e.target.value = str.slice(0, str.length - 1);
        }
    };

    return (
        <div className={"sing-in" + (activeModal == true ? " sing-in_visit" : "")} ref={refSingIn}>
            <div className="sing-in__wrapper">
                <div className="sing-in__content">
                    {activeModal == true && (
                        <>
                            <div className="sing-in__block">
                                <p className="sing-in__block-title">{data[activeBlock].blockTitle}</p>
                                <p className="sing-in__block-text">{data[activeBlock].blockText}</p>
                                <div className="sing-in__block-bg">
                                    <button className="sing-in__block-button" onClick={handleClickBB}>
                                        {data[activeBlock].blockButton}
                                    </button>
                                </div>
                            </div>
                            <div className="sing-in__enter">
                                <p className="sing-in__enter-title">С возвращением!</p>
                                <p className="sing-in__enter-text">Введите ваши данные</p>
                                <form
                                    action=""
                                    className="sing-in__enter-form"
                                    id="formEnter"
                                    onSubmit={(e) => handleOnEnter(e)}
                                    ref={refFormEnter}
                                >
                                    <input type="email" className="sing-in__input" placeholder="Почта" />
                                    <input
                                        type="password"
                                        maxLength={20}
                                        className="sing-in__input"
                                        placeholder="Пароль"
                                        autoComplete="false"
                                    />
                                </form>
                                <div className="sing-in__enter-row">
                                    {errorEnter == true && <p className="sing-in__enter-error">Неверная почта или пароль</p>}
                                    <p className="sing-in__enter-forget" onClick={() => setModalEmail(true)}>
                                        Забыли пароль?
                                    </p>
                                </div>
                                <button className="sing-in__enter-button" type="submit" form="formEnter">
                                    Войти в аккаунт
                                </button>
                            </div>
                            <div className="sing-in__register">
                                <p className="sing-in__register-title">Создать аккаунт</p>
                                <form
                                    action=""
                                    className="sing-in__register-form"
                                    id="registerForm"
                                    onSubmit={(e) => handleSubmit(e)}
                                    ref={refFormRegister}
                                >
                                    <input
                                        type="text"
                                        className="sing-in__input"
                                        placeholder="Имя"
                                        onChange={(e) => handleChangeInputName(e)}
                                    />
                                    <input type="email" className="sing-in__input" placeholder="Почта" />
                                    <div className="sing-in__password-wrapper">
                                        <input
                                            type="password"
                                            name=""
                                            className="sing-in__input"
                                            placeholder="Пароль"
                                            autoComplete="false"
                                            maxLength={20}
                                            onChange={(e) => handleChangeInputPassword(e)}
                                        />
                                        <div className="infoBlockPassword" onClick={() => handleOpenModalPassword()}></div>
                                    </div>
                                </form>
                                <p className="sing-in__register-error">{errorRegister == true && "Пароль не соответствует требованиям!"}</p>
                                <button className="sing-in__enter-button" form="registerForm" type="submit">
                                    Создать
                                </button>
                            </div>

                            <button className="sing-in__cancel" onClick={() => handleOpenModal()}>
                                <svg width="23.000000" height="23.000000" viewBox="0 0 23 23" fill="none">
                                    <desc>Created with Pixso.</desc>
                                    <defs />
                                    <path
                                        id="cancle-color"
                                        d="M13.52 11.49L22.57 2.46C22.84 2.19 22.99 1.82 22.99 1.44C22.99 1.05 22.84 0.69 22.57 0.42C22.3 0.15 21.93 0 21.55 0C21.17 0 20.8 0.15 20.53 0.42L11.5 9.47L2.46 0.42C2.19 0.15 1.82 0 1.44 0C1.06 0 0.69 0.15 0.42 0.42C0.15 0.69 0 1.05 0 1.44C0 1.82 0.15 2.19 0.42 2.46L9.47 11.49L0.42 20.53C0.29 20.66 0.18 20.82 0.11 21C0.03 21.17 0 21.36 0 21.55C0 21.74 0.03 21.93 0.11 22.1C0.18 22.28 0.29 22.44 0.42 22.57C0.55 22.7 0.71 22.81 0.89 22.88C1.06 22.96 1.25 23 1.44 23C1.63 23 1.82 22.96 1.99 22.88C2.17 22.81 2.33 22.7 2.46 22.57L11.5 13.52L20.53 22.57C20.66 22.7 20.82 22.81 21 22.88C21.17 22.96 21.36 23 21.55 23C21.74 23 21.93 22.96 22.1 22.88C22.28 22.81 22.44 22.7 22.57 22.57C22.7 22.44 22.81 22.28 22.88 22.1C22.96 21.93 23 21.74 23 21.55C23 21.36 22.96 21.17 22.88 21C22.81 20.82 22.7 20.66 22.57 20.53L13.52 11.49Z"
                                        fill="#FFFFFF"
                                    />
                                </svg>
                            </button>
                        </>
                    )}
                </div>
                {modalPassword == true && <InfoPassword handleOpen={handleOpenModalPassword} />}
                {modalEmail == true && <EmailSend setModalEmail={setModalEmail} />}
            </div>
        </div>
    );
}

export default SingIn;
