import React, { useState } from "react";
import "./style.scss";
import Dropdown from "../../../component/dropdown";
import SurveyModal from "../../../component/survey-modal";

const drop = ["Выбрать всё", "По рейтингу", "По возрастанию цены", "По убыванию цены", "По количеству заказов", "По дате добавления"]

function BlockCatalog() {
    const [sortValue, setSortValue] = useState()
    const [search, setSearch] = useState("")

    return <div className="block-catalog">
        <div className="block-catalog__banner">
            <img src="/img/catalog/banner.svg" alt="Реклама" />
        </div>
        <div className="block-catalog__row">
            <div className="block-catalog__wrapper">
                <Dropdown list={drop} setData={setSortValue} defaultValue="Сортировка по"/>
            </div>
            <label className="block-catalog__find">
                <input type="text" placeholder="Найти подарок" value={search} onChange={(e) => setSearch(e.target.value)}/>
            </label>
            <SurveyModal />
        </div>
    </div>
}

export default BlockCatalog;