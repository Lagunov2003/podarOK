import React, { useEffect } from "react";
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

function Main() {
  useEffect(() => {
    document.title = "podarOK | Главная";
  }, []);

  return (
    <PageLayer>
      <Header />
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
