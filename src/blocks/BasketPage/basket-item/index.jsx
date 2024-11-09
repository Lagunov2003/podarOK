import React, { useEffect, useState } from "react";
import "./style.scss";

function BasketItem({ itemValue, setSelectItem, selectItem }) {
    const [count, setCount] = useState(1);

    const handleMinus = () => {
        if (count >= 2) {
            setCount((c) => --c);
        }
    };

    const handlePlus = () => {
        if (count <= 9) {
            setCount((c) => ++c);
        }
    };

    const handleSelect = (e) => {
        if (e.target.checked) {
            setSelectItem((list) => [...list, {...itemValue, count: count}]); 
        } else {
            setSelectItem((list) => [...list.filter((v) => v.name != itemValue.name)])
        }
    };


    useEffect(() => {
        setSelectItem((list) => [...list.map((v) => v.name != itemValue.name ? v : {...itemValue, count: count})])
    }, [count])

    return (
        <div className="basket-item">
            <div className="basket-item__img">
                <img src="" alt="" />
            </div>
            <div className="basket-item__info">
                <h2 className="basket-item__title">{itemValue?.name}</h2>
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
            <p className="basket-item__price">{itemValue?.price}₽</p>
            <input type="checkbox" className="basket-item__input" onChange={(e) => handleSelect(e)} checked={selectItem.filter((v) => v.name == itemValue.name)?.length != 0 ? true : false}/>
        </div>
    );
}

export default BasketItem;
