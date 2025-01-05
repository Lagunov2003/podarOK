export async function responseRegister(name, email, password) {

    try {
        const response = await fetch("http://localhost:8080/registration", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                "firstName": name,
                "email": email,
                "password": password
            })
        })

        if (response.status === 200) {
            return "успешно"
        } else if (response.status === 409) {
            return "повтор"
        } else {
            return "ошибка"
        }
    } catch (e) {

    }
}

export async function responseLogin(email, password) {

    try {
        const response = await fetch("http://localhost:8080/login", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                "email": email,
                "password": password
            })
        })

        if (response.status === 200) {
            const data = await response.json()
            localStorage.setItem("token", data.token)
            return "успешно"
        } else {
            return "ошибка"
        }
    } catch (e) {

    }
}

//Кабинет
export async function responseGetProfile(token) {

    try {
        const response = await fetch("http://localhost:8080/profile", {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
        })

        if (response.status === 200) {
            const data = await response.json()
            return data
        } else {
            return "ошибка"
        }
    } catch (e) {

    }
}

export async function responsePutProfile(data) {
    const token = localStorage.getItem("token")

    try {
        const response = await fetch("http://localhost:8080/profile", {
            method: "PUT",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                firstName: data.firstName,
                lastName: data.lastName || null,
                dateOfBirth: data.dateOfBirth || null,
                gender: data.gender,
                email: data.email,
                phoneNumber: data.phoneNumber || null
            })
        })

        return response.status
    } catch (e) {

    }
}


export async function responseGetCurrentOrders(setData) {

    const token = localStorage.getItem("token")

    try {
        const response = await fetch("http://localhost:8080/currentOrders", {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
        })

        if (response.status === 200) {
            const data = await response.json()
            setData(data)
        } else {
            return "ошибка"
        }
    } catch (e) {

    }
}


export async function responseGetOrdersHistory(setData) {

    const token = localStorage.getItem("token")

    try {
        const response = await fetch("http://localhost:8080/ordersHistory", {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
        })

        if (response.status === 200) {
            const data = await response.json()
            setData(data)
        } else {
            return "ошибка"
        }
    } catch (e) {

    }
}


export async function responseGetFavorites(setData) {

    const token = localStorage.getItem("token")

    try {
        const response = await fetch("http://localhost:8080/favorites", {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
        })

        if (response.status === 200) {
            const data = await response.json()
            setData(data)
        } else {
            return "ошибка"
        }
    } catch (e) {

    }
}


export async function responseDeleteFavorites() {

    const token = localStorage.getItem("token")

    try {
        const response = await fetch("http://localhost:8080/profile", {
            method: "DELETE",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
        })

        if (response.status === 200) {
            return "успешно"
        } else {
            return "ошибка"
        }
    } catch (e) {
        return "ошибка"
    }
}


export async function responseDeleteFromFavorites(giftId) {

    const token = localStorage.getItem("token")

    try {
        const response = await fetch("http://localhost:8080/deleteFromFavorites", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                giftId
            })
        })

        if (response.status === 200) {
            return "успешно"
        } else {
            return "ошибка"
        }
    } catch (e) {
        return "ошибка"
    }
}


export async function responseForgot(email) {

    try {
        const response = await fetch("http://localhost:8080/forgot", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                email
            })
        })

        if (response.status === 200) {
            return "успешно"
        } else {
            return "ошибка"
        }
    } catch (e) {
        return "ошибка"
    }
}

export async function responseResetPassword(code, email, password, confirmPassword) {
    let strUrl = ""
    let hed = {
        'Content-Type': 'application/json',
    }
    const token = localStorage.getItem("token")

    if (token) {
        strUrl = "http://localhost:8080/confirmChanges?code=" + code
        hed['Authorization'] = `Bearer ${token}`
    } else {
        strUrl = "http://localhost:8080/resetPassword?token=" + code
    }

    try {
        const response = await fetch(strUrl, {
            method: "POST",
            headers: hed,
            body: JSON.stringify({
                email,
                password,
                confirmPassword
            })
        })

        if (response.status === 200) {
            return "успешно"
        } else {
            return "ошибка"
        }
    } catch (e) {
        return "ошибка"
    }
}

