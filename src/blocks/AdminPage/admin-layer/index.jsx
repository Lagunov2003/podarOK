import React from "react";
import "./style.scss";
import { Link, useLocation } from "react-router-dom";

function AdminLayer({ children }) {
    const location = useLocation();

    return (
        <section className="admin-layer">
            <div className="admin-layer__content padding-style">
                <div className="admin-layer__menu">
                    <Link
                        to={"/admin"}
                        className={"admin-layer__link" + (location.pathname == "/admin" ? " admin-layer__link_active" : "")}
                    >
                        <span>Отзывы</span>
                    </Link>
                    <Link
                        to={"/admin/orders"}
                        className={"admin-layer__link" + (location.pathname == "/admin/orders" ? " admin-layer__link_active" : "")}
                    >
                        <span>Заказы</span>
                    </Link>
                    <Link
                        to={"/admin/chats"}
                        className={
                            "admin-layer__link" + (location.pathname.indexOf("/admin/chats") != -1 ? " admin-layer__link_active" : "")
                        }
                    >
                        <span>Чат поддержки</span>
                    </Link>
                    <Link
                        to={"/admin/setting"}
                        className={
                            "admin-layer__link" + (location.pathname.indexOf("/admin/setting") != -1 ? " admin-layer__link_active" : "")
                        }
                    >
                        <span>Настройки</span>
                    </Link>
                </div>
                <div className="admin-layer__block">{children}</div>
            </div>
        </section>
    );
}

export default AdminLayer;
