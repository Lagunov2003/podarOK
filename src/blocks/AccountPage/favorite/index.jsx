import React, { useEffect, useState } from "react";
import "./style.scss";
import { Link } from "react-router-dom";
import { responseDeleteFromFavorites, responseGetFavorites, responsePostAddToFavorites } from "../../../tool/response";
import ItemCatalog from "../../CatalogPage/item-catalog";
import LoadingCircle from "../../../component/loading-circle";

function Favorite() {
    const [list, setList] = useState(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(false);
        (async () => {
            await responseGetFavorites(setList);
            setLoading(true)
        })();
    }, []);

    const handleAddFavorite = async (id) => {
        await responsePostAddToFavorites(id);
        await responseGetFavorites(setList);
    };

    const handleDeleteFavorite = async (id) => {
        if (window.location.pathname === "/account/favorite") {
            await responseDeleteFromFavorites(id);
            await responseGetFavorites(setList);
        }
    };

    return (
        <div className="favorite">
            <LoadingCircle loading={loading}>
                {list?.length !== 0 ? (
                    <>
                        <h2 className="favorite__title">Товары которые вам понравились</h2>
                        <div className="favorite__list">
                            {list?.map((v, i) => (
                                <div className="favorite__item" key={v.id}>
                                    <ItemCatalog
                                        item={v}
                                        handleDeleteFavorite={handleDeleteFavorite}
                                        handleAddFavorite={handleAddFavorite}
                                    />
                                </div>
                            ))}
                        </div>
                    </>
                ) : (
                    <div className="favorite__empty">
                        <p className="favorite__empty-text">Вы еще не добавили ни одного товара</p>
                        <Link to="/catalog" className="button-style favorite__empty-button">
                            Перейти в каталог
                        </Link>
                    </div>
                )}
            </LoadingCircle>
        </div>
    );
}

export default Favorite;
