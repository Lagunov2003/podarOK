import React, { createContext, useContext, useEffect, useState } from "react";
import "./style.scss";
import { Outlet, useLocation, useNavigate, useParams } from "react-router";
import Header from "../header";
import SingIn from "../../../component/sing-in";
import Footer from "../footer";
import ChatHelper from "../../../component/chat-helper";
import WrapperModal from "../../../component/wrapper-modal";
import { ContextData } from "../../../app/app";
import LoadingCircle from "../../../component/loading-circle";

export const ModalAuth = createContext();

function LayerPage({ loading, chatAdmin }) {
    const { id } = useParams();
    const [activeModal, setActiveModal] = useState(false);
    const location = useLocation();
    const navigate = useNavigate();
    const data = useContext(ContextData);

    useEffect(() => {
        if (location.pathname === "/") document.title = "podarOK | Главная";
        else if (location.pathname === "/catalog") document.title = "podarOK | Каталог";
        else if (location.pathname.indexOf("/article") !== -1) document.title = "podarOK | Товар";
        else if (location.pathname === "/basket") document.title = "podarOK | Корзина";
        else if (location.pathname.indexOf("/account") !== -1) document.title = "podarOK | Кабинет";
        else if (location.pathname.indexOf("/admin") !== -1) document.title = "podarOK | Админ";
        else if (location.pathname.indexOf("/order") !== -1) document.title = "podarOK | Оформление";
        else document.title = "podarOK | Ошибка";
    }, [location]);

    const handleOpenModal = () => {
        document.body.classList.toggle("local-page");
        setActiveModal((v) => !v);
    };

    useEffect(() => {
        const scroll = sessionStorage.getItem("scroll");
        if (!scroll) window.scrollTo({ top: 0 });
    }, [location]);

    useEffect(() => {
        if (location.pathname.indexOf("/admin/chats/") !== -1 && id !== undefined && chatAdmin === null) {
            navigate("/admin/chats");
        }
    }, [location, chatAdmin, id, navigate]);

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
                    <LoadingCircle loading={loading}>
                        <main className="">
                            <Outlet />
                        </main>
                    </LoadingCircle>
                    <Footer handleOpenModal={handleOpenModal} />
                </div>
                <div className={"page-layer__bg" + (location.pathname.indexOf("admin") !== -1 ? " page-layer__bg_admin" : "")}></div>
                <ChatHelper />
            </div>
        </ModalAuth.Provider>
    );
}

export default LayerPage;
