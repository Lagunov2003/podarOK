import React, { useEffect, useRef, useState } from "react";
import "./style.scss";
import { convertImg, convertPrice } from "../../../tool/tool";
import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation } from "swiper/modules";
import { responseGetCart, responsePostCart } from "../../../tool/response";
import { useNavigate } from "react-router";

const items = ["Набор 3 шт", "Карамель-попкорн", "Синнабон", "Апельсин-корица"];

function Info({ item }) {
    const navigate = useNavigate();
    const [typeItem, setTypeItem] = useState(0);
    const [urlMainImg, setUrlMainImg] = useState();
    const [checkBasket, setCheckBasket] = useState(false);
    const numberCount = useRef(0);
    const prevBt = useRef(null);
    const nextBt = useRef(null);

    useEffect(() => {
        if (item) {
            const token = localStorage.getItem("token");

            setUrlMainImg(convertImg(item?.photos[0]?.photoUrl));

            let func = (list) => {
                console.log(item);
                if (list.filter((v) => v?.gift?.id == item?.id).length != 0) setCheckBasket(true);
            };

            if (token) responseGetCart(token, func);
        }
    }, [item]);

    const handleAddBasket = () => {
        const token = localStorage.getItem("token");

        if (token) {
            responsePostCart(token, item.id);
            setCheckBasket(true);
        }
    };

    const handleBuy = async () => {
        const token = localStorage.getItem("token");

        if (token) {
            responsePostCart(token, item.id);
            setCheckBasket(true);
            setTimeout(() => {
                navigate("/basket")
            }, 0)
        }
    };

    return (
        <section className="info">
            <div className="info__content">
                <div className="info__top">
                    <div className="info__top-left">
                        {[...(item?.categories || []), ...(item?.occasions || [])].map((v, i) => (
                            <span className="info__top-tag" key={i}>
                                {v.name}
                            </span>
                        ))}
                    </div>
                    <div className="info__top-right">
                        <span className="info__top-avail info__top-avail_active">Есть в наличии</span>
                        <div className="info__top-favorite">
                            <img src={"/img/favorite.svg"} alt="Добавить в избранное" />
                        </div>
                    </div>
                </div>
                <div className="info__block">
                    <div className="info__block-column">
                        <div className="info__block-wrapper">
                            <button className="info__block-prev" ref={prevBt}></button>
                            <div className="info__block-scroll">
                                <Swiper
                                    modules={[Navigation]}
                                    spaceBetween={12}
                                    slidesPerView={"auto"}
                                    direction="vertical"
                                    rewind="true"
                                    onInit={(swiper) => {
                                        swiper.params.navigation.prevEl = prevBt.current;
                                        swiper.params.navigation.nextEl = nextBt.current;
                                        swiper.navigation.init();
                                        swiper.navigation.update();
                                    }}
                                    onSlideChangeTransitionEnd={() => {
                                        if (item.photos)
                                            setUrlMainImg(
                                                "/img/photos" +
                                                    item.photos[numberCount.current].photoUrl.slice(
                                                        1,
                                                        item.photos[numberCount.current].photoUrl.length
                                                    ) +
                                                    ".png"
                                            );
                                    }}
                                >
                                    {item?.photos &&
                                        item?.photos?.map((v, i) => (
                                            <SwiperSlide key={i}>
                                                {({ isActive }) => {
                                                    if (isActive) numberCount.current = i;

                                                    return (
                                                        <div className="info__block-img">
                                                            <img
                                                                src={"/img/photos" + v.photoUrl.slice(1, v.photoUrl.length) + ".png"}
                                                                alt="Фотография товара"
                                                            />
                                                        </div>
                                                    );
                                                }}
                                            </SwiperSlide>
                                        ))}
                                </Swiper>
                            </div>
                            <button className="info__block-next" ref={nextBt}></button>
                        </div>
                        <div className="info__block-main-img">
                            <img src={urlMainImg} alt="Фотография товара" />
                        </div>
                    </div>
                    <div className="info__block-column">
                        <h2 className="info__block-name">{item?.name}</h2>
                        <div className="info__block-row">
                            <div className="info__block-rating">
                                <img src="/img/yellow-star.png" alt="Элемент оформления" />
                                <span className="info__block-text">4,7</span>
                            </div>
                            <span className="info__block-comment">6 отзывов</span>
                        </div>
                        <div className="info__block-list">
                            {item?.giftGroup != null && (
                                <>
                                    {items.map((v, i) => (
                                        <span
                                            className={"info__block-item" + (typeItem == i ? " info__block-item_active" : "")}
                                            key={i}
                                            onClick={() => setTypeItem(i)}
                                        >
                                            {v}
                                        </span>
                                    ))}
                                </>
                            )}
                        </div>
                        <h3 className="info__block-title">Коротко о товаре</h3>
                        <div className="info__block-descr">
                            {item?.features?.map((v, i) => (
                                <p className="info__block-text" key={i}>
                                    {v?.itemName}: {v?.itemValue}
                                </p>
                            ))}
                        </div>
                    </div>
                    <div className="info__block-column">
                        <div className="info__block-bg">
                            <p className="info__block-price">{convertPrice(item?.price || "")} ₽</p>
                            {checkBasket == true ? (
                                <button className="info__block-basket" onClick={() => navigate("/basket")}>
                                    Перейти в корзину
                                </button>
                            ) : (
                                <>
                                    <button className="info__block-add" onClick={() => handleAddBasket()}>
                                        Добавить в корзину
                                    </button>
                                    <button className="info__block-buy" onClick={() => handleBuy()}>
                                        Купить
                                    </button>
                                </>
                            )}
                        </div>
                        <h3 className="info__block-title">Описание товара</h3>
                        <p className="info__block-text-small">{item?.description}</p>
                    </div>
                </div>
            </div>
        </section>
    );
}

export default Info;
