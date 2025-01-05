import React, { useState } from "react";
import "./style.scss";
import { Map, YMaps } from "@pbe/react-yandex-maps";
import { getAdressFromCoordinates } from "../../../tool/response";

function EditAddress({ closeModalAddress, setAcceptAddress, setDataOrder }) {
    const [address, setAddress] = useState({
        address: "",
        home: "",
        entrance: "",
        floor: "",
        flat: "",
    });
    const [comment, setComment] = useState("");

    const handleClickMap = async (e) => {
        let coords = e.get("coords");
        const newAddress = await getAdressFromCoordinates(coords[0], coords[1]);
        setAddress((v) => ({ ...v, address: newAddress }));
    };

    const handleSave = () => {
        let price = 0;
        if (address.address !== "" && address.home !== "" && address.entrance !== "" && address.floor !== "" && address.flat !== "") {
            if (address.address.indexOf("Ярославль") !== -1) {
                price = 100;
            } else if (address.address.indexOf("Ярославская область") !== -1) {
                price = 200;
            } else {
                price = 300;
            }

            closeModalAddress(false);
            setAcceptAddress(true);
            let strAddress = address.address;

            for (let x in address) {
                if (address[x] !== "" && x !== "address") {
                    switch (x) {
                        case "flat": {
                            strAddress += ", кв. " + address[x];
                            break;
                        }
                        case "floor": {
                            strAddress += ", " + address[x] + " этаж";
                            break;
                        }
                        case "home": {
                            strAddress += ", д. " + address[x];
                            break;
                        }
                        case "entrance": {
                            strAddress += ", " + address[x] + " подъезд";
                            break;
                        }
                        default:
                            break
                    }
                }
            }

            console.log(strAddress);
            setDataOrder((v) => {
                let pr = price;
                if (v.fastDelivery === true) {
                    pr += 250;
                }
                return { ...v, address: strAddress, addressPrice: pr, comment: comment };
            });
        }
    };

    const handleChangeField = (e) => {
        const { value } = e.target;

        if (value !== "" && !value[value.length - 1].match(/[0-9]/)) {
            e.target.value = value.slice(0, value.length - 1);
        }
    };

    return (
        <div className="edit-address">
            <h2 className="edit-address__title">Укажите куда вам доставить заказ</h2>
            <div className="edit-address__block">
                <div className="edit-address__info">
                    <label className="edit-address__field">
                        Адрес
                        <input
                            type="text"
                            name="address"
                            value={address.address}
                            onChange={(e) => setAddress((v) => ({ ...v, address: e.target.value }))}
                        />
                    </label>
                    <div className="edit-address__row">
                        <label className="edit-address__field">
                            Дом, корпус
                            <input
                                type="text"
                                name="home"
                                value={address.home}
                                maxLength={5}
                                onChange={(e) => {
                                    handleChangeField(e);
                                    setAddress((v) => ({ ...v, home: e.target.value }));
                                }}
                            />
                        </label>
                        <label className="edit-address__field">
                            Подъезд
                            <input
                                type="text"
                                name="entrance"
                                maxLength={5}
                                value={address.entrance}
                                onChange={(e) => {
                                    handleChangeField(e);
                                    setAddress((v) => ({ ...v, entrance: e.target.value }));
                                }}
                            />
                        </label>
                    </div>
                    <div className="edit-address__row">
                        <label className="edit-address__field">
                            Этаж
                            <input
                                type="text"
                                name="floor"
                                maxLength={5}
                                value={address.floor}
                                onChange={(e) => {
                                    handleChangeField(e);
                                    setAddress((v) => ({ ...v, floor: e.target.value }));
                                }}
                            />
                        </label>
                        <label className="edit-address__field">
                            Квартира
                            <input
                                type="text"
                                name="flat"
                                maxLength={5}
                                value={address.flat}
                                onChange={(e) => {
                                    handleChangeField(e);
                                    setAddress((v) => ({ ...v, flat: e.target.value }));
                                }}
                            />
                        </label>
                    </div>
                    <label className="edit-address__field">
                        Комментарий курьеру
                        <textarea name="" id="" value={comment} onChange={(e) => setComment(e.target.value)} />
                    </label>
                    <button
                        className="edit-address__save"
                        onClick={() => {
                            handleSave();
                        }}
                    >
                        Готово
                    </button>
                    <button className="edit-address__close" onClick={() => closeModalAddress(false)}>
                        <svg width="23.000000" height="23.000000" viewBox="0 0 23 23" fill="none">
                            <path
                                id="Vector-ttt"
                                d="M13.52 11.49L22.57 2.46C22.84 2.19 22.99 1.82 22.99 1.44C22.99 1.05 22.84 0.69 22.57 0.42C22.3 0.15 21.93 0 21.55 0C21.17 0 20.8 0.15 20.53 0.42L11.5 9.47L2.46 0.42C2.19 0.15 1.82 0 1.44 0C1.06 0 0.69 0.15 0.42 0.42C0.15 0.69 0 1.05 0 1.44C0 1.82 0.15 2.19 0.42 2.46L9.47 11.49L0.42 20.53C0.29 20.66 0.18 20.82 0.11 21C0.03 21.17 0 21.36 0 21.55C0 21.74 0.03 21.93 0.11 22.1C0.18 22.28 0.29 22.44 0.42 22.57C0.55 22.7 0.71 22.81 0.89 22.88C1.06 22.96 1.25 23 1.44 23C1.63 23 1.82 22.96 1.99 22.88C2.17 22.81 2.33 22.7 2.46 22.57L11.5 13.52L20.53 22.57C20.66 22.7 20.82 22.81 21 22.88C21.17 22.96 21.36 23 21.55 23C21.74 23 21.93 22.96 22.1 22.88C22.28 22.81 22.44 22.7 22.57 22.57C22.7 22.44 22.81 22.28 22.88 22.1C22.96 21.93 23 21.74 23 21.55C23 21.36 22.96 21.17 22.88 21C22.81 20.82 22.7 20.66 22.57 20.53L13.52 11.49Z"
                                fill="#000"
                            />
                        </svg>
                    </button>
                </div>
                <YMaps>
                    <div className="edit-address__map">
                        <Map
                            defaultState={{ center: [57.62, 39.88], zoom: 13 }}
                            height={533}
                            width={585}
                            onClick={(e) => handleClickMap(e)}
                        ></Map>
                    </div>
                </YMaps>
            </div>
        </div>
    );
}

export default EditAddress;
