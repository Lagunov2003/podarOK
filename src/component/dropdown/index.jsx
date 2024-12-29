import React, { useEffect, useRef, useState } from "react";
import "./style.scss";

function Dropdown({ list, setData, classBlock = "", defaultValue = "", inactive = false }) {
    const refTime = useRef(null);
    const [textTime, setTextTime] = useState(defaultValue != "" ? defaultValue : list[0]);

    useEffect(() => {
        setTextTime(defaultValue != "" ? defaultValue : list[0])
    }, [defaultValue])

    const handleClick = () => {
        refTime.current.classList.toggle("dropdown_active");
    };

    const handleClickItem = (value) => {
        setTextTime(value);
        refTime.current.classList.remove("dropdown_active");
        setData(value)
    };

    return (
        <div className={"dropdown " + classBlock} ref={refTime}>
            <div className={"dropdown__content"}>
                {inactive == false ? (
                    <>
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
                    </>
                ) : (
                    <p className="dropdown__text">
                        {textTime}
                    </p>
                )}
            </div>
        </div>
    );
}

export default Dropdown;
