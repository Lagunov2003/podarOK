import React, { createContext, useEffect, useRef, useState } from "react";
import Main from "./main";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import PasswordCange from "./password";
import Account from "./account";
import User from "../blocks/AccountPage/user";
import Setting from "../blocks/AccountPage/setting";
import Edit from "../blocks/AccountPage/edit";
import Notice from "../blocks/AccountPage/notice";
import ChatHelper from "../blocks/AccountPage/chat-helper";
import Favorite from "../blocks/AccountPage/favorite";
import Catalog from "./catalog";
import Basket from "./basket";
import OrderAccount from "../blocks/AccountPage/order";
import Card from "./card";
import Order from "./order";
import Admin from "./admin/admin";
import AdminReviews from "../blocks/AdminPage/admin-reviews";
import AdminOrders from "../blocks/AdminPage/admin-orders";
import LayerPage from "../blocks/LayerPage/layer-page";
import { responseGetProfile } from "../tool/response";
import ErrorPage from "../blocks/ErrorPage";
import AdminChats from "../blocks/AdminPage/admin-chats";
import AdminItemChat from "../blocks/AdminPage/admin-item-chat";
import { decoderToken } from "../tool/tool";

export const ContextData = createContext();
export const ContextLogin = createContext();
export const ContextSurvey = createContext();

function App() {
    const [data, setData] = useState(null);
    const [order, setOrder] = useState(null);
    const [survey, setSurvey] = useState(null);

    const asyncLogin = async () => {
        const token = localStorage.getItem("token");
        if (token) {
            let d = await responseGetProfile(token);
            let role = await decoderToken(token);
            setData({ ...d, role });
        } else {
            setData(null);
        }
    };

    useEffect(() => {
        asyncLogin();
    }, []);

    return (
        <ContextData.Provider value={data}>
            <ContextLogin.Provider value={asyncLogin}>
                <ContextSurvey.Provider value={{ survey: survey, setSurvey: setSurvey }}>
                    <BrowserRouter>
                        <Routes>
                            <Route path="/" element={<LayerPage />}>
                                <Route index element={<Main />} />
                                <Route path="catalog" element={<Catalog />} />
                                <Route path="article/:id" element={<Card />} />
                                <Route path="resetPassword" element={<PasswordCange />} />
                                <Route path="confirmChanges" element={<PasswordCange />} />
                                {data != null && (
                                    <>
                                        {data.role == false && (
                                            <>
                                                <Route path="order/:id" element={<Order order={order} />} />
                                                <Route path="basket" element={<Basket setOrder={setOrder} />} />
                                                <Route path="account" element={<Account />}>
                                                    <Route index element={<User />} />
                                                    <Route path="edit" element={<Edit />} />
                                                    <Route path="notice" element={<Notice />} />
                                                    <Route path="order" element={<OrderAccount />} />
                                                    <Route path="favorite" element={<Favorite />} />
                                                    <Route path="help" element={<ChatHelper />} />
                                                    <Route path="setting" element={<Setting />} />
                                                </Route>
                                            </>
                                        )}
                                        {data.role == true && (
                                            <Route path="admin" element={<Admin />}>
                                                <Route index element={<AdminReviews />} />
                                                <Route path="orders" element={<AdminOrders />} />
                                                <Route path="chats/:id" element={<AdminItemChat />} />
                                                <Route path="chats" element={<AdminChats />} />
                                                <Route path="setting" element={<Setting />} />
                                            </Route>
                                        )}
                                    </>
                                )}
                                <Route path="*" element={<ErrorPage />} />
                            </Route>
                        </Routes>
                    </BrowserRouter>
                </ContextSurvey.Provider>
            </ContextLogin.Provider>
        </ContextData.Provider>
    );
}

export default App;
