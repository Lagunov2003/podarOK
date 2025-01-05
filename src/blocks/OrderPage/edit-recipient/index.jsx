import React, { useEffect, useState } from "react";
import "./style.scss";

function EditRecipient({ closeModalRecipient, recipient, setDataOrder }) {
    const [dataRecipient, setDataRecipient] = useState({});

    useEffect(() => {
        setDataRecipient({...recipient})
    }, [recipient])


    const handleSave = () => {
        console.log(dataRecipient);
        setDataOrder((v) => ({ ...v, recipient: {...dataRecipient} }));
        closeModalRecipient(false);
    };

    const handleChangeInputName = (e) => {
        let str = e.target.value;
        
        if (str === "" || str[str.length - 1].match(/[а-яА-Я]/)) {    
            const val = str.slice(0, str.length)
            setDataRecipient(v => ({...v, name: val}))
        }
    };

    const handleBlurInputName = (e) => {
        if (e.target.value !== "") {
            setDataRecipient(v => ({...v, name: v.name[0].toUpperCase() + v.name.slice(1, e.target.value.length)}));
        }
    };

    const handleChangeInputEmail = (e) => {
        let str = e.target.value;
        
        if (str === "" || str[str.length - 1].match(/[a-zA-Z@0-9._+-]/)) {    
            const val = str.slice(0, str.length)
            setDataRecipient(v => ({...v, email: val}))
        }
    };

    const handleChangeInputTel = (e) => {
        let str = e.target.value;
        
        if (str === "" || str[str.length - 1].match(/[0-9]/)) {    
            const val = str.slice(0, str.length)
            setDataRecipient(v => ({...v, tel: val}))
        }
    };
    

    return (
        <div className="edit-recipient">
            <h2 className="edit-recipient__title">Редактировать данные получателя</h2>
            <div className="edit-recipient__list">
                <label className="edit-recipient__field">
                    Имя и фамилия
                    <input
                        type="text"
                        name="nameEdit"
                        maxLength={40}
                        value={dataRecipient.name || ""}
                        onChange={(e) => handleChangeInputName(e)}
                        onBlur={(e) => handleBlurInputName(e)}
                    />
                </label>
                <label className="edit-recipient__field">
                    Почта
                    <input
                        name="emailEdit"
                        type="email"
                        maxLength={40}
                        value={dataRecipient.email || ""}
                        onChange={(e) => handleChangeInputEmail(e)}
                    />
                </label>
                <label className="edit-recipient__field">
                    Телефон
                    <input
                        type="tel"
                        name="telEdit"
                        maxLength={11}
                        value={dataRecipient.tel || ""}
                        onChange={(e) => handleChangeInputTel(e)}
                    />
                </label>
            </div>
            <button className="edit-recipient__save" onClick={() => handleSave()}>
                Готово
            </button>
            <button className="edit-recipient__close" onClick={() => closeModalRecipient(false)}>
                <svg width="23.000000" height="23.000000" viewBox="0 0 23 23" fill="none">
                    <path
                        id="Vector-ttt"
                        d="M13.52 11.49L22.57 2.46C22.84 2.19 22.99 1.82 22.99 1.44C22.99 1.05 22.84 0.69 22.57 0.42C22.3 0.15 21.93 0 21.55 0C21.17 0 20.8 0.15 20.53 0.42L11.5 9.47L2.46 0.42C2.19 0.15 1.82 0 1.44 0C1.06 0 0.69 0.15 0.42 0.42C0.15 0.69 0 1.05 0 1.44C0 1.82 0.15 2.19 0.42 2.46L9.47 11.49L0.42 20.53C0.29 20.66 0.18 20.82 0.11 21C0.03 21.17 0 21.36 0 21.55C0 21.74 0.03 21.93 0.11 22.1C0.18 22.28 0.29 22.44 0.42 22.57C0.55 22.7 0.71 22.81 0.89 22.88C1.06 22.96 1.25 23 1.44 23C1.63 23 1.82 22.96 1.99 22.88C2.17 22.81 2.33 22.7 2.46 22.57L11.5 13.52L20.53 22.57C20.66 22.7 20.82 22.81 21 22.88C21.17 22.96 21.36 23 21.55 23C21.74 23 21.93 22.96 22.1 22.88C22.28 22.81 22.44 22.7 22.57 22.57C22.7 22.44 22.81 22.28 22.88 22.1C22.96 21.93 23 21.74 23 21.55C23 21.36 22.96 21.17 22.88 21C22.81 20.82 22.7 20.66 22.57 20.53L13.52 11.49Z"
                        fill="#000"
                    />
                </svg>
            </button>
        </div>
    );
}

export default EditRecipient;
