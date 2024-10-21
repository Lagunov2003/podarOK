import React, { useState } from "react";
import { Link } from "react-router-dom";
import "./style.scss";

function Setting() {
    const [activeModal, setActiveModal] = useState(false);

    const handleOpenModal = () => {
        setActiveModal((v) => !v);
    };

    return (
        <div className="account-setting">
            <Link to="/password-change" className="account-setting__change-password">
                Изменить пароль
            </Link>
            <button className="account-setting__delete-password" onClick={() => handleOpenModal()}>
                Удалить аккаунт
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
