import React, { useEffect, useState } from "react";
import img from "../../../public/img/circle.jpg";
import "./style.scss";
import { Outlet } from "react-router";
import Header from "../../blocks/LayerPage/header";
import SingIn from "../sing-in";
import Footer from "../../blocks/LayerPage/footer";

function PageLayer() {
    const [activeModal, setActiveModal] = useState(false);

    useEffect(() => {
        document.title = "podarOK | Главная";
    }, []);

    const handleOpenModal = () => {
        document.body.classList.toggle("local-page");
        setActiveModal((v) => !v);
    };

    return (
        <div className="page-layer">
            <div className="page-layer__wrapper">
                <div className="page-layer__wrapper-header">
                    <Header handleOpenModal={handleOpenModal} />
                    <SingIn openModal={handleOpenModal} activeModal={activeModal} />
                </div>
                <div className="">
                    <Outlet />
                </div>
                <Footer />
            </div>
            <div className="page-layer__bg">
                <img src={img} alt="" />
            </div>
        </div>
    );
}

export default PageLayer;
