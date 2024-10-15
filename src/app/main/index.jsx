import React from "react";
import Intro from "../../blocks/MainPage/intro";
import Manual from "../../blocks/MainPage/manual";
import Advantage from "../../blocks/MainPage/advantage";
import Survey from "../../blocks/MainPage/survey";
import GiftsInCity from "../../blocks/MainPage/gifts-in-city";
import Delivery from "../../blocks/MainPage/delivery";
import Reviews from "../../blocks/MainPage/reviews";

function Main() {
    return (
        <>
            <Intro />
            <Manual />
            <Advantage />
            <GiftsInCity />
            <Delivery />
            <Reviews />
            <Survey />
        </>
    );
}

export default Main;
