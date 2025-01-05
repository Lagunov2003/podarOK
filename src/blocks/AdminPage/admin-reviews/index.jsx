import React, { useEffect, useState } from "react";
import "./style.scss";
import {
    responseDeleteNotAcceptedSiteReviews,
    responseGetNotAcceptedSiteReviews,
    responsePutchangeAcceptedStatusSiteReviews,
} from "../../../tool/response";
import LoadingCircle from "../../../component/loading-circle";

const stats = new Array(5).fill("");

function AdminReviews() {
    const [list, setList] = useState([]);
    const [update, setUpdate] = useState(false);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(false);
        (async () => {
            await responseGetNotAcceptedSiteReviews(setList);
            setUpdate(false);
            setLoading(true);
        })();
    }, [update]);

    const handleAcceptReview = async (id) => {
        setLoading(false);
        await responsePutchangeAcceptedStatusSiteReviews(id);
        setUpdate(true);
        setLoading(true);
    };

    const handleDeleteReview = async (id) => {
        setLoading(false);
        await responseDeleteNotAcceptedSiteReviews(id);
        setUpdate(true);
        setLoading(true);
    };

    return (
        <div className="admin-reviews">
            <h2 className="admin-reviews__title">Отзывы о сайте</h2>
            <LoadingCircle loading={loading}>
                {list?.length !== 0 ? (
                    <div className="admin-reviews__list">
                        {list?.map((v) => (
                            <div className="admin-reviews__item" key={v.userId}>
                                <div className="admin-reviews__item-content">
                                    <div className="admin-reviews__item-row">
                                        <span className="admin-reviews__item-name">{v.userName}</span>
                                        <span className="admin-reviews__item-id">№{v.userId}</span>
                                        <div className="admin-reviews__item-stars">
                                            {stats.map((_, i) => (
                                                <img
                                                    src={v.mark > i ? "/img/rating-star.svg" : "/img/rating-star-empty.svg"}
                                                    alt="Элемент оформления"
                                                    key={i}
                                                />
                                            ))}
                                        </div>
                                    </div>
                                    <p className="admin-reviews__item-text">{v.review}</p>
                                </div>
                                <div className="admin-reviews__row">
                                    <button
                                        className="admin-reviews__button admin-reviews__button_accept"
                                        onClick={() => handleAcceptReview(v.id)}
                                    >
                                        Принять
                                    </button>
                                    <button
                                        className="admin-reviews__button admin-reviews__button_reject"
                                        onClick={() => handleDeleteReview(v.id)}
                                    >
                                        Отклонить
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                ) : (
                    <p className="admin-reviews__empty">Список отзывов пуст!</p>
                )}
            </LoadingCircle>
        </div>
    );
}

export default AdminReviews;
