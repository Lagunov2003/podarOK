import React from "react";
import "./style.scss"
import Button from "../button";

function InfoPassword({ handleOpen }) {


    return <div className="info-password">
        <p className="info-password__text">Каким должен быть пароль?</p>
        <ul className="info-password__list">
            <li>
                <p className="info-password__item">от 6 до 12 букв</p>
            </li>
            <li>
                <p className="info-password__item">только латиница (eng)</p>
            </li>
            <li>
                <p className="info-password__item">Нельзя использовать цифры и другие символы</p>
            </li>
        </ul>
        <Button text="ок" classNameText="info-password__button" onClick={() => handleOpen()}/>
    </div>
}

export default InfoPassword