export async function responseChangePassword() {

    const token = localStorage.getItem("token")

    try {
        const response = await fetch("http://localhost:8080/changePassword", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
        })

        if (response.status === 200) {
            return "успешно"
        } else {
            return "ошибка"
        }
    } catch (e) {
        return "ошибка"
    }
}


export async function responseGetNotifications(setData) {

    const token = localStorage.getItem("token")

    try {
        const response = await fetch("http://localhost:8080/notifications", {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
        })

        if (response.status === 200) {
            const data = await response.json()
            setData(data)
        } else {
            return "ошибка"
        }
    } catch (e) {

    }
}


//Каталог 
export async function responseGetCatalog(setList, setPage, page = 1, name, sort, dataResp = null) {
    let strUrl = "http://localhost:8080/catalog?page=" + page + "&name=" + name + "&sort=" + sort
    const token = localStorage.getItem("token")

    let hed = {
        'Content-Type': 'application/json',
    }

    if (token) {
        hed["Authorization"] = `Bearer ${token}`
    }

    try {
        const response = dataResp === null
            ? await fetch(strUrl, {
                method: "POST",
                headers: hed,
            })
            : await fetch(strUrl, {
                method: "POST",
                headers: hed,
                body: JSON.stringify({
                    ...dataResp
                })
            })

        const data = await response.json();
        setList(data["_embedded"].giftDtoList)
        setPage(data["page"])
    } catch (e) {
        setList([])
        setPage(1)
    }
}


export async function responseGetGift(setItem, id) {
    let strUrl = "http://localhost:8080/gift/" + id

    const token = localStorage.getItem("token")
    let hed = {
        'Content-Type': 'application/json',
    }

    if (token) {
        hed["Authorization"] = `Bearer ${token}`
    }

    try {
        const response = await fetch(strUrl, {
            method: "GET",
            headers: hed,
        });

        const data = await response.json();
        setItem(data)
    } catch (e) {

    }
}

export async function responsePostAddGiftReview(text, rating, giftId) {
    let strUrl = "http://localhost:8080/addGiftReview"

    const token = localStorage.getItem("token")

    try {
        await fetch(strUrl, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                text,
                rating,
                giftId
            })
        });
    } catch (e) {

    }
}


export async function responsePostAddToFavorites(giftId) {
    let strUrl = "http://localhost:8080/addToFavorites"

    const token = localStorage.getItem("token")

    try {
        await fetch(strUrl, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                giftId
            })
        });
    } catch (e) {

    }
}


//Корзина
export async function responseGetCart(token, setList) {
    let strUrl = "http://localhost:8080/cart"

    try {
        const response = await fetch(strUrl, {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
        });

        const data = await response.json();
        setList(data)
    } catch (e) {

    }
}

export async function responsePostCart(token, id, count = 1) {
    let strUrl = "http://localhost:8080/cart"

    try {
        await fetch(strUrl, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                giftId: id,
                itemCount: count
            })
        });

    } catch (e) {

    }
}

export async function responsePutCart(token, id, count) {
    let strUrl = "http://localhost:8080/cart"

    try {
        await fetch(strUrl, {
            method: "PUT",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                giftId: id,
                itemCount: count
            })
        });

    } catch (e) {

    }
}

export async function responsePostOrder(order) {
    let strUrl = "http://localhost:8080/order"

    const token = localStorage.getItem("token")

    try {
        const response = await fetch(strUrl, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                items: [
                    ...order.order.gift.reduce((acc, v) => acc = [...acc, { itemCount: v.itemCount, giftId: v.gift.id }], [])
                ],
                deliveryDate: order.dataOrder.deliveryDate,
                fromDeliveryTime: order.dataOrder.time === "Любое время" ? "08:00" : order.dataOrder.time.slice(" ")[1],
                ToDeliveryTime: order.dataOrder.time === "Любое время" ? "23:00" : order.dataOrder.time.slice(" ")[3],
                information: order.dataOrder.address,
                recipientName: order.dataOrder.recipient.name,
                recipientEmail: order.dataOrder.recipient.email,
                recipientPhoneNumber: order.dataOrder.recipient.tel,
                orderCost: order.dataOrder.price - order.dataOrder.discount + order.dataOrder.addressPrice
            })
        });

        return await response.status
    } catch (e) {

    }
}


