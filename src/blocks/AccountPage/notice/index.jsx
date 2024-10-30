import React from "react";
import "./style.scss";

const list = [
    {
        text: "Выполнен вход в аккаунт 01.01.2025 в 19:53. Это были не Вы? Напишите нам в поддержку.",
        date: "01.01.2025 19:55",
    },
    {
        text: "Ваш заказ №35412 собран. В течение суток он будет передан на доставку. Я не уверена что про заказы нам надо тут писать, сделала чисто для примера.",
        date: "01.01.2025 19:55",
    },
    {
        text: "Ваш пароль успешно изменен. Можете продолжать покупки!",
        date: "01.01.2025 19:55",
    },
];

function Notice() {
    return (
        <div className="notice">
            {list.length != 0 ? (
                <div className="notice__list">
                    {list.map((v, i) => (
                        <div className="notice__item" key={i}>
                            <p className="notice__item-text">{v.text}</p>
                            <p className="notice__item-date">{v.date}</p>
                        </div>
                    ))}
                </div>
            ) : (
                <p className="notice__empty">Тут пока пусто</p>
            )}
        </div>
    );
}

export default Notice;
