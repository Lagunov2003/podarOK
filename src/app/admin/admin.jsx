import React from "react";
import AdminLayer from "../../blocks/AdminPage/admin-layer";
import { Outlet } from "react-router";

function Admin() {

    return <AdminLayer>
        <Outlet />
    </AdminLayer>
}

export default Admin