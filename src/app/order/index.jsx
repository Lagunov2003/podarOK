import React, { useEffect, useState } from "react";
import WrapperOrder from "../../blocks/OrderPage/wrapper-order";
import InfoOrder from "../../blocks/OrderPage/info-order";
import InfoPrice from "../../blocks/OrderPage/info-price";
import WrapperModal from "../../component/wrapper-modal";
import EditRecipient from "../../blocks/OrderPage/edit-recipient";
import EditAddress from "../../blocks/OrderPage/edit-address";
import { useNavigate } from "react-router";

function Order() {
    const [dataOrder, setDataOrder] = useState({
        recipient: {
            name: "",
            email: "",
            tel: "",
        },
        deliveryDate: new Date(new Date().getTime() + 24 * 60 * 60 * 1000).toISOString().split("T")[0],
        address: "",
        addressPrice: 0,
        comment: "",
        time: "Любое время",
        fastDelivery: false,
        typePay: "card",
        price: 0,
        discount: 0
    });
    const [modalRecipient, setModalRecipient] = useState(false);
    const [modalAddress, setModalAddress] = useState(false);
    const [acceptAddress, setAcceptAddress] = useState(false);
    const navigate = useNavigate()

    useEffect(() => {
        console.log(dataOrder);

    }, [dataOrder])

    const handleOrderRegistration = () => {
        if(dataOrder.address != "" && dataOrder.time != "" && dataOrder.recipient.name != "" && dataOrder.recipient.email != "" && dataOrder.recipient.tel != "") {
            navigate("/")
        }
    }

    return (
        <WrapperOrder>
            <InfoOrder openModalRecipient={setModalRecipient} openModalAddress={setModalAddress} acceptAddress={acceptAddress} dataOrder={dataOrder} setDataOrder={setDataOrder}/>
            <InfoPrice dataOrder={dataOrder} handleOrderRegistration={handleOrderRegistration} setDataOrder={setDataOrder}/>
            <WrapperModal activeModal={modalRecipient}>
                <EditRecipient closeModalRecipient={setModalRecipient} recipient={dataOrder.recipient} setDataOrder={setDataOrder} />
            </WrapperModal>
            <WrapperModal activeModal={modalAddress}>
                <EditAddress closeModalAddress={setModalAddress} setAcceptAddress={setAcceptAddress} setDataOrder={setDataOrder}/>
            </WrapperModal>
        </WrapperOrder>
    );
}

export default Order;
