import React, { useContext, useRef, useState } from "react";
import "./style.scss";
import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation } from "swiper/modules";
import "swiper/css";
import WrapperModal from "../../../component/wrapper-modal";
import ReviewModal from "../../../component/review-modal";
import SingIn from "../../../component/sing-in";
import { ContextData } from "../../../app/app";
import { ModalAuth } from "../../LayerPage/layer-page";

function Reviews({ refReviews }) {
    const data = useContext(ContextData);
    const handleOpenModal = useContext(ModalAuth)
    const [open, setOpen] = useState(false);
    const prevBt = useRef(null);
    const nextBt = useRef(null);

    const handleOpen = () => {
        if(data == null) {
            handleOpenModal()
        } else {
            document.body.classList.toggle("local-page");
            setOpen(v => !v)
        }
    }

    return (
        <section className="reviews" ref={refReviews}>
            <div className="reviews__content padding-style">
                <h2 className="reviews__title title-style">Отзывы</h2>
                <div className="reviews__swiper">
                    <button className="reviews__swiper-prev" ref={prevBt}></button>
                    <Swiper
                        modules={[Navigation]}
                        spaceBetween={21}
                        slidesPerView={3}
                        onInit={(swiper) => {
                            swiper.params.navigation.prevEl = prevBt.current;
                            swiper.params.navigation.nextEl = nextBt.current;
                            swiper.navigation.init();
                            swiper.navigation.update();
                        }}
                    >
                        <SwiperSlide>
                            <div className="reviews__item">
                                <p className="reviews__item-author">
                                    Игнат <span>☆☆☆☆☆</span>
                                </p>
                                <p className="reviews__item-text">
                                    Замечательный подбор подарков, подошло все идеально, друг очень был рад, доставка вовремя!
                                </p>
                            </div>
                        </SwiperSlide>
                        <SwiperSlide>
                            <div className="reviews__item">
                                <p className="reviews__item-author">
                                    Алёна <span>☆☆☆☆</span>☆
                                </p>
                                <p className="reviews__item-text">
                                    Прекрасный сервис подбора подарков. Нужно было привезти день в день, было доставлено все в срок.
                                    Аккуратно упаковали, бережно довезли до квартиры
                                </p>
                            </div>
                        </SwiperSlide>
                        <SwiperSlide>
                            <div className="reviews__item">
                                <p className="reviews__item-author">
                                    Марат <span>☆☆☆☆☆</span>
                                </p>
                                <p className="reviews__item-text">
                                    Все супер-пупер, были нюансы по доставке, обращался в чат поддержки, спасибо терпеливому администратору,
                                    помог решить все вопросы.
                                </p>
                            </div>
                        </SwiperSlide>
                        <SwiperSlide>
                            <div className="reviews__item">
                                <p className="reviews__item-author">
                                    Игнат <span>☆☆☆☆☆</span>
                                </p>
                                <p className="reviews__item-text">
                                    Замечательный подбор подарков, подошло все идеально, друг очень был рад, доставка вовремя!
                                </p>
                            </div>
                        </SwiperSlide>
                        <SwiperSlide>
                            <div className="reviews__item">
                                <p className="reviews__item-author">
                                    Алёна <span>☆☆☆☆</span>☆
                                </p>
                                <p className="reviews__item-text">
                                    Прекрасный сервис подбора подарков. Нужно было привезти день в день, было доставлено все в срок.
                                    Аккуратно упаковали, бережно довезли до квартиры
                                </p>
                            </div>
                        </SwiperSlide>
                        <SwiperSlide>
                            <div className="reviews__item">
                                <p className="reviews__item-author">
                                    Марат <span>☆☆☆☆☆</span>
                                </p>
                                <p className="reviews__item-text">
                                    Все супер-пупер, были нюансы по доставке, обращался в чат поддержки, спасибо терпеливому администратору,
                                    помог решить все вопросы.
                                </p>
                            </div>
                        </SwiperSlide>
                    </Swiper>
                    <button className="reviews__swiper-next" ref={nextBt}></button>
                </div>
                <p className="reviews__label">Последние отзывы о нашем сервисе</p>
                <button className="reviews__button button-style" onClick={() => handleOpen()}>
                    Оставить отзыв
                </button>
                <WrapperModal activeModal={open}>
                    <ReviewModal handleOpen={handleOpen} />
                </WrapperModal>
            </div>
        </section>
    );
}

export default Reviews;
