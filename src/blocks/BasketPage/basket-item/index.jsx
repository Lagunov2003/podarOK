import React, { useEffect, useState } from "react";
import "./style.scss";
import { convertImg, convertPrice } from "../../../tool/tool";
import { responsePutCart } from "../../../tool/response";

function BasketItem({ itemValue, setSelectItem, selectItem, setList }) {
    const [count, setCount] = useState(itemValue?.itemCount);

    useEffect(() => {
        const token = localStorage.getItem("token");

        setSelectItem((list) => [...list.map((v) => (v?.gift?.id != itemValue?.gift?.id ? v : { ...itemValue, itemCount: count }))]);

        if (token) responsePutCart(token, itemValue?.gift?.id, count);

    }, [count]);

    const handleMinus = () => {
        if (count >= 2) {
            setCount((c) => --c);
        }
    };

    const handlePlus = () => {
        if (count < 5) {
            setCount((c) => ++c);
        }
    };

    const handleSelect = (e) => {
        if (e.target.checked) {
            setSelectItem((list) => [...list, { ...itemValue, itemCount: count }]);
        } else {
            setSelectItem((list) => [...list.filter((v) => v?.gift?.id != itemValue?.gift?.id)]);
        }
    };

    const handleDelete = () => {
        const token = localStorage.getItem("token");

        setList((list) => [...list.filter((v) => v?.gift?.id != itemValue?.gift?.id)]);
        setSelectItem((list) => [...list.filter((v) => v?.gift?.id != itemValue?.gift?.id)]);

        if (token) responsePutCart(token, itemValue?.gift?.id, 0);
    };

    return (
        <div className="basket-item">
            <div className="basket-item__img">
                <img src={convertImg(itemValue?.gift?.photoUrl)} alt="Изображение товара" />
            </div>
            <div className="basket-item__info">
                <h2 className="basket-item__title">{itemValue?.gift?.name}</h2>
                <div className="basket-item__count">
                    <button className="basket-item__button" onClick={() => handleMinus()}>
                        -
                    </button>
                    <p className="basket-item__number">{count}</p>
                    <button className="basket-item__button" onClick={() => handlePlus()}>
                        +
                    </button>
                </div>
            </div>
            <p className="basket-item__price">{convertPrice(itemValue?.gift?.price)} ₽</p>
            <div className="basket-item__column">
                <input
                    type="checkbox"
                    className="basket-item__input"
                    onChange={(e) => handleSelect(e)}
                    checked={selectItem.filter((v) => v?.gift?.id == itemValue?.gift?.id)?.length != 0 ? true : false}
                />
                <button className="basket-item__delete" onClick={() => handleDelete()}>
                    <img src="/img/delete-basket.svg" alt="Кнопка удаления товара" />
                </button>
            </div>
        </div>
    );
}

export default BasketItem;
