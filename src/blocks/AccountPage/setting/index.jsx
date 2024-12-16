import React, { useContext, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./style.scss";
import { ContextLogin } from "../../../app/app";

function Setting() {
    const navigate = useNavigate()
    const asyncLogin = useContext(ContextLogin)
    const [activeModal, setActiveModal] = useState(false);

    const handleOpenModal = () => {
        setActiveModal((v) => !v);
    };

    const handleClickExit = () => {
        localStorage.removeItem("token")
        asyncLogin()
        navigate("/")
    }

    return (
        <div className="account-setting">
            <Link to="/password-change" className="account-setting__change-password">
                Изменить пароль
            </Link>
            <button className="account-setting__delete-password" onClick={() => handleOpenModal()}>
                Удалить аккаунт
            </button>
            <button className="account-setting__delete-password" onClick={() => handleClickExit()}>
                Выйти из аккаунта
            </button>

            {activeModal == true && (
                <div className="account-setting__modal">
                    <div className="account-setting__modal-content">
                        <p className="account-setting__modal-title">Вы уверены, что хотите удалить аккаунт?</p>
                        <p className="account-setting__modal-text">Вы не сможете его восстановить</p>
                        <div className="account-setting__modal-row">
                            <button className="account-setting__modal-cancle" onClick={() => handleOpenModal()}>
                                отмена
                            </button>
                            <button className="account-setting__modal-yes" onClick={() => handleOpenModal()}>
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
