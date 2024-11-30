import React, { useEffect, useRef } from "react";
import Intro from "../../blocks/MainPage/intro";
import Manual from "../../blocks/MainPage/manual";
import Advantage from "../../blocks/MainPage/advantage";
import Survey from "../../blocks/MainPage/survey";
import GiftsInCity from "../../blocks/MainPage/gifts-in-city";
import Delivery from "../../blocks/MainPage/delivery";
import Reviews from "../../blocks/MainPage/reviews";

function Main() {
    const refDelivery = useRef(null);
    const refReviews = useRef(null);

    useEffect(() => {
        let scroll = sessionStorage.getItem("scroll");

        if (scroll) {
            scroll == "delivery"
                ? refDelivery.current.scrollIntoView({ behavior: "smooth", block: "center" })
                : refReviews.current.scrollIntoView({ behavior: "smooth", block: "center" });
            setTimeout(() => {
                sessionStorage.removeItem("scroll")
            }, 0)
        }
    }, []);

    return (
        <>
            <Intro />
            <Manual />
            <Advantage />
            <GiftsInCity />
            <Delivery refDelivery={refDelivery} />
            <Reviews refReviews={refReviews} />
            <Survey />
        </>
    );
}

export default Main;
