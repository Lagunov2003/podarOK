import React, { useEffect, useState } from "react";
import "./style.scss";
import { responseGetOrdersAdmin, responsePutChangeOrderStatus } from "../../../tool/response";
import OrderItemUser from "../../AccountPage/order-item-user";
import { useLocation } from "react-router";
import LoadingCircle from "../../../component/loading-circle";

function AdminOrders() {
    const location = useLocation();
    const [list, setList] = useState([]);
    const [data, setData] = useState([]);
    const [search, setSearch] = useState("");
    const [idVisit, setIdVisit] = useState(-1);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(false);
        (async () => {
            await responseGetOrdersAdmin(setData);
            setLoading(true)
        })();
    }, []);

    useEffect(() => {
        const d = [...data];
        if (search !== "") {
            setList(d.filter((v) => v.id.toString().indexOf(search) !== -1));
        } else {
            setList(d);
        }
    }, [data, search]);

    const handleChangeSearch = (value) => {
        if (value === "" || value[value.length - 1].match(/[0-9]/)) setSearch(value);
    };

    const handleChangeStatus = (newStatus) => {
        if (location.pathname.indexOf("admin") !== -1) {
            (async () => {
                await responsePutChangeOrderStatus(idVisit, newStatus);
                await responseGetOrdersAdmin(setData);
                alert(`Статус заказа №${idVisit} изменён на "${newStatus}"`);
            })();
        }
    };

    return (
        <div className="admin-orders">
            {idVisit === -1 ? (
                <>
                    <div className="admin-orders__search">
                        <input
                            type="text"
                            placeholder="Найти заказ по номеру"
                            value={search}
                            onChange={(e) => handleChangeSearch(e.target.value)}
                        />
                        <div className="admin-orders__search-img">
                            <img src="/img/find.svg" alt="" />
                        </div>
                    </div>
                    <LoadingCircle loading={loading}>
                        {list?.length !== 0 ? (
                            <div className="admin-orders__list">
                                {list?.map((v) => (
                                    <div className="admin-orders__item" key={v.id} onClick={() => setIdVisit(v.id)}>
                                        <div className="admin-orders__item-info">
                                            <span className="admin-orders__item-text">
                                                Получатель: <span>{v.recipientName}</span>
                                            </span>
                                            <span className="admin-orders__item-text">
                                                Почта: <span>{v.recipientEmail}</span>
                                            </span>
                                            <span className="admin-orders__item-text">Стоимость: {v.orderCost} руб</span>
                                            <span className="admin-orders__item-text">Статус: {v.status}</span>
                                        </div>
                                        <p className="admin-orders__item-number">
                                            <span>Заказ</span>
                                            <span>№ {v.id}</span>
                                        </p>
                                    </div>
                                ))}
                            </div>
                        ) : (
                            <p className="admin-orders__empty">Список заказов пуст!</p>
                        )}
                    </LoadingCircle>
                </>
            ) : (
                <OrderItemUser data={data.filter((v) => v.id === idVisit)[0]} handleChangeStatus={handleChangeStatus} />
            )}
        </div>
    );
}

export default AdminOrders;
