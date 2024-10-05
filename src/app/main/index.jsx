import React, { useEffect } from "react";
import PageLayer from "../../component/page-layer";
import Intro from "../../blocks/intro";
import Header from "../../blocks/header";
import Manual from "../../blocks/manual";
import Advantage from "../../blocks/advantage";

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
    </PageLayer>
  );
}

export default Main;
