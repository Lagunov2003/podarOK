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

function AdminItemChat() {
    return (
        <div className="admin-item-chat">
            <div className="admin-item-chat__top">
                <span className="admin-item-chat__top-title">Пользователь: Олег Легов</span>
                <span className="admin-item-chat__top-id">Идентификатор пользователя: №24265</span>
                <span className="admin-item-chat__top-email">Почта пользователя: email@mail.ru</span>
            </div>
            <div className="admin-item-chat__list">
                {list.length != 0 ? (
                    <>
                        {list.map((v, i) => (
                            <div className={"admin-item-chat__item admin-item-chat__item-" + v.type}>
                                <p className="admin-item-chat__item-text">{v.text}</p>
                                <p className="admin-item-chat__item-date">{v.date}</p>
                            </div>
                        ))}
                    </>
                ) : (
                    <p className="admin-item-chat__empty">Здесь вы можете задать свои вопросы</p>
                )}
            </div>

            <div className="admin-item-chat__bottom">
                <form action="" id="admin-item-chatHelper">
                    <input type="text" placeholder="Введите сообщение" className="admin-item-chat__bottom-input" />
                    <button className="admin-item-chat__bottom-send">
                        <img src="/img/send-chat.svg" alt="" />
                    </button>
                </form>
            </div>
        </div>
    );
}

export default AdminItemChat;
