import React, { useRef } from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation } from "swiper/modules";
import "swiper/css";
import "./style.scss";
import { convertPrice } from "../../../tool/tool";
import { Link } from "react-router-dom";

function SimilarProducts({ list }) {
    const prevBt = useRef(null);
    const nextBt = useRef(null);

    return (
        <section className="similar-products">
            <div className="similar-products__content">
                <h2 className="similar-products__title">Похожие товары</h2>
                <div className="similar-products__swiper">
                    <button className="similar-products__swiper-prev" ref={prevBt}></button>
                    <Swiper
                        modules={[Navigation]}
                        spaceBetween={35}
                        slidesPerView={"auto"}
                        onInit={(swiper) => {
                            swiper.params.navigation.prevEl = prevBt.current;
                            swiper.params.navigation.nextEl = nextBt.current;
                            swiper.navigation.init();
                            swiper.navigation.update();
                        }}
                    >
                        {list.map((v, i) => (
                            <SwiperSlide key={i}>
                                <div className="similar-products__item">
                                    <div className="similar-products__item-wrapper">
                                        <span className="similar-products__item-avail">В наличии</span>
                                        <button className="similar-products__item-favorite">
                                            <img src="/img/favorite.svg" alt="" />
                                        </button>
                                        <div className="similar-products__item-img">
                                            <img
                                                src={"/img/photos" + v.photoUrl.slice(1, v.photoUrl.length) + ".png"}
                                                alt="Фотография товара"
                                            />
                                        </div>
                                    </div>
                                    <p className="similar-products__item-price">{convertPrice(v.price)} ₽</p>
                                    <p className="similar-products__item-name">{v.name}</p>
                                    <Link to={"/article/" + v.id} className="similar-products__item-open">
                                        Подробнее
                                    </Link>
                                </div>
                            </SwiperSlide>
                        ))}
                    </Swiper>
                    <button className="similar-products__swiper-next" ref={nextBt}></button>
                </div>
            </div>
        </section>
    );
}

export default SimilarProducts;
