import React from "react";
import "./style.scss";
import PageCount from "../../../component/page-count";
import { Link } from "react-router-dom";
import ItemCatalog from "../item-catalog";
import { responseDeleteFromFavorites, responsePostAddToFavorites } from "../../../tool/response";

function List({ list, page, setCurrentPage, currentPage }) {

    const handleAddFavorite = async (id) => {
        await responsePostAddToFavorites(id);
    };

    const handleDeleteFavorite = async (id) => {
        if (location.pathname == "/account/favorite") {
            await responseDeleteFromFavorites(id);
        }
    };

    return (
        <div className="list">
            {list?.length != 0 ? (
                <>
                    <div className="list__content">
                        {list?.map((v) => (
                            <ItemCatalog item={v} handleAddFavorite={handleAddFavorite} handleDeleteFavorite={handleDeleteFavorite}/>
                        ))}
                    </div>
                    <PageCount count={page.totalPages} setCurrentPage={setCurrentPage} currentPage={currentPage} />
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
