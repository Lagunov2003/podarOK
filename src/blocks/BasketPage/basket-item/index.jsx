import React, { useEffect, useState } from "react";
import "./style.scss";
import { convertPrice } from "../../../tool/tool";

function BasketItem({ itemValue, setSelectItem, selectItem, setList }) {
    const [count, setCount] = useState(1);

    useEffect(() => {
        setSelectItem((list) => [...list.map((v) => (v.id != itemValue.id ? v : { ...itemValue, count: count }))]);
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
            setSelectItem((list) => [...list, { ...itemValue, count: count }]);
        } else {
            setSelectItem((list) => [...list.filter((v) => v.id != itemValue.id)]);
        }
    };

    const handleDelete = () => {
        setList(list => [...list.filter(v => v.id != itemValue.id)])
        setSelectItem((list) => [...list.filter(v => v.id != itemValue.id)]);
    }

    return (
        <div className="basket-item">
            <div className="basket-item__img">
                <img src="" alt="Изображение товара" />
            </div>
            <div className="basket-item__info">
                <h2 className="basket-item__title">{itemValue?.name}</h2>
                <div className="basket-item__descr">
                    <p className="basket-item__text">Категория: {itemValue?.categorie}</p>
                    <p className="basket-item__text">Повод: {itemValue?.holidays}</p>
                </div>
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
            <p className="basket-item__price">{convertPrice(itemValue?.price)} ₽</p>
            <div className="basket-item__column">
                <input
                    type="checkbox"
                    className="basket-item__input"
                    onChange={(e) => handleSelect(e)}
                    checked={selectItem.filter((v) => v.id == itemValue.id)?.length != 0 ? true : false}
                />
                <button className="basket-item__delete" onClick={() => handleDelete()}>
                    <img src="/img/delete-basket.svg" alt="Кнопка удаления товара" />
                </button>
            </div>
        </div>
    );
}

export default BasketItem;
