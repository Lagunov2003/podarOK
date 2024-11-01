import React from "react";
import "./style.scss";
import PageCount from "../../../component/page-count";
import Dropdown from "../../../component/dropdown";
import { Link } from "react-router-dom";

const filter = ["По дате добавления ↓", "По дате добавления ↑", "По возрастанию цены", "По убыванию цены", "Сначала в наличии"];

const list = ["", "", "", "", "", "", "", "", ""];

function Favorite() {
    return (
        <div className="favorite">
            {list.length != 0 ? (
                <>
                    <h2 className="favorite__title">Товары которые вам понравились</h2>
                    <div className="favorite__dropdown">
                        <Dropdown list={filter} />
                    </div>
                    <div className="favorite__list">
                        {list.map((v, i) => (
                            <div className="favorite__item" key={i}></div>
                        ))}
                    </div>
                    <PageCount />
                </>
            ) : (
                <div className="favorite__empty">
                    <p className="favorite__empty-text">Вы еще не добавили ни одного товара</p>
                    <Link to="/" className="button-style favorite__empty-button">
                        Перейти в каталог
                    </Link>
                </div>
            )}
        </div>
    );
}

export default Favorite;
