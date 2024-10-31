import React from "react";
import "./style.scss";
import PageCount from "../../../component/page-count";

function Favorite() {
    return (
        <div className="favorite">
            <h2 className="favorite__title">Товары которые вам понравились</h2>
            <div className="favorite__filter">По дате добавления ↓</div>
            <div className="favorite__list">
                <div className="favorite__item"></div>
                <div className="favorite__item"></div>
                <div className="favorite__item"></div>
                <div className="favorite__item"></div>
                <div className="favorite__item"></div>
                <div className="favorite__item"></div>
                <div className="favorite__item"></div>
                <div className="favorite__item"></div>
                <div className="favorite__item"></div>
            </div>
            <PageCount />
        </div>
    );
}

export default Favorite;
