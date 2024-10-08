import React from "react";
import "./style.scss"


function Button({ text = "", onClick = () => {}, classNameText = "" }) {

    return <button onClick={() => onClick()} className={"button-style " + classNameText}>
        {text}
    </button>
}

export default Button