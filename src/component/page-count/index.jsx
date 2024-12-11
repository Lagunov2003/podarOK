import React, { useState } from "react";
import "./style.scss";

function PageCount({ count }) {
    const [page, setPage] = useState(1);
    const [arr, setArr] = useState(new Array(count).fill(0));

    return (
        <div className="page-count">
            <div className="page-count__prev">
                <img src="/img/purple-arrow.svg" alt="" />
            </div>
            <div className="page-count__row">
                {arr.map((v, i) => (
                    <div className={"page-count__number " + (page == i + 1 ? "page-count__number_active" : "")}>{i + 1}</div>
                ))}
            </div>
            <div className="page-count__next">
                <img src="/img/purple-arrow.svg" alt="" />
            </div>
        </div>
    );
}

export default PageCount;
