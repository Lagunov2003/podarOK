import React, { useEffect, useState } from "react";
import PageLayer from "../../component/page-layer";
import Intro from "../../blocks/intro";
import Header from "../../blocks/header";
import Manual from "../../blocks/manual";
import Advantage from "../../blocks/advantage";
import Survey from "../../blocks/survey";
import GiftsInCity from "../../blocks/gifts-in-city";
import Delivery from "../../blocks/delivery";
import Footer from "../../blocks/footer";
import Reviews from "../../blocks/reviews";
import SingIn from "../../component/sing-in";

function Main() {
    const [activeModal, setActiveModal] = useState(false);

    useEffect(() => {
        document.title = "podarOK | Главная";
    }, []);

    const handleOpenModal = () => {
      document.body.classList.toggle("local-page")
      setActiveModal(v => !v)
    }

    return (
        <PageLayer>
            <Header handleOpenModal={handleOpenModal}/>
            <SingIn openModal={handleOpenModal} activeModal={activeModal}/>
            <Intro />
            <Manual />
            <Advantage />
            <GiftsInCity />
            <Delivery />
            <Reviews />
            <Survey />
            <Footer />
        </PageLayer>
    );
}

export default Main;
