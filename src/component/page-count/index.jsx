import React, { useEffect, useState } from "react";
import "./style.scss";

function PageCount({ count, setCurrentPage, currentPage }) {
    const [arr, setArr] = useState(new Array(count).fill(0));

    useEffect(() => {
        setArr(new Array(count).fill(0))
    }, [count])


    const handleClickPrev = () => {
        if(currentPage != 1) {
            setCurrentPage(v => --v)
        }
    }

    const handleClickNext = () => {
        if(currentPage != count) {
            setCurrentPage(v => ++v)
        }
    }

    return (
        <div className="page-count">
            <div className="page-count__prev" onClick={() => handleClickPrev()}>
                <img src="/img/purple-arrow.svg" alt="Предыдущая страница" />
            </div>
            <div className="page-count__row">
                {arr.map((_, i) => (
                    <span
                        className={"page-count__number " + (currentPage == i + 1 ? "page-count__number_active" : "")}
                        onClick={() => setCurrentPage(i + 1)}
                    >
                        {i + 1}
                    </span>
                ))}
            </div>
            <div className="page-count__next" onClick={() => handleClickNext()}>
                <img src="/img/purple-arrow.svg" alt="Следующая страница" />
            </div>
        </div>
    );
}

export default PageCount;
