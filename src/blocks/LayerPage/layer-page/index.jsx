import React, { createContext, useContext, useEffect, useState } from "react";
import "./style.scss";
import { Outlet, useLocation } from "react-router";
import Header from "../header";
import SingIn from "../../../component/sing-in";
import Footer from "../footer";
import ChatHelper from "../../../component/chat-helper";
import WrapperModal from "../../../component/wrapper-modal";
import { ContextData } from "../../../app/app";

export const ModalAuth = createContext();

function LayerPage() {
    const [activeModal, setActiveModal] = useState(false);
    const location = useLocation();
    const data = useContext(ContextData);

    useEffect(() => {
        document.title = "podarOK | Главная";
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
        <ModalAuth.Provider value={handleOpenModal}>
            <div className="page-layer">
                <div className="page-layer__wrapper">
                    <div className="page-layer__wrapper-header">
                        <Header handleOpenModal={handleOpenModal} data={data} />
                        <WrapperModal activeModal={activeModal}>
                            <SingIn openModal={handleOpenModal} />
                        </WrapperModal>
                    </div>
                    <main className="">
                        <Outlet />
                    </main>
                    <Footer handleOpenModal={handleOpenModal}/>
                </div>
                <div className={"page-layer__bg" + (location.pathname.indexOf("admin") != -1 ? " page-layer__bg_admin" : "")}>
                </div>
                <ChatHelper />
            </div>
        </ModalAuth.Provider>
    );
}

export default LayerPage;
