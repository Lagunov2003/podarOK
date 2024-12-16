import React from "react";
import "./style.scss";
import { Link, useLocation } from "react-router-dom";

function AccountLayer({ children }) {
    const location = useLocation();

    return (
        <section className="account-layer">
            <div className="account-layer__content padding-style">
                <div className="account-layer__menu">
                    <Link
                        to={"/account"}
                        className={
                            "account-layer__link" +
                            (location.pathname == "/account" || location.pathname == "/account/edit"
                                ? " account-layer__link_active"
                                : "")
                        }
                    >
                        <span>Личные данные</span>
                    </Link>
                    <Link
                        to={"/account/notice"}
                        className={"account-layer__link" + (location.pathname == "/account/notice" ? " account-layer__link_active" : "")}
                    >
                        <span>Уведомления</span>
                    </Link>
                    <Link
                        to={"/account/order"}
                        className={"account-layer__link" + (location.pathname == "/account/order" ? " account-layer__link_active" : "")}
                    >
                        <span>Заказы</span>
                    </Link>
                    <Link
                        to={"/account/favorite"}
                        className={"account-layer__link" + (location.pathname == "/account/favorite" ? " account-layer__link_active" : "")}
                    >
                        <span>Избранные</span>
                    </Link>
                    <Link
                        to={"/account/help"}
                        className={"account-layer__link" + (location.pathname == "/account/help" ? " account-layer__link_active" : "")}
                    >
                        <span>Чат поддержки</span>
                    </Link>
                    <Link
                        to={"/account/setting"}
                        className={"account-layer__link" + (location.pathname == "/account/setting" ? " account-layer__link_active" : "")}
                    >
                        <span>Настройки</span>
                    </Link>
                </div>
                <div className="account-layer__block">{children}</div>
            </div>
        </section>
    );
}

export default AccountLayer;
