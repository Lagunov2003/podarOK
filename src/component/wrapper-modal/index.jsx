import React, { useEffect, useState } from "react";
import "./style.scss";

function WrapperModal({ activeModal, children }) {
    const [active, setActive] = useState("")

    useEffect(() => {
        if(activeModal) {
            setActive("open")
        } else {
            setActive("close")
            setTimeout(() => {
                setActive("")
            }, 300)
        }
    }, [activeModal])

    return (
        <div className={"wrapper-modal " + (active !== "" ? `wrapper-modal_${active}` : "")}>
            <div className={"wrapper-modal__content"}>{children}</div>
        </div>
    );
}

export default WrapperModal;
