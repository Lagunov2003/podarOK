import React from "react";
import "./style.scss";
import Button from "../../../component/button";
import SingIn from "../../../component/sing-in";
import { Link } from "react-router-dom";

function Header({ handleOpenModal }) {
    return (
        <header className="header">
            <div className="header__content padding-style">
                <div className="header__logo">
                    <Link to={"/"}>
                        podar<span>OK</span>
                    </Link>
                </div>
                <Link to={"/account/user"}>
                    <span>аккаунт</span>
                </Link>
                <nav className="header__nav">
                    <Link to={"/catalog"} className="header__link">
                        Каталог
                    </Link>
                    <Link to={"/"} className="header__link">
                        Доставка
                    </Link>
                    <Link to={"/"} className="header__link">
                        Отзывы
                    </Link>
                    <Link to={"/basket"} className="header__link">
                        Корзина
                    </Link>
                </nav>
                <button className="header__sing-in" onClick={() => handleOpenModal()}>
                    Вход
                </button>
            </div>
        </header>
    );
}

export default Header;
