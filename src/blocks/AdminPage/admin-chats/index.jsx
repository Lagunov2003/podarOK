import React from "react";
import "./style.scss";

function AdminChats() {
    return (
        <div className="admin-chats">
            <div className="admin-chats__search">
                <input type="text" placeholder="Найти чат" />
                <div className="admin-chats__search-img">
                    <img src="/img/find.svg" alt="" />
                </div>
            </div>
            <div className="admin-chats__list">
                <div className="admin-chats__item">
                    <span className="admin-chats__item-title">Пользователь: Олег  </span>
                    <span className="admin-chats__item-id">Идентификатор пользователя: №24265</span>
                    <span className="admin-chats__item-email">Почта пользователя: email@mail.ru</span>
                </div>
                <div className="admin-chats__item">
                    <span className="admin-chats__item-title">Пользователь: Олег  </span>
                    <span className="admin-chats__item-id">Идентификатор пользователя: №24265</span>
                    <span className="admin-chats__item-email">Почта пользователя: email@mail.ru</span>
                </div>
                <div className="admin-chats__item">
                    <span className="admin-chats__item-title">Пользователь: Олег  </span>
                    <span className="admin-chats__item-id">Идентификатор пользователя: №24265</span>
                    <span className="admin-chats__item-email">Почта пользователя: email@mail.ru</span>
                </div>
            </div>
        </div>
    );
}

export default AdminChats;
