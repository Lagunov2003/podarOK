import React, { useEffect, useState } from "react";
import WrapperBasket from "../../blocks/BasketPage/wrapper-basket";
import { responseGetCart } from "../../tool/response";


function Basket({ setOrder }) {
    const [list, setList] = useState([]);

    useEffect(() => {
        const token = localStorage.getItem("token")

        if(token) {
            responseGetCart(token, setList)
        }
    }, [])

    return (
        <>
            <WrapperBasket list={list} setList={setList} setOrder={setOrder}/>
        </>
    );
}

export default Basket;
