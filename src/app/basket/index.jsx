import React from "react";
import WrapperBasket from "../../blocks/BasketPage/wrapper-basket";

const list = [
    {
        name: "Кружка стеклянная двойная с медведем",
        price: 1199,
        count: 1,
    },
    {
        name: "Название товара",
        price: 9999,
        count: 1,
    },
];

function Basket() {
    return (
        <>
            <WrapperBasket list={list} />
        </>
    );
}

export default Basket;
