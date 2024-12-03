import React, { useState } from "react";
import WrapperOrder from "../../blocks/OrderPage/wrapper-order";
import InfoOrder from "../../blocks/OrderPage/info-order";
import InfoPrice from "../../blocks/OrderPage/info-price";
import WrapperModal from "../../component/wrapper-modal";
import EditRecipient from "../../blocks/OrderPage/edit-recipient";
import EditAddress from "../../blocks/OrderPage/edit-address";

function Order() {
    const [modalRecipient, setModalRecipient] = useState(false)
    const [modalAddress, setModalAddress] = useState(false)
    const [acceptAddress, setAcceptAddress] = useState(false)


    return <WrapperOrder>
        <InfoOrder openModalRecipient={setModalRecipient} openModalAddress={setModalAddress} acceptAddress={acceptAddress}/>
        <InfoPrice />
        <WrapperModal activeModal={modalRecipient}>
            <EditRecipient closeModalRecipient={setModalRecipient}/>
        </WrapperModal>
        <WrapperModal activeModal={modalAddress}>
            <EditAddress closeModalAddress={setModalAddress} setAcceptAddress={setAcceptAddress}/>
        </WrapperModal>
    </WrapperOrder>
}

export default Order;