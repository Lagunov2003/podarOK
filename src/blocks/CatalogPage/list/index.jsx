import React from "react";
import "./style.scss";
import PageCount from "../../../component/page-count";
import { Link } from "react-router-dom";
import ItemCatalog from "../item-catalog";

function List({ list, page, setCurrentPage, currentPage }) {

    
    return (
        <div className="list">
            {list?.length != 0 ? (
                <>
                    <div className="list__content">
                        {list?.map((v) => (
                            <ItemCatalog item={v} />
                        ))}
                    </div>
                    <PageCount count={page.totalPages} setCurrentPage={setCurrentPage} currentPage={currentPage}/>
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
