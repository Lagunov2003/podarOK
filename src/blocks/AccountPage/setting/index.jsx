import React, { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./style.scss";
import { ContextLogin } from "../../../app/app";
import { responseChangePassword, responseDeleteFavorites } from "../../../tool/response";

function Setting() {
    const navigate = useNavigate()
    const asyncLogin = useContext(ContextLogin)
    const [activeModal, setActiveModal] = useState(false);

    const handleDeleteAccount = () => {
        ;(async () => {
            const status = await responseDeleteFavorites()
            if(status === "успешно") {
                setActiveModal((v) => !v);
                localStorage.removeItem("token")
                asyncLogin()
                navigate("/")
            }
        })()
    };

    const handleClickExit = () => {
        localStorage.removeItem("token")
        asyncLogin()
        navigate("/")
    }

    const handlePasswordChange = async () => {
        const status = await responseChangePassword()

        if(status === "успешно") {
            alert("Перейдите по ссылке в письме для подтверждения смены пароля!")
        } else {
            alert("Ошибка сервера!")
        }
    }

    return (
        <div className="account-setting">
            <button className="account-setting__change-password" onClick={() => handlePasswordChange()}>
                Изменить пароль
            </button>
            <button className="account-setting__delete-password" onClick={() =>  setActiveModal((v) => !v)}>
                Удалить аккаунт
            </button>
            <button className="account-setting__delete-password" onClick={() => handleClickExit()}>
                Выйти из аккаунта
            </button>

            {activeModal === true && (
                <div className="account-setting__modal">
                    <div className="account-setting__modal-content">
                        <p className="account-setting__modal-title">Вы уверены, что хотите удалить аккаунт?</p>
                        <p className="account-setting__modal-text">Вы не сможете его восстановить</p>
                        <div className="account-setting__modal-row">
                            <button className="account-setting__modal-cancle" onClick={() => setActiveModal((v) => !v)}>
                                отмена
                            </button>
                            <button className="account-setting__modal-yes" onClick={() => handleDeleteAccount()}>
                                да
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default Setting;
