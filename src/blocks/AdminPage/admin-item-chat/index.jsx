import React, { useContext, useEffect, useRef, useState } from "react";
import "./style.scss";
import { ContextData } from "../../../app/app";
import { responseGetChatMessages, responsePostChatSend } from "../../../tool/response";
import { convertDate } from "../../../tool/tool";
import LoadingCircle from "../../../component/loading-circle";
import { useLocation } from "react-router";

function AdminItemChat({ chatAdmin }) {
    const data = useContext(ContextData);
    const [messages, setMessages] = useState();
    const [loading, setLoading] = useState(false);
    const refList = useRef();
    const refInterval = useRef();
    const location = useLocation();

    useEffect(() => {
        setLoading(false);

        if (chatAdmin !== null) {
            (async () => {
                await responseGetChatMessages(setMessages, chatAdmin?.senderEmail);
                setLoading(true);
                setTimeout(() => {
                    if (location.pathname.indexOf("/admin/chats/") !== -1 && refList.current !== null)
                        refList.current.scrollTop = refList.current.scrollHeight;
                }, 0);

                refInterval.current = setInterval(() => {
                    (async () => {
                        await responseGetChatMessages(setMessages, chatAdmin?.senderEmail);
                        setTimeout(() => {
                            if (location.pathname.indexOf("/admin/chats/") !== -1 && refList.current !== null)
                                refList.current.scrollTop = refList.current.scrollHeight;
                        }, 0);
                    })();
                }, 5000);
            })();
        }

        return () => {
            clearInterval(refInterval.current);
        };
    }, [data, chatAdmin, location]);

    const handleSend = (e) => {
        e.preventDefault();

        if (e.target[0].value !== "") {
            (async () => {
                await responsePostChatSend(e.target[0].value, chatAdmin?.senderEmail);
                await responseGetChatMessages(setMessages, chatAdmin?.senderEmail);
                setTimeout(() => {
                    if (location.pathname.indexOf("/admin/chats/") !== -1 && refList.current !== null)
                        refList.current.scrollTop = refList.current.scrollHeight;
                }, 0);
                e.target[0].value = "";
            })();
        }
    };

    return (
        <div className="admin-item-chat">
            <div className="admin-item-chat__top">
                <span className="admin-item-chat__top-title">Пользователь: {chatAdmin?.username}</span>
                <span className="admin-item-chat__top-id">Идентификатор пользователя: №{chatAdmin?.userId}</span>
                <span className="admin-item-chat__top-email">Почта пользователя: {chatAdmin?.senderEmail}</span>
            </div>
            <div className="admin-item-chat__list" ref={refList}>
                <LoadingCircle loading={loading}>
                    {messages?.length !== 0 ? (
                        <>
                            {messages?.map((v, i) => (
                                <div
                                    className={
                                        "admin-item-chat__item " +
                                        (data.email === v.receiverEmail ? "admin-item-chat__item-helper" : "admin-item-chat__item-user")
                                    }
                                    key={i}
                                >
                                    <p className="admin-item-chat__item-text">{v.content}</p>
                                    <p className="admin-item-chat__item-date">
                                        {convertDate(v.timestamp.split("T")[0])}{" "}
                                        {v.timestamp.split("T")[1].split(".")[0].split(":")[0] +
                                            ":" +
                                            v.timestamp.split("T")[1].split(".")[0].split(":")[1]}
                                    </p>
                                </div>
                            ))}
                        </>
                    ) : (
                        <p className="admin-item-chat__empty">Нет сообщений от пользователя!</p>
                    )}
                </LoadingCircle>
            </div>

            <div className="admin-item-chat__bottom">
                <form action="" id="admin-item-chatHelper" onSubmit={(e) => handleSend(e)}>
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
