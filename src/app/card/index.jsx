import React, { useEffect, useState } from "react";
import Info from "../../blocks/CardPage/info";
import SimilarProducts from "../../blocks/CardPage/similar-products";
import ReviewsItem from "../../blocks/CardPage/reviews-item";
import FeedbackItem from "../../blocks/CardPage/feedback-item";
import { useParams } from "react-router";
import { responseGetGift } from "../../tool/response";

function Card() {
    const { id } = useParams()
    const [item, setItem] = useState()

    useEffect(() => {
        responseGetGift(setItem, id)
    }, [id])

    return <>
        <Info item={item?.groupGifts[0] || null} reviewsAmount={item?.reviewsAmount || null} averageRating={item?.averageRating || null}/>
        <SimilarProducts list={item?.similarGifts || []}/>
        <ReviewsItem list={item?.reviews || []}/>
        <FeedbackItem />
    </>
}

export default Card;