import React, { useEffect } from "react";
import Main from "./main";
import { BrowserRouter, createBrowserRouter, Route, RouterProvider, Routes, useLocation } from "react-router-dom";
import PageLayer from "../component/page-layer";
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

const router = createBrowserRouter([
    {
        path: "/",
        element: <PageLayer />,
        children: [
            {
                path: "account",
                element: <Account />,
                children: [
                    {
                        path: "user",
                        element: <User />,
                    },
                    {
                        path:"user/edit",
                        element: <Edit />
                    },
                    {
                        path: "notice",
                        element: <Notice />,
                    },
                    {
                        path: "order",
                        element: <OrderAccount />,
                    },
                    {
                        path: "favorite",
                        element: <Favorite />,
                    },
                    {
                        path: "help",
                        element: <ChatHelper />,
                    },
                    {
                        path: "setting",
                        element: <Setting />,
                    },
                ],
            },
        ],
    },
]);

function App() {

    useEffect(() => {
        async function qwe(params) {

            const resp = await fetch("/api/catalog")
            const data = await resp.body

            console.log(data);
            
        }

        qwe()
    }, [])


    return <BrowserRouter>
        <Routes>
            <Route path="/" element={<PageLayer />}>
                <Route index element={<Main />} />
                <Route path="password-change" element={<PasswordCange />} />
                <Route path="catalog" element={<Catalog />} />
                <Route path="basket" element={<Basket />} />
                <Route path="article/:id" element={<Card />} />
                <Route path="order/:id" element={<Order />} />
            </Route>
        </Routes>
    </BrowserRouter>
}

export default App;
