import React, { useRef, useState } from "react";
import "./style.scss";

function Dropdown({ list, classBlock = "", defaultValue = "" }) {
    const refTime = useRef(null);
    const [textTime, setTextTime] = useState(defaultValue != "" ? defaultValue : list[0]);

    const handleClick = () => {
        refTime.current.classList.toggle("dropdown_active");
    };

    const handleClickItem = (value) => {
        setTextTime(value);
        refTime.current.classList.remove("dropdown_active");
    };

    return (
        <div className={"dropdown " + classBlock} ref={refTime}>
            <div className={"dropdown__content"}>
                <p className="dropdown__text" onClick={handleClick}>
                    {textTime}
                </p>
                <div className="dropdown__list">
                    {list?.map((v, i) => (
                        <p className="dropdown__item" key={i} onClick={() => handleClickItem(v)}>
                            {v}
                        </p>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default Dropdown;
