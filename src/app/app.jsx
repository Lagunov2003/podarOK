import React from "react";
import Main from "./main";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import PageLayer from "../component/page-layer";
import PasswordCange from "./password";
import Account from "./account";
import User from "../blocks/AccountPage/user";
import Setting from "../blocks/AccountPage/setting";
<<<<<<< HEAD
=======

>>>>>>> 0daf3b5a26c263562593456756e3b707db11a599

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
