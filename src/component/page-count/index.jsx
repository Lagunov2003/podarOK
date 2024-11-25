import React from "react";
import "./style.scss";

function PageCount() {

    

    return (
        <div className="page-count">
            <div className="page-count__prev">
                <img src="/img/purple-arrow.svg" alt="" />
            </div>
            <div className="page-count__row">
                <div className="page-count__number">1</div>
                <div className="page-count__number page-count__number_active">2</div>
                <div className="page-count__number">3</div>
                <div className="page-count__number">4</div>
                <div className="page-count__number">5</div>
            </div>
            <div className="page-count__next">
                <img src="/img/purple-arrow.svg" alt="" />
            </div>
        </div>
    );
}

export default PageCount;
