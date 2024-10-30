import React from "react";
import "./style.scss";

const list = [
    {
        text: "Здравствуйте, ничего не работает что делать?((",
        date: "12:13",
        type: "user",
    },
    {
        text: " Добрый день, меня зовут Анастасия, я - Ваш помощник из службы поддержки. Для решения вашей проблемы попробуйте перезагрузить страницу. Если не поможет - обращайтесь!",
        date: "12:16",
        type: "helper",
    },
    {
        text: "Здравствуйте, ничего не работает что делать?((",
        date: "12:13",
        type: "user",
    },
    {
        text: " Добрый день, меня зовут Анастасия, я - Ваш помощник из службы поддержки. Для решения вашей проблемы попробуйте перезагрузить страницу. Если не поможет - обращайтесь!",
        date: "12:16",
        type: "helper",
    },
];

function ChatHelper() {
    return (
        <div className="chat">
            <div className="chat__top">
                <div className="chat__top-icon"></div>
                <p className="chat__top-title">Оператор поддержки</p>
                <p className="chat__top-online">Online</p>
            </div>
            <div className="chat__list">
                {list.length != 0 ? (
                    <>
                        {list.map((v, i) => (
                            <div className={"chat__item chat__item-" + v.type}>
                                <p className="chat__item-text">{v.text}</p>
                                <p className="chat__item-date">{v.date}</p>
                            </div>
                        ))}
                    </>
                ) : (
                    <p className="chat__empty">Здесь вы можете задать свои вопросы</p>
                )}
            </div>

            <div className="chat__bottom">
                <form action="">
                    <input type="text" placeholder="Введите сообщение" className="chat__bottom-input" />
                    <button className="chat__bottom-send">
                        <img src="/img/account/send-chat.svg" alt="" />
                    </button>
                </form>
            </div>
        </div>
    );
}

export default ChatHelper;
