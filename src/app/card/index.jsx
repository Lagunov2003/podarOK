import React from "react";
import Info from "../../blocks/CardPage/info";
import SimilarProducts from "../../blocks/CardPage/similar-products";
import ReviewsItem from "../../blocks/CardPage/reviews-item";
import FeedbackItem from "../../blocks/CardPage/feedback-item";

function Card() {

    return <>
        <Info />
        <SimilarProducts />
        <ReviewsItem />
        <FeedbackItem />
    </>
}

export default Card;