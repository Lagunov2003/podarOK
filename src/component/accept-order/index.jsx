import React from "react";
import "./style.scss";

function AcceptOrder() {
    return (
        <section className="accept-order">
            <div className="accept-order__content">
                <div className="accept-order__img">
                    <img src="/img/accept-green.svg" alt="Элемент оформления" />
                </div>
                <h1 className="accept-order__title">Заказ оформлен!</h1>
                <p className="accept-order__text">Вы можете перейти в каталог и продолжить покупки!</p>
                <Link to={"/catalog"} className="accept-order__button">
                    В каталог
                </Link>
            </div>
        </section>
    );
}

export default AcceptOrder;
