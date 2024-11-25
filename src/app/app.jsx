import React, { useEffect } from "react";
import Main from "./main";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
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
import Order from "../blocks/AccountPage/order";
import Card from "./card";

const router = createBrowserRouter([
    {
        path: "/",
        element: <PageLayer />,
        children: [
            {
                path: "",
                element: <Main />,
            },
            {
                path: "password-change",
                element: <PasswordCange />,
            },
            {
                path: "catalog",
                element: <Catalog />
            },
            {
                path: "basket",
                element: <Basket />
            },
            {
                path: "article/:id",
                element: <Card />
            },
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
                        element: <Order />,
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

    return <RouterProvider router={router} />;
}

export default App;
