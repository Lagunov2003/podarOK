import React from "react";
import "./style.scss";

function WrapperOrder(props) {
    return (
        <section className="wrapper-order">
            <div className="wrapper-order__content">
                <h1 className="wrapper-order__title">Оформление заказа</h1>
                {props.children}
            </div>
        </section>
    );
}

export default WrapperOrder;
