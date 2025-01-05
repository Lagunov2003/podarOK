import React, { useEffect, useState } from "react";
import "./style.scss";
import { responseGetCurrentOrders, responseGetOrdersHistory } from "../../../tool/response";
import { convertDate, convertPrice } from "../../../tool/tool";
import OrderItemUser from "../order-item-user";
import LoadingCircle from "../../../component/loading-circle";

const textView = [
    {
        button: "Перейти к прошлым заказам",
        title: "Актуальные заказы",
        empty: "У вас ещё нет незавершенных заказов",
    },
    {
        button: "Перейти к актуальным заказам",
        title: "Прошлые заказы",
        empty: "У вас ещё нет завершенных заказов",
    },
];

function OrderAccount() {
    const [typeView, setTypeView] = useState(0);
    const [openItem, setOpenItem] = useState(false);
    const [orderData, setOrderData] = useState(null);
    const [list, setList] = useState(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(false);
        if (typeView === 0) {
            (async () => {
                await responseGetCurrentOrders(setList);
                setLoading(true);
            })();
        } else {
            (async () => {
                await responseGetOrdersHistory(setList);
                setLoading(true);
            })();
        }
    }, [typeView]);

    const handleChangeTypeView = () => {
        setTypeView((v) => (v === 0 ? 1 : 0));
    };

    const handleClickItem = (indexOrder) => {
        setOrderData(list.filter((v) => v.id === indexOrder)[0]);
        setOpenItem(true);
    };

    return (
        <div className="basket">
            <h2 className="basket__title">{openItem === false ? textView[typeView].title : "Информация о заказе"}</h2>
            {openItem === false ? (
                <>
                    <button className="basket__button" onClick={() => handleChangeTypeView()}>
                        {textView[typeView].button}
                    </button>
                    <LoadingCircle loading={loading}>
                        {list?.length !== 0 ? (
                            <div className="basket__list">
                                {list?.map((v) =>
                                    typeView === 0 ? (
                                        <div className="basket__item" key={v.id} onClick={() => handleClickItem(v.id)}>
                                            <div className="basket__item-info">
                                                <p className="basket__item-number">Заказ № {v.id}</p>
                                                <p className="basket__item-name-price">
                                                    Статус: {v.status}
                                                    <span>{convertPrice(v.orderCost)} руб</span>
                                                </p>
                                                <p className="basket__item-date">Ожидается получение: {convertDate(v.deliveryDate)}</p>
                                            </div>
                                        </div>
                                    ) : (
                                        <div className="basket__item" key={v.id} onClick={() => handleClickItem(v.id)}>
                                            <div className="basket__item-info">
                                                <p className="basket__item-number">Заказ № {v.id}</p>
                                                <p className="basket__item-name-price">
                                                    Статус: {v.status}
                                                    <span>{convertPrice(v.orderCost)} руб</span>
                                                </p>
                                                <p className="basket__item-date">Ожидается получение: {convertDate(v.deliveryDate)}</p>
                                            </div>
                                        </div>
                                    )
                                )}
                            </div>
                        ) : (
                            <p className="basket__empty">{textView[typeView].empty}</p>
                        )}
                    </LoadingCircle>
                </>
            ) : (
                <>{typeView === 0 ? <OrderItemUser data={orderData} /> : <OrderItemUser data={orderData} typeOrder="history" />}</>
            )}
        </div>
    );
}

export default OrderAccount;
