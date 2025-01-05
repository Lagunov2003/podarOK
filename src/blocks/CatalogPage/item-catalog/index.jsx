import React, { useContext, useState } from "react";
import "./style.scss";
import { Link } from "react-router-dom";
import { convertImg, convertPrice } from "../../../tool/tool";
import { ModalAuth } from "../../LayerPage/layer-page";

function ItemCatalog({ item, handleDeleteFavorite = () => {}, handleAddFavorite = () => {} }) {
    const [favorite, setFavorite] = useState(item.isFavorite);
    const handleOpenModal = useContext(ModalAuth);

    const handleChangeFavorite = () => {
        const token = localStorage.getItem("token");

        if (token) {
            setFavorite((v) => !v);
            favorite === true ? handleDeleteFavorite(item.id) : handleAddFavorite(item.id);
        } else {
            handleOpenModal()
        }
    };

    return (
        <div className="item-catalog" key={item.id}>
            <div className="item-catalog__wrapper-img">
                <div className="item-catalog__img">
                    <img src={convertImg(item.photoUrl)} alt="Изображение товара" />
                </div>
                <span className="item-catalog__label">В наличии</span>
                <button className="item-catalog__favorite" onClick={() => handleChangeFavorite()}>
                    <img src={favorite === false ? "/img/catalog/favorite.svg" : "/img/catalog/favorite-purple.svg"} alt="Кнопка лайка" />
                </button>
            </div>
            <p className="item-catalog__price">{convertPrice(item.price)} ₽</p>
            <p className="item-catalog__name" title={item.name}>
                {item.name}
            </p>
            <Link to={"/article/" + item.id} className="item-catalog__button">
                Подробнее
            </Link>
        </div>
    );
}

export default ItemCatalog;
