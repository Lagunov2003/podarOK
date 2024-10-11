import React from "react";
import "./style.scss";

function Footer() {
    return (
        <footer className="footer">
            <div className="footer__content padding-style">
                <div className="footer__info">
                    <div className="footer__name">
                        <p>
                            podar<span>OK</span>
                        </p>
                    </div>
                    <div className="footer__bottom-row">
                        <div className="footer__nav">
                            <a href="" className="footer__link">
                                Каталог
                            </a>
                            <a href="" className="footer__link">
                                Корзина
                            </a>
                            <a href="" className="footer__link">
                                Кабинет
                            </a>
                            <a href="" className="footer__link">
                                Доставка
                            </a>
                            <a href="" className="footer__link">
                                Отзывы
                            </a>
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
                        Укажите свою почту для подписки на наши новости и получите промокод на скидку 10%
                    </p>
                    <div className="footer__promocode-email">
                        <input type="email" name="" id="" placeholder="EMAIL" />
                    </div>
                </div>
            </div>
        </footer>
    );
}

export default Footer;
