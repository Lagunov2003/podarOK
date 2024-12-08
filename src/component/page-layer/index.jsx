import React, { useEffect, useState } from "react";
import "./style.scss";
import { Outlet, useLocation } from "react-router";
import Header from "../../blocks/LayerPage/header";
import SingIn from "../sing-in";
import Footer from "../../blocks/LayerPage/footer";
import ChatHelper from "../chat-helper";
import { responseGetProfile } from "../../tool/response";
import WrapperModal from "../wrapper-modal";

function PageLayer() {
    const [activeModal, setActiveModal] = useState(false);
    const location = useLocation();
    const [data, setData] = useState();
    useEffect(() => {
        document.title = "podarOK | Главная";

        const asyncFunc = async () => {
            const token = localStorage.getItem("token")

            if(token) {
                let d = await responseGetProfile(token)
                setData(d)
                console.log(d);
                
            }
        }

        asyncFunc()
    }, []);

    const handleOpenModal = () => {
        document.body.classList.toggle("local-page");
        setActiveModal((v) => !v);
    };

    useEffect(() => {
        const scroll = sessionStorage.getItem("scroll");
        if (!scroll) window.scrollTo({ top: 0 });
    }, [location.pathname]);

    return (
        <div className="page-layer">
            <div className="page-layer__wrapper">
                <div className="page-layer__wrapper-header">
                    <Header handleOpenModal={handleOpenModal} data={data}/>
                    <WrapperModal activeModal={activeModal}>
                        <SingIn openModal={handleOpenModal} />
                    </WrapperModal>
                </div>
                <main className="">
                    <Outlet />
                </main>
                <Footer />
            </div>
            <div className="page-layer__bg">
                <img src={"/img/circle.jpg"} alt="" />
            </div>
            <ChatHelper />
        </div>
    );
}

export default PageLayer;
