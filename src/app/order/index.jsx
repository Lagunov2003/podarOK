import React, { useEffect, useState } from "react";
import WrapperOrder from "../../blocks/OrderPage/wrapper-order";
import InfoOrder from "../../blocks/OrderPage/info-order";
import InfoPrice from "../../blocks/OrderPage/info-price";
import WrapperModal from "../../component/wrapper-modal";
import EditRecipient from "../../blocks/OrderPage/edit-recipient";
import EditAddress from "../../blocks/OrderPage/edit-address";
import { useNavigate, useParams } from "react-router";
import { responsePostOrder } from "../../tool/response";
import AcceptOrder from "../../component/accept-order";

function Order({ order }) {
    const { id } = useParams()
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
        price: order?.price || 0,
        discount: 0,
    });
    const [regOrder, setRegOrder] = useState(false);

    const [modalRecipient, setModalRecipient] = useState(false);
    const [modalAddress, setModalAddress] = useState(false);
    const [acceptAddress, setAcceptAddress] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const numberOrder = sessionStorage.getItem("numberOrder")

        if(numberOrder !== id) {
            navigate("/")
        } 
    }, [id, navigate])

    const handleOrderRegistration = async () => {
        if (
            dataOrder.address !== "" &&
            dataOrder.time !== "" &&
            dataOrder.recipient.name !== "" &&
            dataOrder.recipient.email !== "" &&
            dataOrder.recipient.tel !== ""
        ) {
            const status = await responsePostOrder({ dataOrder, order });

            if (status === 200) {
                setRegOrder(true);
                sessionStorage.removeItem("numberOrder")
            }
        }
    };

    return (
        <>
            {regOrder === false ? (
                <WrapperOrder>
                    <InfoOrder
                        openModalRecipient={setModalRecipient}
                        openModalAddress={setModalAddress}
                        acceptAddress={acceptAddress}
                        dataOrder={dataOrder}
                        setDataOrder={setDataOrder}
                    />
                    <InfoPrice
                        dataOrder={dataOrder}
                        handleOrderRegistration={handleOrderRegistration}
                        setDataOrder={setDataOrder}
                        order={order}
                    />
                    <WrapperModal activeModal={modalRecipient}>
                        <EditRecipient
                            closeModalRecipient={setModalRecipient}
                            recipient={dataOrder.recipient}
                            setDataOrder={setDataOrder}
                        />
                    </WrapperModal>
                    <WrapperModal activeModal={modalAddress}>
                        <EditAddress closeModalAddress={setModalAddress} setAcceptAddress={setAcceptAddress} setDataOrder={setDataOrder} />
                    </WrapperModal>
                </WrapperOrder>
            ) : (
                <AcceptOrder />
            )}
        </>
    );
}

export default Order;
