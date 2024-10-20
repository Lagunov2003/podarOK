import React from "react";
import Main from "./main";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import PageLayer from "../component/page-layer";
import PasswordCange from "./password";
import Account from "./account";
import User from "../blocks/AccountPage/user";

const router = createBrowserRouter([
    {
        path: "/",
        element: <PageLayer />,
        children: [
            {
                path: "/",
                element: <Main />,
            },
            {
                path: "/password-cange",
                element: <PasswordCange />,
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
                        path: "notice",
                        element: <User />,
                    },
                    {
                        path: "order",
                        element: <User />,
                    },
                    {
                        path: "favorite",
                        element: <User />,
                    },
                    {
                        path: "help",
                        element: <User />,
                    },
                    {
                        path: "setting",
                        element: <User />,
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
