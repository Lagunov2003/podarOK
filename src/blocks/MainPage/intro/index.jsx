import React from "react";
import "./style.scss";
import Button from "../../../component/button";

function Intro() {
  return (
    <section className="intro">
      <div className="intro__content padding-style">
        <h1 className="intro__title">
          Подберите лучший
          <span> подарок </span>
          для близких
        </h1>
        <p className="intro__text">
          Пройдите небольшой опрос и мы составим подборку самых подходящих
          подарков именно для вас
        </p>
        <Button text={"Пройти опрос"} />
        <div className="intro__decor-bag">
          <img src="/img/bag-podarOK.png" alt="" />
        </div>
        <div className="intro__decor-star">
          <img src="/img/star-yellow.svg" alt="" />
        </div>
      </div>
    </section>
  );
}

export default Intro;
