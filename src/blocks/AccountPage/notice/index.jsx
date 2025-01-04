import React, { useEffect, useState } from "react";
import "./style.scss";
import { responseGetNotifications } from "../../../tool/response";

function Notice() {
    const [list, setList] = useState();

    useEffect(() => {
        (async () => {
            await responseGetNotifications(setList);
        })();
    }, []);

    return (
        <div className="notice">
            {list?.length != 0 ? (
                <div className="notice__list">
                    {list?.map((v) => (
                        <div className="notice__item" key={v.id}>
                            <p className="notice__item-text">{v.itemValue}</p>
                            <p className="notice__item-date">
                                {v.creationDateTime.split("T")[0].split("-").reverse().join(".")}{" "}
                                {v.creationDateTime.split("T")[1].split(":")[0] + ":" + v.creationDateTime.split("T")[1].split(":")[1]}
                            </p>
                        </div>
                    ))?.reverse()}
                </div>
            ) : (
                <p className="notice__empty">Тут пока пусто</p>
            )}
        </div>
    );
}

export default Notice;