//Отзывы на главной
export async function responseGetSiteReviews(setList) {
    let strUrl = "http://localhost:8080/getSiteReviews"

    try {
        const response = await fetch(strUrl, {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
            },
        });

        const data = await response.json();
        setList(data)
    } catch (e) {

    }
}

export async function responsePostAddSiteReviews(mark, review) {
    const token = localStorage.getItem("token")

    let strUrl = "http://localhost:8080/addSiteReview"

    try {
        const response = await fetch(strUrl, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                mark,
                review
            })
        });

        return response.status
    } catch (e) {

    }
}

//Админ
export async function responseGetNotAcceptedSiteReviews(setList) {
    let strUrl = "http://localhost:8080/getNotAcceptedSiteReviews"

    const token = localStorage.getItem("token")

    try {
        const response = await fetch(strUrl, {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
        });

        const data = await response.json();
        setList(data)
    } catch (e) {

    }
}


export async function responsePutchangeAcceptedStatusSiteReviews(id) {
    let strUrl = "http://localhost:8080/changeAcceptedStatusSiteReviews?id=" + id

    const token = localStorage.getItem("token")

    try {
        await fetch(strUrl, {
            method: "PUT",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
        });

    } catch (e) {

    }
}

export async function responseDeleteNotAcceptedSiteReviews(id) {
    let strUrl = "http://localhost:8080/deleteNotAcceptedSiteReviews?id=" + id

    const token = localStorage.getItem("token")

    try {
        await fetch(strUrl, {
            method: "DELETE",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
        });

    } catch (e) {

    }
}

export async function responseGetOrdersAdmin(setList) {
    let strUrl = "http://localhost:8080/getOrders?status="

    const token = localStorage.getItem("token")

    try {
        const response = await fetch(strUrl, {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
        });

        const data = await response.json();
        setList(data)
    } catch (e) {

    }
}


export async function responsePutChangeOrderStatus(orderId, newOrderStatus) {
    let strUrl = "http://localhost:8080/changeOrderStatus"

    const token = localStorage.getItem("token")

    try {
        await fetch(strUrl, {
            method: "PUT",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                orderId: orderId,
                newOrderStatus: newOrderStatus
            })
        });
    } catch (e) {

    }
}

//Чат
export async function responseGetAllDialogs(setList) {
    let strUrl = "http://localhost:8080/chat/allDialogs"

    const token = localStorage.getItem("token")

    try {
        const response = await fetch(strUrl, {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
        });

        const data = await response.json();
        setList(data)
    } catch (e) {

    }
}


export async function responseGetChatMessages(setData, email) {
    let strUrl = "http://localhost:8080/chat/messages?userEmail=" + email

    const token = localStorage.getItem("token")

    try {
        const resp = await fetch(strUrl, {
            method: "GET",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
        });

        const data = await resp.json()
        setData(data)
    } catch (e) {

    }
}

export async function responsePostChatSend(content, email) {
    let strUrl = "http://localhost:8080/chat/send"

    const token = localStorage.getItem("token")

    try {
        await fetch(strUrl, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({
                content: content,
                receiverEmail: email
            })
        });
    } catch (e) {

    }
}

// Карта от Яндекса 
export const getAdressFromCoordinates = async (lat, lon) => {
    const apiKey = "83f63b8a-a5c6-4cfc-a278-cc18d2859012"
    const response = await fetch(`https://geocode-maps.yandex.ru/1.x/?apikey=${apiKey}&geocode=${lon},${lat}&format=json`);
    const data = await response.json();

    if (
        data.response &&
        data.response.GeoObjectCollection &&
        data.response.GeoObjectCollection.featureMember &&
        data.response.GeoObjectCollection.featureMember.length > 0
    ) {
        const firstGeoObject = data.response.GeoObjectCollection.featureMember[1].GeoObject;
        const address = firstGeoObject.metaDataProperty.GeocoderMetaData.text;
        return address;
    } else {
        return "Address not found";
    }
};