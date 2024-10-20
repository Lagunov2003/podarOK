import React from "react";
import "./style.scss";
import { Link, useLocation } from "react-router-dom";

function AccountLayer({ children }) {
    const location = useLocation()

    return (
        <section className="account-layer">
            <div className="account-layer__content padding-style">
                <ul className="account-layer__menu">
                    <li className={"account-layer__link" + (location.pathname == "/account/user" ? " account-layer__link_active" : "")}>
                        <Link to={"/account/user"}>Личные данные</Link>
                    </li>
                    <li className={"account-layer__link" + (location.pathname == "/account/notice" ? " account-layer__link_active" : "")}>
                        <Link to={"/account/notice"}>Уведомления</Link>
                    </li>
                    <li className={"account-layer__link" + (location.pathname == "/account/order" ? " account-layer__link_active" : "")}>
                        <Link to={"/account/order"}>Заказы</Link>
                    </li>
                    <li className={"account-layer__link" + (location.pathname == "/account/favorite" ? " account-layer__link_active" : "")}>
                        <Link to={"/account/favorite"}>Избранные</Link>
                    </li>
                    <li className={"account-layer__link" + (location.pathname == "/account/help" ? " account-layer__link_active" : "")}>
                        <Link to={"/account/help"}>Чат поддержки</Link>
                    </li>
                    <li className={"account-layer__link" + (location.pathname == "/account/setting" ? " account-layer__link_active" : "")}>
                        <Link to={"/account/setting"}>Настройки</Link>
                    </li>
                </ul>
                <div className="account-layer__block">{children}</div>
            </div>
        </section>
    );
}

export default AccountLayer;
