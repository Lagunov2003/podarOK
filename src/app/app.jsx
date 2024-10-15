import React from "react";
import Main from "./main";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import PageLayer from "../component/page-layer";
import Password from "./password";

const router = createBrowserRouter([
    {
        path: "/",
        element: <PageLayer />,
        children: [
          {
            path: "/",
            element: <Main />
          },
          {
            path: "password-cange",
            element: <Password />
          }
        ]
    },
]);

function App() {
    return <RouterProvider router={router} />;
}

export default App;
