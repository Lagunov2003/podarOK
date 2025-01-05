import React from "react";
import "./style.scss";
import Dropdown from "../../../component/dropdown";
import SurveyModal from "../../../component/survey-modal";

const drop = ["Выбрать всё", "По рейтингу", "По возрастанию цены", "По убыванию цены"];

function BlockCatalog({ search, setSearch, sortValue, setSortValue }) {
    return (
        <div className="block-catalog">
            <div className="block-catalog__banner">
                <img src="/img/catalog/banner.svg" alt="Реклама" />
            </div>
            <div className="block-catalog__row">
                <div className="block-catalog__wrapper">
                    <Dropdown list={drop} setData={setSortValue} defaultValue={sortValue !== "" ? sortValue : "Сортировка по"} />
                </div>
                <label className="block-catalog__find">
                    <input type="text" placeholder="Найти подарок" value={search} onChange={(e) => setSearch(e.target.value)} />
                </label>
                <div className="block-catalog__wrapper-button">
                    <SurveyModal />
                </div>
            </div>
        </div>
    );
}

export default BlockCatalog;
