import React from "react";
import AccountLayer from "../../blocks/AccountPage/account-layer";
import { Outlet } from "react-router";

function Account() {
    return (
        <>
            <AccountLayer>
                <Outlet />
            </AccountLayer>
        </>
    );
}

export default Account;
