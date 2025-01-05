import React, { useEffect, useState } from "react";
import "./style.scss";
import { responseGetNotifications } from "../../../tool/response";
import LoadingCircle from "../../../component/loading-circle";

function Notice() {
    const [list, setList] = useState();
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(false);
        (async () => {
            await responseGetNotifications(setList);
            setLoading(true)
        })();
    }, []);

    return (
        <div className="notice">
            <LoadingCircle loading={loading}>
                {list?.length !== 0 ? (
                    <div className="notice__list">
                        {list
                            ?.map((v) => (
                                <div className="notice__item" key={v.id}>
                                    <p className="notice__item-text">{v.itemValue}</p>
                                    <p className="notice__item-date">
                                        {v.creationDateTime.split("T")[0].split("-").reverse().join(".")}{" "}
                                        {v.creationDateTime.split("T")[1].split(":")[0] +
                                            ":" +
                                            v.creationDateTime.split("T")[1].split(":")[1]}
                                    </p>
                                </div>
                            ))
                            ?.reverse()}
                    </div>
                ) : (
                    <p className="notice__empty">Тут пока пусто</p>
                )}
            </LoadingCircle>
        </div>
    );
}

export default Notice;
