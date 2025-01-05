import React, { useContext, useEffect, useRef, useState } from "react";
import "./style.scss";
import { responseGetChatMessages, responsePostChatSend } from "../../../tool/response";
import { ContextData } from "../../../app/app";
import { convertDate } from "../../../tool/tool";
import LoadingCircle from "../../../component/loading-circle";
import { useLocation } from "react-router";

function ChatHelper() {
    const data = useContext(ContextData);
    const [messages, setMessages] = useState([]);
    const [loading, setLoading] = useState(false);
    const refList = useRef();
    const refInterval = useRef();
    const location = useLocation();

    useEffect(() => {
        setLoading(false);
        (async () => {
            await responseGetChatMessages(setMessages, "petrov.nikita702@mail.ru");
            setLoading(true);
            setTimeout(() => {
                refList.current.scrollTop = refList.current.scrollHeight;
            }, 0);

            clearInterval(refInterval.current);
            if (location.pathname === "/account/help") {
                refInterval.current = setInterval(() => {
                    (async () => {
                        await responseGetChatMessages(setMessages, "petrov.nikita702@mail.ru");
                        setTimeout(() => {
                            if (refList.current !== null) refList.current.scrollTop = refList.current.scrollHeight;
                        }, 0);
                    })();
                }, 5000);
            } 
        })();

        return () => {
            clearInterval(refInterval.current);
        };
    }, [location]);

    const handleSend = (e) => {
        e.preventDefault();

        if (e.target[0].value !== "") {
            (async () => {
                await responsePostChatSend(e.target[0].value, "petrov.nikita702@mail.ru");
                await responseGetChatMessages(setMessages, "petrov.nikita702@mail.ru");
                setTimeout(() => {
                    refList.current.scrollTop = refList.current.scrollHeight;
                }, 0);
                e.target[0].value = "";
            })();
        }
    };

    return (
        <div className="chat">
            <div className="chat__top">
                <p className="chat__top-title">Оператор поддержки</p>
            </div>
            <div className="chat__list" ref={refList}>
                <LoadingCircle loading={loading}>
                    {messages.length !== 0 ? (
                        <>
                            {messages.map((v, i) => (
                                <div
                                    className={"chat__item " + (data.email === v.receiverEmail ? "chat__item-helper" : "chat__item-user")}
                                    key={i}
                                >
                                    <p className="chat__item-text">{v.content}</p>
                                    <p className="chat__item-date">
                                        {convertDate(v.timestamp.split("T")[0])}{" "}
                                        {v.timestamp.split("T")[1].split(".")[0].split(":")[0] +
                                            ":" +
                                            v.timestamp.split("T")[1].split(".")[0].split(":")[1]}
                                    </p>
                                </div>
                            ))}
                        </>
                    ) : (
                        <p className="chat__empty">Здесь вы можете задать свои вопросы</p>
                    )}
                </LoadingCircle>
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
