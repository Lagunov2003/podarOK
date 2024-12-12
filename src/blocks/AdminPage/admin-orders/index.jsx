import React from "react";
import "./style.scss";

function AdminOrders() {
    return (
        <div className="admin-orders">
            <div className="admin-orders__search">
                <input type="text" placeholder="Найти заказ по номеру" />
                <div className="admin-orders__search-img">
                    <img src="/img/find.svg" alt="" />
                </div>
            </div>
            <div className="admin-orders__list">
                <div className="admin-orders__item">
                    <div className="admin-orders__item-img"></div>
                    <div className="admin-orders__item-info">
                        <span className="admin-orders__item-name">Комплект постельного белья </span>
                        <span className="admin-orders__item-price">1750 руб.</span>
                        <span className="admin-orders__item-status">Статус: В пути</span>
                    </div>
                    <p className="admin-orders__item-number">
                        <span>Заказ</span>
                        <span>№ 36</span>
                    </p>
                </div>
                <div className="admin-orders__item">
                    <div className="admin-orders__item-img"></div>
                    <div className="admin-orders__item-info">
                        <span className="admin-orders__item-name">Комплект постельного белья </span>
                        <span className="admin-orders__item-price">1750 руб.</span>
                        <span className="admin-orders__item-status">Статус: В пути</span>
                    </div>
                    <p className="admin-orders__item-number">
                        <span>Заказ</span>
                        <span>№ 36</span>
                    </p>
                </div>
                <div className="admin-orders__item">
                    <div className="admin-orders__item-img"></div>
                    <div className="admin-orders__item-info">
                        <span className="admin-orders__item-name">Комплект постельного белья </span>
                        <span className="admin-orders__item-price">1750 руб.</span>
                        <span className="admin-orders__item-status">Статус: В пути</span>
                    </div>
                    <p className="admin-orders__item-number">
                        <span>Заказ</span>
                        <span>№ 36</span>
                    </p>
                </div>
            </div>
        </div>
    );
}

export default AdminOrders;
