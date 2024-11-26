import React, { useRef } from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation } from "swiper/modules";
import "swiper/css";
import "./style.scss";

function SimilarProducts() {
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
                        slidesPerView={4}
                        onInit={(swiper) => {
                            swiper.params.navigation.prevEl = prevBt.current;
                            swiper.params.navigation.nextEl = nextBt.current;
                            swiper.navigation.init();
                            swiper.navigation.update();
                        }}
                    >
                        <SwiperSlide>
                            <div className="similar-products__item">
                                <div className="similar-products__item-img">
                                    <span className="similar-products__item-avail">В наличии</span>
                                    <button className="similar-products__item-favorite">
                                        <img src="/img/favorite.svg" alt="" />
                                    </button>
                                    <img src="" alt="" />
                                </div>
                                <p className="similar-products__item-price">1199 ₽</p>
                                <p className="similar-products__item-name">Свечи набор 6 штук</p>
                                <button className="similar-products__item-open">Подробнее</button>
                            </div>
                        </SwiperSlide>
                        <SwiperSlide>
                            <div className="similar-products__item">
                                <div className="similar-products__item-img">
                                    <span className="similar-products__item-avail">В наличии</span>
                                    <button className="similar-products__item-favorite">
                                        <img src="/img/favorite.svg" alt="" />
                                    </button>
                                    <img src="" alt="" />
                                </div>
                                <p className="similar-products__item-price">1199 ₽</p>
                                <p className="similar-products__item-name">Свечи набор 6 штук</p>
                                <button className="similar-products__item-open">Подробнее</button>
                            </div>
                        </SwiperSlide>
                        <SwiperSlide>
                            <div className="similar-products__item">
                                <div className="similar-products__item-img">
                                    <span className="similar-products__item-avail">В наличии</span>
                                    <button className="similar-products__item-favorite">
                                        <img src="/img/favorite.svg" alt="" />
                                    </button>
                                    <img src="" alt="" />
                                </div>
                                <p className="similar-products__item-price">1199 ₽</p>
                                <p className="similar-products__item-name">Свечи набор 6 штук</p>
                                <button className="similar-products__item-open">Подробнее</button>
                            </div>
                        </SwiperSlide>
                        <SwiperSlide>
                            <div className="similar-products__item">
                                <div className="similar-products__item-img">
                                    <span className="similar-products__item-avail">В наличии</span>
                                    <button className="similar-products__item-favorite">
                                        <img src="/img/favorite.svg" alt="" />
                                    </button>
                                    <img src="" alt="" />
                                </div>
                                <p className="similar-products__item-price">1199 ₽</p>
                                <p className="similar-products__item-name">Свечи набор 6 штук</p>
                                <button className="similar-products__item-open">Подробнее</button>
                            </div>
                        </SwiperSlide>
                        <SwiperSlide>
                            <div className="similar-products__item">
                                <div className="similar-products__item-img">
                                    <span className="similar-products__item-avail">В наличии</span>
                                    <button className="similar-products__item-favorite">
                                        <img src="/img/favorite.svg" alt="" />
                                    </button>
                                    <img src="" alt="" />
                                </div>
                                <p className="similar-products__item-price">1199 ₽</p>
                                <p className="similar-products__item-name">Свечи набор 6 штук</p>
                                <button className="similar-products__item-open">Подробнее</button>
                            </div>
                        </SwiperSlide>
                        <SwiperSlide>
                            <div className="similar-products__item">
                                <div className="similar-products__item-img">
                                    <span className="similar-products__item-avail">В наличии</span>
                                    <button className="similar-products__item-favorite">
                                        <img src="/img/favorite.svg" alt="" />
                                    </button>
                                    <img src="" alt="" />
                                </div>
                                <p className="similar-products__item-price">1199 ₽</p>
                                <p className="similar-products__item-name">Свечи набор 6 штук</p>
                                <button className="similar-products__item-open">Подробнее</button>
                            </div>
                        </SwiperSlide>
                    </Swiper>
                    <button className="similar-products__swiper-next" ref={nextBt}></button>
                </div>
            </div>
        </section>
    );
}

export default SimilarProducts;
