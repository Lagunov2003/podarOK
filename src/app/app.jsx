import React from "react";
import Main from "./main";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import PageLayer from "../component/page-layer";
import PasswordCange from "./password";
import Account from "./account";
import User from "../blocks/AccountPage/user";
import Setting from "../blocks/AccountPage/setting";
import Edit from "../blocks/AccountPage/edit";
import Basket from "../blocks/AccountPage/basket";
import Notice from "../blocks/AccountPage/notice";
import ChatHelper from "../blocks/AccountPage/chat-helper";
import Favorite from "../blocks/AccountPage/favorite";
import Catalog from "./catalog";

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
                        element: <Basket />,
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
    return <RouterProvider router={router} />;
}

export default App;
