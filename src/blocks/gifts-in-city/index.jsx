import React from "react";
import "./style.scss";
import Button from "../../component/button";

function GiftsInCity() {
  return (
    <section className="gifts-in-city">
      <div className="gifts-in-city__content padding-style">
        <h2 className="gifts-in-city__title title-style">
          Подарки в вашем городе
        </h2>
        <div className="gifts-in-city__grid">
          <div className="gifts-in-city__row">
            <div className="gifts-in-city__item">
              <h3 className="gifts-in-city__item-title">Техника</h3>
              <div className="gifts-in-city__item-img">
                <img src="/img/item-1.png" alt="" />
              </div>
            </div>
            <div className="gifts-in-city__item">
              <h3 className="gifts-in-city__item-title">Аксессуары</h3>
              <div className="gifts-in-city__item-img">
                <img src="/img/item-2.png" alt="" />
              </div>
            </div>
            <div className="gifts-in-city__item flex-x2">
              <h3 className="gifts-in-city__item-title">Популярное</h3>
              <div className="gifts-in-city__item-img">
                <img src="/img/item-3.png" alt="" />
              </div>
            </div>
          </div>
          <div className="gifts-in-city__row">
            <div className="gifts-in-city__item flex-x2">
              <h3 className="gifts-in-city__item-title">Скидки</h3>
              <div className="gifts-in-city__item-img">
                <img src="/img/item-4.png" alt="" />
              </div>
            </div>
            <div className="gifts-in-city__item">
              <h3 className="gifts-in-city__item-title">Цветы</h3>
              <div className="gifts-in-city__item-img">
                <img src="/img/item-5.png" alt="" />
              </div>
            </div>
            <div className="gifts-in-city__item">
              <h3 className="gifts-in-city__item-title">Десерты</h3>
              <div className="gifts-in-city__item-img">
                <img src="/img/item-6.png" alt="" />
              </div>
            </div>
          </div>
        </div>
        <p className="gifts-in-city__label">
          Больше вариантов можете увидеть в разделе Каталог{" "}
        </p>
        <Button
          text="Перейти в каталог"
          classNameText="gifts-in-city__button"
        />
      </div>
    </section>
  );
}

export default GiftsInCity;
