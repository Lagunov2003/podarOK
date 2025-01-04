import React, { useContext, useEffect, useRef, useState } from "react";
import "./style.scss";
import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation } from "swiper/modules";
import "swiper/css";
import WrapperModal from "../../../component/wrapper-modal";
import ReviewModal from "../../../component/review-modal";
import SingIn from "../../../component/sing-in";
import { ContextData } from "../../../app/app";
import { ModalAuth } from "../../LayerPage/layer-page";
import { responseGetSiteReviews } from "../../../tool/response";

const stars = new Array(5).fill(0);

function Reviews({ refReviews }) {
    const data = useContext(ContextData);
    const handleOpenModal = useContext(ModalAuth);
    const [open, setOpen] = useState(false);
    const [list, setList] = useState([]);
    const prevBt = useRef(null);
    const nextBt = useRef(null);

    useEffect(() => {
        (async () => {
            await responseGetSiteReviews(setList);
        })();
    }, []);

    useEffect(() => {
        console.log(list);
    }, [list]);

    const handleOpen = () => {
        if (data == null) {
            handleOpenModal();
        } else {
            document.body.classList.toggle("local-page");
            setOpen((v) => !v);
        }
    };

    return (
        <section className="reviews" ref={refReviews}>
            <div className="reviews__content padding-style">
                <h2 className="reviews__title title-style">Отзывы</h2>
                <div className="reviews__swiper">
                    <button className="reviews__swiper-prev" ref={prevBt}></button>
                    <Swiper
                        modules={[Navigation]}
                        spaceBetween={21}
                        slidesPerView={"auto"}
                        onInit={(swiper) => {
                            swiper.params.navigation.prevEl = prevBt.current;
                            swiper.params.navigation.nextEl = nextBt.current;
                            swiper.navigation.init();
                            swiper.navigation.update();
                        }}
                    >
                        {list?.map((v, i) => (
                            <SwiperSlide key={i}>
                                <div className="reviews__item">
                                    <p className="reviews__item-author">
                                        {v.userName}{" "}
                                        <span>
                                            {stars.map((_, i) => (
                                                <img
                                                    key={i}
                                                    src={v.mark >= i + 1 ? "/img/rating-star.svg" : "/img/rating-star-empty.svg"}
                                                    alt="Элемент оформления"
                                                />
                                            ))}
                                        </span>
                                    </p>
                                    <p className="reviews__item-text">
                                        Замечательный подбор подарков, подошло все идеально, друг очень был рад, доставка вовремя!
                                    </p>
                                </div>
                            </SwiperSlide>
                        ))}
                    </Swiper>
                    <button className="reviews__swiper-next" ref={nextBt}></button>
                </div>
                <p className="reviews__label">Последние отзывы о нашем сервисе</p>
                {list.filter((v) => v?.userId == data?.id)?.length != 1 && (
                    <button className="reviews__button button-style" onClick={() => handleOpen()}>
                        Оставить отзыв
                    </button>
                )}
                <WrapperModal activeModal={open}>
                    <ReviewModal handleOpen={handleOpen} />
                </WrapperModal>
            </div>
        </section>
    );
}

export default Reviews;
