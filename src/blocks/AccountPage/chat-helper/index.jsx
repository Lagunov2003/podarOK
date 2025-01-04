import React, { useEffect, useRef, useState } from "react";
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
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState("");
    const ws = useRef(null); // useRef для WebSocket, чтобы его не пересоздавать при ререндере

    useEffect(() => {
        // Создаем соединение при монтировании компонента
        ws.current = new WebSocket("ws://localhost:8080/ws"); // Замените на ваш WebSocket URL

        ws.current.onopen = () => {
            console.log("WebSocket connection opened");
        };

        ws.current.onmessage = (event) => {
            const message = JSON.parse(event.data); // Предполагаем JSON-формат сообщений
            setMessages((prevMessages) => [...prevMessages, message]);
        };

        ws.current.onclose = () => {
            console.log("WebSocket connection closed");
        };

        ws.current.onerror = (error) => {
            console.error("WebSocket error:", error);
        };

        // Закрываем соединение при размонтировании компонента
        return () => {
            if (ws.current && ws.current.readyState === WebSocket.OPEN) {
                ws.current.close();
            }
        };
    }, []);

    const handleSend = (e) => {
        e.preventDefault();
    };

    return (
        <div className="chat">
            <div className="chat__top">
                <p className="chat__top-title">Оператор поддержки</p>
            </div>
            <div className="chat__list">
                {list.length != 0 ? (
                    <>
                        {list.map((v, i) => (
                            <div className={"chat__item chat__item-" + v.type} key={i}>
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
                <form action="" id="chatHelper" onSubmit={(e) => handleSend(e)}>
                    <input type="text" placeholder="Введите сообщение" className="chat__bottom-input" />
                    <button className="chat__bottom-send">
                        <img src="/img/account/send-chat.svg" alt="Кнопка" />
                    </button>
                </form>
            </div>
        </div>
    );
}

export default ChatHelper;
