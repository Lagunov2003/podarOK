import React from "react";
import "./style.scss";
import { Link, useNavigate } from "react-router-dom";

function Footer({ handleOpenModal }) {
    const navigate = useNavigate();

    const handleClickAccount = (e, type) => {
        e.preventDefault();
        const token = localStorage.getItem("token");

        if (token) {
            if (type == "basket") {
                navigate("/basket");
            } else {
                navigate("/account");
            }
        } else {
            handleOpenModal();
        }
    };

    return (
        <footer className="footer">
            <div className="footer__content">
                <div className="footer__info">
                    <div className="footer__name" onClick={() => { navigate("/"); window.scrollTo({ top: 0 }) }}>
                        <p>
                            podar<span>OK</span>
                        </p>
                    </div>
                    <div className="footer__bottom-row">
                        <div className="footer__nav">
                            <Link to="/catalog" className="footer__link">
                                Каталог
                            </Link>
                            <Link className="footer__link" onClick={(e) => handleClickAccount(e, "basket")}>
                                Корзина
                            </Link>
                            <Link className="footer__link" onClick={(e) => handleClickAccount(e, "account")}>
                                Кабинет
                            </Link>
                            <Link
                                to={"/"}
                                className="header__link"
                                onClick={() =>
                                    location.pathname != "/"
                                        ? sessionStorage.setItem("scroll", "delivery")
                                        : document
                                              .getElementsByClassName("delivery")[0]
                                              .scrollIntoView({ behavior: "smooth", block: "center" })
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
                                        : document
                                              .getElementsByClassName("reviews")[0]
                                              .scrollIntoView({ behavior: "smooth", block: "center" })
                                }
                            >
                                Отзывы
                            </Link>
                        </div>
                        <div className="footer__contact">
                            <p className="footer__contact-title">Контакты:</p>
                            <address className="footer__contact-address">
                                <p className="footer__contact-tel">
                                    Телефон: <a href="tel:+79012345678">+79012345678</a>
                                </p>
                                <p className="footer__contact-email">
                                    Почта: <a href="mailto:podarOK@mail.ru">podarOK@mail.ru</a>
                                </p>
                            </address>
                            <div className="footer__contact-messages">
                                <a href="" className="footer__contact-url">
                                    <img src="/img/whatsapp.svg" alt="" />
                                </a>
                                <a href="" className="footer__contact-url">
                                    <img src="/img/vk.svg" alt="" />
                                </a>
                                <a href="" className="footer__contact-url">
                                    <img src="/img/tg.svg" alt="" />
                                </a>
                                <a href="" className="footer__contact-url">
                                    <img src="/img/ok.svg" alt="" />
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="footer__promocode">
                    <p className="footer__promocode-text">
                        Укажите свой номер телефона, чтобы мы могли информировать вас о новых акциях и других новостях. Мы отправим вам
                        <span> промокод на скидку 10%!</span>
                    </p>
                    <div className="footer__promocode-tel">
                        <input type="tel" maxLength={11} autoComplete="off" />
                        <button className="footer__promocode-button">
                            <img src="/img/arrow-line-white.svg" alt="Кнопка отправки" />
                        </button>
                    </div>
                </div>
            </div>
        </footer>
    );
}

export default Footer;
