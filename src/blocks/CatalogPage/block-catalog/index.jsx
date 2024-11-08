import React from "react";
import "./style.scss";
import Dropdown from "../../../component/dropdown";

const drop = ["Выбрать всё", "По рейтингу", "По возрастанию цены", "По убыванию цены", "По количеству заказов", "По дате добавления"]

function BlockCatalog() {

    return <div className="block-catalog">
        <div className="block-catalog__banner">
            <img src="/img/catalog/banner.svg" alt="" />
        </div>
        <div className="block-catalog__row">
            <div className="block-catalog__wrapper">
                <Dropdown list={drop} defaultValue="Сортировка по"/>
            </div>
            <label className="block-catalog__find">
                <input type="text" name="" placeholder="Найти подарок"/>
            </label>
            <button className="button-style block-catalog__button">Пройти опрос</button>
        </div>
    </div>
}

export default BlockCatalog;