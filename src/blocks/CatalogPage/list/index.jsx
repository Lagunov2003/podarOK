import React from "react";
import "./style.scss";
import PageCount from "../../../component/page-count";
import { Link } from "react-router-dom";

const list = [""];

function List() {
    return (
        <div className="list">
            {list.length != 0 ? (
                <>
                    <div className="list__content">
                        <div className="list__item">
                            <div className="list__item-img">
                                <img src="/img/catalog/item-exmaple.png" alt="" />
                                <span className="list__item-label">В наличии</span>
                                <div className="list__item-favorite">
                                    <img src="/img/catalog/favorite.svg" alt="" />
                                </div>
                            </div>
                            <p className="list__item-price">9999 ₽</p>
                            <p className="list__item-name">Кружка стеклянная двойная</p>
                            <Link to={"/article/1"} className="list__item-button">Подробнее</Link>
                        </div>
                        <div className="list__item">
                            <div className="list__item-img">
                                <img src="/img/catalog/item-exmaple.png" alt="" />
                                <span className="list__item-label">Нет в наличии</span>
                                <div className="list__item-favorite">
                                    <img src="/img/catalog/favorite-purple.svg" alt="" />
                                </div>
                            </div>
                            <p className="list__item-price">9999 ₽</p>
                            <p className="list__item-name">Название товара</p>
                            <Link to={"/article/2"} className="list__item-button">Подробнее</Link>
                        </div>
                    </div>
                    <PageCount />
                </>
            ) : (
                <div className="list__empty">
                    <h2 className="list__title">К сожалению, по вашему запросу ничего не нашлось :(</h2>
                    <p className="list__text">Попробуйте изменить заданные фильтры и проверьте написание запроса</p>
                </div>
            )}
        </div>
    );
}

export default List;
