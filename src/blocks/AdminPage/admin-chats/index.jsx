import React, { useEffect, useState } from "react";
import "./style.scss";
import { responseGetAllDialogs } from "../../../tool/response";
import { useNavigate } from "react-router";
import LoadingCircle from "../../../component/loading-circle";

function AdminChats({ setChatAdmin }) {
    const [list, setList] = useState([]);
    const [data, setData] = useState(null);
    const [search, setSearch] = useState("");
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        setLoading(false);
        (async () => {
            await responseGetAllDialogs(setData);
            setLoading(true);
        })();
    }, []);

    useEffect(() => {
        setList(data?.filter((v) => v?.userId?.toString().indexOf(search) !== -1));
    }, [search, data]);

    const handleChangeSearch = (value) => {
        if (value === "" || value[value.length - 1].match(/[0-9]/)) setSearch(value);
    };

    const handleClickChat = (userId) => {
        setChatAdmin(data.filter((v) => v.userId === userId)[0]);
        navigate("/admin/chats/" + userId);
    };

    return (
        <div className="admin-chats">
            <div className="admin-chats__search">
                <input type="text" placeholder="Найти чат" value={search} onChange={(e) => handleChangeSearch(e.target.value)} />
                <div className="admin-chats__search-img">
                    <img src="/img/find.svg" alt="Поиск" />
                </div>
            </div>
            <LoadingCircle loading={loading}>
                {list?.length !== 0 ? (
                    <div className="admin-chats__list">
                        {list?.map((v) => (
                            <div className="admin-chats__item" key={v.userId} onClick={() => handleClickChat(v.userId)}>
                                <span className="admin-chats__item-title">Пользователь: {v.username}</span>
                                <span className="admin-chats__item-id">Идентификатор пользователя: №{v.userId}</span>
                            </div>
                        ))}
                    </div>
                ) : (
                    <p className="admin-chats__empty">Чат не найден!</p>
                )}
            </LoadingCircle>
        </div>
    );
}

export default AdminChats;
