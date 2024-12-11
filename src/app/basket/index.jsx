import React, { useState } from "react";
import WrapperBasket from "../../blocks/BasketPage/wrapper-basket";

const defList = [
    {
        name: "Кружка стеклянная двойная с медведем",
        price: 1199,
        count: 1,
        id: 1,
        categorie: "Товары для дома",
        holidays: "Новоселье",
    },
    {
        name: "Название товара",
        price: 9999,
        count: 1,
        id: 2,
        categorie: "Товары для дома",
        holidays: "Новоселье",
    },
    {
        name: "Название товара",
        price: 3400,
        count: 1,
        id: 3,
        categorie: "Товары для дома",
        holidays: "Новоселье",
    },
    {
        name: "Название товара",
        price: 9999,
        count: 1,
        id: 4,
        categorie: "Товары для дома",
        holidays: "Новоселье",
    },
    {
        name: "Название товара",
        price: 3400,
        count: 1,
        id: 5,
        categorie: "Товары для дома",
        holidays: "Новоселье",
    },
    {
        name: "Название товара",
        price: 9999,
        count: 1,
        id: 6,
        categorie: "Товары для дома",
        holidays: "Новоселье",
    },
    {
        name: "Название товара",
        price: 3400,
        count: 1,
        id: 7,
        categorie: "Товары для дома",
        holidays: "Новоселье",
    },
];

function Basket() {
    const [list, setList] = useState(defList);

    return (
        <>
            <WrapperBasket list={list} setList={setList} />
        </>
    );
}

export default Basket;
