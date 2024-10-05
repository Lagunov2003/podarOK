import React from "react";
import "./style.scss";
import Button from "../../component/button";

function Header() {
  return (
    <header className="header">
      <div className="header__content padding-style">
        <div className="header__logo">
          <p>
            podar<span>OK</span>
          </p>
        </div>
        <nav className="header__nav">
          <a href="" className="header__link">
            Каталог
          </a>
          <a href="" className="header__link">
            Доставка
          </a>
          <a href="" className="header__link">
            Отзывы
          </a>
          <a href="" className="header__link">
            Корзина
          </a>
          <Button text="Вход" />
        </nav>
      </div>
    </header>
  );
}

export default Header;
