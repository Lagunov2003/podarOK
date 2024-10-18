import React from "react";
import "./style.scss"


function Button({ text = "", onClick = () => {}, classNameText = "", idForm = "", typeButton = "" }) {

    return <button onClick={() => onClick()} className={"button-style " + classNameText} type={typeButton} form={idForm}>
        {text}
    </button>
}

export default Button