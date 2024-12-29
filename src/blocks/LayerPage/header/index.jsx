import React, { useContext } from "react";
import "./style.scss";
import Button from "../../../component/button";
import SingIn from "../../../component/sing-in";
import { Link, useLocation, useNavigate, useNavigation } from "react-router-dom";
import { ContextData } from "../../../app/app";
import { ModalAuth } from "../layer-page";

function Header({ handleOpenModal }) {
    const data = useContext(ContextData);
    const handleOpenAuth = useContext(ModalAuth);
    const location = useLocation();
    const navigate = useNavigate();

    const handleClickBasket = () => {
        if (data == null) {
            handleOpenAuth();
        } else {
            navigate("/basket");
        }
    };

    return (
        <header className="header">
            <div className="header__content padding-style">
                <div className="header__logo">
                    <Link to={"/"}>
                        podar<span>OK</span>
                    </Link>
                </div>
                <nav className="header__nav">
                    <Link to={"/catalog"} className="header__link">
                        Каталог
                    </Link>
                    <Link
                        to={"/"}
                        className="header__link"
                        onClick={() =>
                            location.pathname != "/"
                                ? sessionStorage.setItem("scroll", "delivery")
                                : document.getElementsByClassName("delivery")[0].scrollIntoView({ behavior: "smooth", block: "center" })
                        }
                    >
                        Доставка
                    </Link>
                    <Link
                        to={"/"}
                        className="header__link"
                        onClick={() =>
                            location.pathname != "/"
                                ? sessionStorage.setItem("scroll", "reviews")
                                : document.getElementsByClassName("reviews")[0].scrollIntoView({ behavior: "smooth", block: "center" })
                        }
                    >
                        Отзывы
                    </Link>
                    {data?.role != true && (
                        <button className="header__link" onClick={() => handleClickBasket()}>
                            Корзина
                        </button>
                    )}
                </nav>
                {data?.firstName ? (
                    <Link to={data.role ? "/admin" : "/account"} className="header__sing-in">
                        <span>Кабинет</span>
                    </Link>
                ) : (
                    <button className="header__sing-in" onClick={() => handleOpenModal()}>
                        Вход
                    </button>
                )}
            </div>
        </header>
    );
}

export default Header;
