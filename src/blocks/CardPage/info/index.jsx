import React, { useContext, useEffect, useRef, useState } from "react";
import "./style.scss";
import { convertImg, convertPrice } from "../../../tool/tool";
import { Swiper, SwiperSlide, useSwiper } from "swiper/react";
import { Navigation } from "swiper/modules";
import { responseDeleteFromFavorites, responseGetCart, responsePostAddToFavorites, responsePostCart } from "../../../tool/response";
import { useNavigate, useParams } from "react-router";
import { ModalAuth } from "../../LayerPage/layer-page";

const items = ["Набор 3 шт", "Карамель-попкорн", "Синнабон", "Апельсин-корица"];

const convertWord = (number) => {
    switch (number) {
        case 1:
            return "отзыв";
        case 2:
        case 3:
        case 4:
            return "отзыва";
        default:
            return "отзывов";
    }
};

function Info({ item, reviewsAmount, averageRating, isFavorite }) {
    const { id } = useParams();
    const navigate = useNavigate();
    const handleOpenModal = useContext(ModalAuth);
    const [typeItem, setTypeItem] = useState(0);
    const [urlMainImg, setUrlMainImg] = useState();
    const [checkBasket, setCheckBasket] = useState(false);
    const [numberSlide, setNumberSlide] = useState(0);
    const [favorite, setFavorite] = useState(isFavorite);
    const prevBt = useRef(null);
    const nextBt = useRef(null);
    const refSwiper = useRef(null);

    useEffect(() => {
        setFavorite(isFavorite);
    }, [isFavorite]);

    useEffect(() => {
        if (item) {
            const token = localStorage.getItem("token");

            setNumberSlide(0);
            setUrlMainImg(convertImg(item?.photos[0]?.photoUrl));

            let func = (list) => {
                if (list.filter((v) => v?.gift?.id == item?.id).length != 0) setCheckBasket(true);
            };

            if (token) responseGetCart(token, func);
        }
    }, [item]);

    useEffect(() => {
        refSwiper.current.slideTo(numberSlide);
        if (item?.photos) setUrlMainImg(convertImg(item.photos[numberSlide].photoUrl));
    }, [numberSlide]);

    const handleChangeSlide = (type) => {
        if (type == "add" && numberSlide < item?.photos?.length - 1) {
            setNumberSlide((v) => v + 1);
        } else if (type == "sub" && numberSlide >= 1) {
            setNumberSlide((v) => v - 1);
        }
    };

    const handleAddBasket = () => {
        const token = localStorage.getItem("token");

        if (token) {
            responsePostCart(token, item.id);
            setCheckBasket(true);
        } else {
            handleOpenModal();
        }
    };

    const handleBuy = async () => {
        const token = localStorage.getItem("token");

        if (token) {
            await responsePostCart(token, item.id);
            setCheckBasket(true);
            setTimeout(() => {
                navigate("/basket");
            }, 0);
        } else {
            handleOpenModal();
        }
    };

    const handleFavorite = async () => {
        const token = localStorage.getItem("token");
        
        if (token) {
            setFavorite((v) => !v);
            favorite == true ? await responseDeleteFromFavorites(id) : await responsePostAddToFavorites(id);
        } else {
            handleOpenModal();
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
                        <div className="info__top-favorite" onClick={() => handleFavorite()}>
                            <img
                                src={favorite == true ? "/img/favorite-purple-fill.svg" : "/img/favorite.svg"}
                                alt="Добавить в избранное"
                            />
                        </div>
                    </div>
                </div>
                <div className="info__block">
                    <div className="info__block-column">
                        <div className="info__block-sticky">
                            <div className="info__block-wrapper">
                                <button className="info__block-prev" onClick={() => handleChangeSlide("sub")}>
                                    <img src={"/img/white-button-arrow.svg"} alt="Кнопка" />
                                </button>
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
                                        onSwiper={(swiper) => (refSwiper.current = swiper)}
                                    >
                                        {item?.photos?.length != 0 && (
                                            <>
                                                {item?.photos?.map((v, i) => (
                                                    <SwiperSlide key={i}>
                                                        <div className="info__block-img" onClick={() => setNumberSlide(i)}>
                                                            <img
                                                                src={"/img/photos" + v.photoUrl.slice(1, v.photoUrl.length) + ".png"}
                                                                alt="Фотография товара"
                                                            />
                                                        </div>
                                                    </SwiperSlide>
                                                ))}
                                            </>
                                        )}
                                    </Swiper>
                                </div>
                                <button className="info__block-next" onClick={() => handleChangeSlide("add")}>
                                    <img src={"/img/white-button-arrow.svg"} alt="Кнопка" />
                                </button>
                            </div>
                            <div className="info__block-main-img">
                                <img src={urlMainImg} alt="Фотография товара" />
                            </div>
                        </div>
                    </div>
                    <div className="info__block-column">
                        <div className="info__block-row">
                            <div className="info__block-left">
                                <h2 className="info__block-name">{item?.name}</h2>
                                <div className="info__block-rating">
                                    {averageRating && (
                                        <div className="info__block-rating-img">
                                            <img src="/img/yellow-star.png" alt="Элемент оформления" />
                                            <span className="info__block-text">
                                                {averageRating?.toString()?.indexOf(".") != -1 ? averageRating : averageRating + ",0"}
                                            </span>
                                        </div>
                                    )}
                                    <span className="info__block-comment">
                                        {reviewsAmount || 0} {convertWord(reviewsAmount || 0)}
                                    </span>
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
                            </div>
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
                        </div>
                        <div className="info__block-row">
                            <div className="info__block-left">
                                <h3 className="info__block-title">Характеристики</h3>
                                <div className="info__block-descr">
                                    {item?.features?.map((v, i) => (
                                        <p className="info__block-text" key={i}>
                                            {v?.itemName}: {v?.itemValue}
                                        </p>
                                    ))}
                                </div>
                            </div>
                            <div className="info__block-right">
                                <h3 className="info__block-title">Описание товара</h3>
                                <p className="info__block-text-small">{item?.description}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    );
}

export default Info;
