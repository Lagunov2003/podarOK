import React, { useEffect, useState } from "react";
import "./style.scss";
import { responseGetAllDialogs } from "../../../tool/response";
import { useNavigate } from "react-router";

function AdminChats() {
    const [list, setList] = useState([]);
    const [data, setData] = useState(null);
    const [search, setSearch] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        (async () => {
            await responseGetAllDialogs(setData);
        })();
    }, []);

    useEffect(() => {
        setList(data?.filter((v) => v?.userId?.toString().indexOf(search) != -1));
    }, [search, data]);

    const handleChangeSearch = (value) => {
        if (value == "" || value[value.length - 1].match(/[0-9]/)) setSearch(value);
    };

    return (
        <div className="admin-chats">
            <div className="admin-chats__search">
                <input type="text" placeholder="Найти чат" value={search} onChange={(e) => handleChangeSearch(e.target.value)} />
                <div className="admin-chats__search-img">
                    <img src="/img/find.svg" alt="Поиск" />
                </div>
            </div>
            {list?.length != 0 ? (
                <div className="admin-chats__list">
                    {list?.map((v) => (
                        <div className="admin-chats__item" key={v.userId} onClick={() => navigate("/admin/chats/" + v.userId)}>
                            <span className="admin-chats__item-title">Пользователь: {v.username}</span>
                            <span className="admin-chats__item-id">Идентификатор пользователя: №{v.userId}</span>
                        </div>
                    ))}
                </div>
            ) : (
                <p className="admin-chats__empty">Чат не найден!</p>
            )}
        </div>
    );
}

export default AdminChats;
