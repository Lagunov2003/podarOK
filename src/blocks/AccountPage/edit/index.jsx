import React, { useContext, useEffect, useState } from "react";
import "./style.scss";
import Button from "../../../component/button";
import { useNavigate } from "react-router";
import { ContextData, ContextLogin } from "../../../app/app";
import { responsePutProfile } from "../../../tool/response";

function Edit() {
    const navigate = useNavigate();
    const data = useContext(ContextData);
    const asyncLogin = useContext(ContextLogin)
    const [newData, setNewData] = useState({
        firstName: "",
        lastName: "",
        gender: true,
        dateOfBirth: null,
        phoneNumber: "",
        email: ""
    });

    useEffect(() => {
        setNewData({...data})
    }, [])

    useEffect(() => {
        console.log(newData);
        
    }, [newData])

    const handleChangeInputName = (e, type) => {
        let str = e.target.value;
        if (str != "" && str[str.length - 1].match(/[а-яА-Я]/)) {
            if (type == "f") {
                setNewData((v) => ({ ...v, firstName: str }));
            } else {
                setNewData((v) => ({ ...v, lastName: str }));
            }
        } else {
            e.target.value = str.slice(0, str.length - 1)
            if(str == "") {
                if (type == "f") {
                    setNewData((v) => ({ ...v, firstName: "" }));
                } else {
                    setNewData((v) => ({ ...v, lastName: "" }));
                }
            }
        }
    };

    const handleBlurInputName = (e, type) => {
        if (e.target.value != "") {
            const value = e.target.value[0].toUpperCase() + e.target.value.slice(1, e.target.value.length);
            if (type == "f") {
                setNewData((v) => ({ ...v, firstName: value }));
            } else {
                setNewData((v) => ({ ...v, lastName: value }));
            }
        }
    };

    const handleChangeDate = (e) => {
        setNewData((v) => ({ ...v, dateOfBirth: e.target.value }));
    };

    const handleChangeInputEmail = (e) => {
        let str = e.target.value;

        if (str != "" && str[str.length - 1].match(/[a-zA-Z@0-9._+-]/)) {
            const val = str.slice(0, str.length);
            setNewData((v) => ({ ...v, email: val }));
        } else {
            e.target.value = str.slice(0, str.length - 1)
            if(str == "") {
                setNewData((v) => ({ ...v, email: "" }));
            }
        }
    };

    const handleChangeInputTel = (e) => {
        let str = e.target.value;

        if (str != "" && str[str.length - 1].match(/[0-9]/)) {
            const val = str.slice(0, str.length);
            setNewData((v) => ({ ...v, phoneNumber: val }));
        } else {
            e.target.value = str.slice(0, str.length - 1)
            if (str == "") {
                setNewData((v) => ({ ...v, phoneNumber: "" }));
            }
        }
    };

    const handleChangeGender = (type) => {
        setNewData((v) => ({...v, gender: type}))
    }


    const handleSubmit = async (e) => {
        e.preventDefault();

        if(newData.firstName != "" && newData.email != "") {
            const status = await responsePutProfile(newData);
            if(status === 200) {
                await asyncLogin()
                navigate("/account");
            }
        }

    };

    return (
        <div className="edit">
            <form className="edit__content" id="editUser" onSubmit={(e) => handleSubmit(e)}>
                <h2 className="edit__title">Редактировать профиль</h2>
                <div className="edit__top">
                    <label className="edit__top-label edit__text">
                        Имя:
                        <input
                            type="text"
                            maxLength={15}
                            className="edit__input"
                            value={newData.firstName || ""}
                            onChange={(e) => handleChangeInputName(e, "f")}
                            onBlur={(e) => handleBlurInputName(e, "f")}
                        />
                    </label>
                    <label className="edit__top-label edit__text">
                        Фамилия:
                        <input
                            type="text"
                            maxLength={15}
                            className="edit__input"
                            value={newData.lastName || ""}
                            onChange={(e) => handleChangeInputName(e, "l")}
                            onBlur={(e) => handleBlurInputName(e, "l")}
                        />
                    </label>
                </div>
                <div className="edit__info">
                    <div className="edit__info-item">
                        <p className="edit__text">Дата Рождения:</p>
                        <input type="date" className="edit__input" max={new Date(new Date().getTime()).toISOString().split("T")[0]} value={newData.dateOfBirth} onChange={(e) => handleChangeDate(e)}/>
                    </div>
                    <div className="edit__info-item">
                        <p className="edit__text">Пол:</p>
                        <div className="edit__info-wrapper">
                            <label className="edit__text">
                                <input type="radio" name="gender" className="edit__radio" checked={newData.gender} onChange={() => handleChangeGender(true)}/>
                                Мужской
                            </label>
                            <label className="edit__text">
                                <input type="radio" name="gender" className="edit__radio" checked={!newData.gender} onChange={() => handleChangeGender(false)}/>
                                Женский
                            </label>
                        </div>
                    </div>
                    <div className="edit__info-item">
                        <p className="edit__text">Почта:</p>
                        <input type="email" className="edit__input" disabled maxLength={30} value={newData.email || ""} onChange={(e) => handleChangeInputEmail(e)} />
                    </div>
                    <div className="edit__info-item">
                        <p className="edit__text">Телефон:</p>
                        <input type="tel" className="edit__input" maxLength={11} value={newData.phoneNumber || ""} onChange={(e) => handleChangeInputTel(e)} />
                    </div>
                </div>
                <Button text="Сохранить" classNameText="edit__submit" typeButton="submit" idForm="editUser" />
            </form>
        </div>
    );
}

export default Edit;
